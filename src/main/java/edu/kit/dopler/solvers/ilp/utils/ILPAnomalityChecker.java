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
package edu.kit.dopler.solvers.ilp.utils;

import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import edu.kit.dopler.model.Dopler;
import edu.kit.dopler.model.basic.EnumerationLiteral;
import edu.kit.dopler.model.decisions.Decision.DecisionType;
import edu.kit.dopler.model.decisions.EnumerationDecision;
import edu.kit.dopler.model.decisions.IDecision;
import edu.kit.dopler.solvers.ilp.ILPConstants;
import edu.kit.dopler.solvers.ilp.ILPContext;
import edu.kit.dopler.solvers.ilp.encoders.ILPGlobalConstraintEncoder;
import edu.kit.dopler.solvers.shared.AnomalyReport;
import edu.kit.dopler.solvers.shared.SolverUtils;
import edu.kit.dopler.solvers.shared.ValueAnomaly;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Detects logical anomalies (False Optionals, Dead Decisions, and their values) using ILP constraints.
 */
public final class ILPAnomalityChecker {

    private static final Logger LOGGER = LoggerFactory.getLogger(ILPAnomalityChecker.class);

    private ILPAnomalityChecker() {
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
        try (ILPContext context = ILPContext.create()) {
            ILPGlobalConstraintEncoder.encodeToILP(dopler, context);
            MPSolver solver = context.solver();

            // If the base model is INFEASIBLE, anomaly checks are impossible.
            if (solver.solve() == MPSolver.ResultStatus.INFEASIBLE) {
                LOGGER.warn("The DOPLER model is INFEASIBLE. Cannot perform anomaly checks.");
                return AnomalyReport.empty(true);
            }

            return findAnomalies(dopler, context, solver);

        } catch (Exception e) {
            LOGGER.error("Error during ILP anomaly detection: {}", e.getMessage(), e);
            return AnomalyReport.empty(true);
        }
    }

    private static AnomalyReport findAnomalies(Dopler dopler, ILPContext context, MPSolver solver) {
        List<IDecision<?>> deadDecisions = new ArrayList<>();
        List<IDecision<?>> falseOptionalDecisions = new ArrayList<>();
        List<ValueAnomaly> deadValues = new ArrayList<>();
        List<ValueAnomaly> falseOptionalValues = new ArrayList<>();

        for (IDecision<?> d : dopler.getDecisions()) {
            String dName = SolverUtils.toStringConst(d);
            MPVariable isTaken = context.getCoreVar(dName + ILPConstants.TAKEN_SUFFIX);
            if (isTaken == null) continue;

            double oTakenLb = isTaken.lb();
            double oTakenUb = isTaken.ub();

            // Check Dead Decision: Does forcing the decision to be taken result in an invalid model?
            isTaken.setLb(1.0);
            if (solver.solve() == MPSolver.ResultStatus.INFEASIBLE) {
                deadDecisions.add(d);
                isTaken.setLb(oTakenLb);
                isTaken.setUb(oTakenUb);
                continue; // A dead decision cannot have valid values.
            }

            if (d.getDecisionType() == DecisionType.ENUM) {
                EnumerationDecision ed = (EnumerationDecision) d;
                for (EnumerationLiteral lit : ed.getEnumeration().getEnumerationLiterals()) {
                    MPVariable litVar = context.getCoreVar(dName + "_" + lit.getValue());
                    if (litVar == null) continue;

                    double oLb = litVar.lb();
                    double oUb = litVar.ub();

                    // If forcing this literal results in INFEASIBLE, it's a dead value.
                    litVar.setLb(1.0);
                    if (solver.solve() == MPSolver.ResultStatus.INFEASIBLE) {
                        deadValues.add(new ValueAnomaly(d, lit));
                    }
                    litVar.setLb(oLb);

                    // If forbidding this literal results in INFEASIBLE, it must be chosen.
                    litVar.setUb(0.0);
                    if (solver.solve() == MPSolver.ResultStatus.INFEASIBLE) {
                        falseOptionalValues.add(new ValueAnomaly(d, lit));
                    }
                    litVar.setUb(oUb);
                }
            } else if (d.getDecisionType() == DecisionType.BOOLEAN) {
                MPVariable valVar = context.getCoreVar(dName + ILPConstants.VALUE_SUFFIX);
                if (valVar == null) continue;

                double oLb = valVar.lb();
                double oUb = valVar.ub();

                // If setting to true breaks the model, true is dead and false is implicitly forced.
                valVar.setLb(1.0);
                if (solver.solve() == MPSolver.ResultStatus.INFEASIBLE) {
                    deadValues.add(new ValueAnomaly(d, true));
                    falseOptionalValues.add(new ValueAnomaly(d, false));
                }
                valVar.setLb(oLb);

                // If setting to false breaks the model, false is dead and true is implicitly forced.
                valVar.setUb(0.0);
                if (solver.solve() == MPSolver.ResultStatus.INFEASIBLE) {
                    deadValues.add(new ValueAnomaly(d, false));
                    falseOptionalValues.add(new ValueAnomaly(d, true));
                }
                valVar.setUb(oUb);
            } else if (d.getDecisionType() == DecisionType.NUMBER) {
                MPVariable valVar = context.getCoreVar(dName + ILPConstants.VALUE_SUFFIX);
                if (valVar != null) {
                    solver.objective().setCoefficient(valVar, 1.0);
                    solver.objective().setMinimization();
                    solver.solve();
                    double minVal = valVar.solutionValue();

                    solver.objective().setMaximization();
                    solver.solve();
                    double maxVal = valVar.solutionValue();

                    solver.objective().setCoefficient(valVar, 0.0);

                    // If the minimum possible value and maximum possible value are exactly the same,
                    // it must be chosen.
                    if (Math.abs(maxVal - minVal) < 1e-6) {
                        falseOptionalValues.add(new ValueAnomaly(d, minVal));
                    }
                }
            }

            // Clean up the `isTaken` assumption for the current decision
            isTaken.setLb(oTakenLb);

            // Check False Optional Decision: Does omitting the decision result in an invalid model?
            isTaken.setUb(0.0);
            if (solver.solve() == MPSolver.ResultStatus.INFEASIBLE) {
                falseOptionalDecisions.add(d);
            }
            isTaken.setUb(oTakenUb);
        }

        Collections.sort(deadValues);
        Collections.sort(falseOptionalValues);
        return new AnomalyReport(false, deadDecisions, falseOptionalDecisions, deadValues, falseOptionalValues);
    }
}
