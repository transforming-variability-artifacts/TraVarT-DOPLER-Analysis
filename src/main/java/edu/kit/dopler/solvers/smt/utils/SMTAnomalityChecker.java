/*******************************************************************************
 * SPDX-License-Identifier: MPL-2.0
 *
 * Copyright (c) 2026 Karlsruhe Institute of Technology (KIT)
 * KASTEL - Dependability of Software-intensive Systems
 *
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *******************************************************************************/
package edu.kit.dopler.solvers.smt.utils;

import edu.kit.dopler.model.Dopler;
import edu.kit.dopler.model.basic.EnumerationLiteral;
import edu.kit.dopler.model.decisions.Decision.DecisionType;
import edu.kit.dopler.model.decisions.EnumerationDecision;
import edu.kit.dopler.model.decisions.IDecision;
import edu.kit.dopler.solvers.shared.AnomalyReport;
import edu.kit.dopler.solvers.shared.SolverUtils;
import edu.kit.dopler.solvers.shared.ValueAnomaly;
import edu.kit.dopler.solvers.smt.SMTConstants;
import edu.kit.dopler.solvers.smt.SMTContext;
import edu.kit.dopler.solvers.smt.encoders.SMTGlobalConstraintEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sosy_lab.java_smt.api.*;
import org.sosy_lab.java_smt.api.NumeralFormula.RationalFormula;

/**
 * Detects logical anomalies within a DOPLER model using SMT constraints.
 */
public final class SMTAnomalityChecker {

    private static final Logger LOGGER = LoggerFactory.getLogger(SMTAnomalityChecker.class);

    private SMTAnomalityChecker() {
        throw new IllegalStateException("Utility class can't be instantiated");
    }

    /**
     * Detects logical anomalies in a DOPLER model.
     * Evaluates Dead/False Optional decisions and Dead/False Optional decision values.
     *
     * @param dopler The DOPLER model to analyze.
     * @return The {@link AnomalyReport}.
     */
    public static AnomalyReport detectAnomalies(final Dopler dopler) {
        try (SMTContext context = SMTContext.create(SMTUtils.createSolverContext(), true)) {
            SMTGlobalConstraintEncoder.encodeToSMT(dopler, context);
            ProverEnvironment prover = context.prover();
            BooleanFormulaManager bfm = context.fm().getBooleanFormulaManager();

            // If the base model is UNSAT, anomaly checks are impossible.
            if (prover.isUnsat()) {
                LOGGER.warn("The DOPLER model is UNSATISFIABLE. Cannot perform anomaly checks.");
                return AnomalyReport.empty(true);
            }

            return findAnomalies(dopler, context, prover, bfm);

        } catch (SolverException | InterruptedException e) {
            LOGGER.error("SMT Error during anomaly detection: {}", e.getMessage(), e);
            return AnomalyReport.empty(true);
        } catch (Exception e) {
            LOGGER.error("Unexpected error during anomaly detection: {}", e.getMessage(), e);
            return AnomalyReport.empty(true);
        }
    }

    private static AnomalyReport findAnomalies(
            Dopler dopler, SMTContext context, ProverEnvironment prover, BooleanFormulaManager bfm)
            throws SolverException, InterruptedException {

        List<IDecision<?>> deadDecisions = new ArrayList<>();
        List<IDecision<?>> falseOptionalDecisions = new ArrayList<>();
        List<ValueAnomaly> deadValues = new ArrayList<>();
        List<ValueAnomaly> falseOptionalValues = new ArrayList<>();

        FormulaManager fm = context.fm();

        for (IDecision<?> d : dopler.getDecisions()) {
            String dName = SolverUtils.toStringConst(d);
            BooleanFormula isTaken = (BooleanFormula) context.getVar(dName + SMTConstants.TAKEN_SUFFIX);

            // Check Dead Decision: Does forcing the decision to be taken result in an invalid model?
            if (isUnsatUnderConstraint(prover, isTaken)) {
                deadDecisions.add(d);
                continue; // A dead decision cannot have valid values.
            }

            // Check False Optional Decision: Does omitting the decision result in an invalid model?
            if (isUnsatUnderConstraint(prover, bfm.not(isTaken))) {
                falseOptionalDecisions.add(d);
            }

            // Evaluate Decision Values
            // Assume the decision is taken to evaluate if its values are dead or forced.
            prover.push();
            prover.addConstraint(isTaken);

            if (d.getDecisionType() == DecisionType.ENUM) {
                EnumerationDecision ed = (EnumerationDecision) d;
                for (EnumerationLiteral lit : ed.getEnumeration().getEnumerationLiterals()) {
                    BooleanFormula litVar = (BooleanFormula) context.getVar(dName + "_" + lit.getValue());

                    // If forcing this literal results in UNSAT, it's a dead value.
                    if (isUnsatUnderConstraint(prover, litVar)) {
                        deadValues.add(new ValueAnomaly(d, lit));
                    }
                    // If forbidding this literal results in UNSAT, it must be chosen.
                    else if (isUnsatUnderConstraint(prover, bfm.not(litVar))) {
                        falseOptionalValues.add(new ValueAnomaly(d, lit));
                    }
                }
            } else if (d.getDecisionType() == DecisionType.BOOLEAN) {
                BooleanFormula valVar = (BooleanFormula) context.getVar(dName + SMTConstants.VALUE_SUFFIX);

                // Since the decision is forced to be taken, the other value can't be optional.
                if (isUnsatUnderConstraint(prover, valVar)) {
                    // If setting to true breaks the model, true is dead and false is implicitly forced.
                    deadValues.add(new ValueAnomaly(d, true));
                    falseOptionalValues.add(new ValueAnomaly(d, false));
                } else if (isUnsatUnderConstraint(prover, bfm.not(valVar))) {
                    // If setting to false breaks the model, false is dead and true is implicitly forced.
                    deadValues.add(new ValueAnomaly(d, false));
                    falseOptionalValues.add(new ValueAnomaly(d, true));
                }
            } else if (d.getDecisionType() == DecisionType.NUMBER) {
                RationalFormula valVar = (RationalFormula) context.getVar(dName + SMTConstants.VALUE_SUFFIX);
                RationalFormulaManager rfm = fm.getRationalFormulaManager();

                // Needed to generate the model.
                if (prover.isUnsat()) {
                    throw new IllegalStateException("Model must be valid");
                }
                // Numbers have infinite domains. Check if forbidding the current value breaks the model.
                // If it does, it's the only valid number.
                try (Model model = prover.getModel()) {
                    Object val = model.evaluate(valVar);
                    if (val != null) {
                        RationalFormula valFormula = rfm.makeNumber(val.toString());
                        if (isUnsatUnderConstraint(prover, bfm.not(rfm.equal(valVar, valFormula)))) {
                            falseOptionalValues.add(new ValueAnomaly(d, val));
                        }
                    }
                }
            } else if (d.getDecisionType() == DecisionType.STRING) {
                StringFormula valVar = (StringFormula) context.getVar(dName + SMTConstants.VALUE_SUFFIX);
                StringFormulaManager sfm = fm.getStringFormulaManager();

                // Strings have infinite domains. Check if forbidding the current value breaks the model.
                // If it does, it's the only valid string.
                try (Model model = prover.getModel()) {
                    String val = model.evaluate(valVar);
                    if (val != null) {
                        StringFormula valFormula = sfm.makeString(val);
                        if (isUnsatUnderConstraint(prover, bfm.not(sfm.equal(valVar, valFormula)))) {
                            falseOptionalValues.add(new ValueAnomaly(d, val));
                        }
                    }
                }
            }

            // Pop 'isTaken'
            prover.pop();
        }

        Collections.sort(deadValues);
        Collections.sort(falseOptionalValues);
        return new AnomalyReport(false, deadDecisions, falseOptionalDecisions, deadValues, falseOptionalValues);
    }

    /**
     * Helper method to temporarily push a constraint, check for satisfiability, and pop the constraint.
     *
     * @param prover     The SMT prover.
     * @param constraint The constraint to test.
     * @return True if the model is UNSATISFIABLE with the constraint, false otherwise.
     */
    private static boolean isUnsatUnderConstraint(ProverEnvironment prover, BooleanFormula constraint)
            throws SolverException, InterruptedException {
        prover.push();
        prover.addConstraint(constraint);
        boolean isUnsat = prover.isUnsat();
        prover.pop();
        return isUnsat;
    }
}
