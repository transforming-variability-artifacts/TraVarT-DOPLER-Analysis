/*******************************************************************************
 * SPDX-License-Identifier: MPL-2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/.
 *
 * Copyright 2026 Karlsruhe Institute of Technology (KIT)
 * KASTEL - Dependability of Software-intensive Systems
 *******************************************************************************/
package edu.kit.dopler.solvers.smt.utils;

import edu.kit.dopler.model.Dopler;
import edu.kit.dopler.model.decisions.IDecision;
import edu.kit.dopler.solvers.shared.AnomalyReport;
import edu.kit.dopler.solvers.shared.SolverUtils;
import edu.kit.dopler.solvers.smt.SMTConstants;
import edu.kit.dopler.solvers.smt.SMTContext;
import edu.kit.dopler.solvers.smt.encoders.SMTGlobalConstraintEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sosy_lab.java_smt.api.BooleanFormula;
import org.sosy_lab.java_smt.api.BooleanFormulaManager;
import org.sosy_lab.java_smt.api.ProverEnvironment;
import org.sosy_lab.java_smt.api.SolverException;

/**
 * Detects logical anomalies (False Optionals, Dead Decisions) using SMT constraints.
 */
public final class SMTAnomalityChecker {

    private static final Logger LOGGER = LoggerFactory.getLogger(SMTAnomalityChecker.class);

    private SMTAnomalityChecker() {
        throw new IllegalStateException("Utility class can't be instantiated");
    }

    /**
     * Detects logical anomalies in a DOPLER model.
     * <p>
     * It aggregates multiple anomaly checks (Dead Decisions, False Optionals) and
     * returns a single report detailing exactly which decisions are anomalous.
     * </p>
     *
     * @param dopler The DOPLER model to analyze.
     * @return An {@link AnomalyReport} containing the results of the anomaly detection.
     */
    public static AnomalyReport detectAnomalies(final Dopler dopler) {
        try (SMTContext context = SMTContext.create(SMTUtils.createSolverContext())) {
            SMTGlobalConstraintEncoder.encodeToSMT(dopler, context);
            ProverEnvironment prover = context.prover();
            BooleanFormulaManager bfm = context.fm().getBooleanFormulaManager();

            // Are there any valid configurations?
            if (prover.isUnsat()) {
                LOGGER.warn(
                        "The DOPLER model is UNSATISFIABLE. It has 0 valid configurations. Cannot perform anomaly checks.");
                return new AnomalyReport(true, Collections.emptyList(), Collections.emptyList());
            }

            List<IDecision<?>> dead = findDeadDecisions(dopler, bfm, prover, context);
            List<IDecision<?>> falseOptional = findFalseOptionalDecisions(dopler, bfm, prover, context);

            return new AnomalyReport(false, dead, falseOptional);

        } catch (SolverException | InterruptedException e) {
            LOGGER.error("SMT Error during anomaly detection: {}", e.getMessage(), e);
            // Treat solver failures as an unsat/broken base model
            return new AnomalyReport(true, Collections.emptyList(), Collections.emptyList());
        } catch (Exception e) {
            LOGGER.error("Unexpected error during anomaly detection: {}", e.getMessage(), e);
            return new AnomalyReport(true, Collections.emptyList(), Collections.emptyList());
        }
    }

    /**
     * Checks the model for "False Optional" decisions.
     * <p>
     * A decision is "False Optional" if it appears to the user as optional, but the
     * rules force it to be taken in every valid configuration.
     * </p>
     * <p>
     * SMT Logic:<br/>
     * Is there any valid configuration where this decision is not taken?
     * If the solver returns nUNSATISFIABLE, skipping the decision violates the rules,
     * therefore the decision is mandatory.
     * </p>
     *
     * @param dopler The DOPLER model to analyze.
     * @return A list of decisions identified as False Optional.
     * @throws IllegalStateException if the base model is UNSATISFIABLE.
     */
    public static List<IDecision<?>> findFalseOptionalDecisions(final Dopler dopler) {
        try (SMTContext context = SMTContext.create(SMTUtils.createSolverContext())) {
            SMTGlobalConstraintEncoder.encodeToSMT(dopler, context);
            ProverEnvironment prover = context.prover();
            BooleanFormulaManager bfm = context.fm().getBooleanFormulaManager();

            if (prover.isUnsat()) {
                throw new IllegalStateException("Base model is UNSAT. Cannot perform false optional decision checks.");
            }

            return findFalseOptionalDecisions(dopler, bfm, prover, context);

        } catch (SolverException | InterruptedException e) {
            LOGGER.error("SMT Error in findFalseOptionalDecisions: {}", e.getMessage(), e);
            throw new RuntimeException("SMT check failed", e);
        }
    }

    private static List<IDecision<?>> findFalseOptionalDecisions(
            final Dopler dopler, BooleanFormulaManager bfm, ProverEnvironment prover, SMTContext context)
            throws SolverException, InterruptedException {

        List<IDecision<?>> anomalousDecisions = new ArrayList<>();

        for (IDecision<?> d : dopler.getDecisions()) {
            BooleanFormula isTaken =
                    (BooleanFormula) context.getVar(SolverUtils.toStringConst(d) + SMTConstants.TAKEN_SUFFIX);

            prover.push();

            // Force this decision not to be taken
            prover.addConstraint(bfm.not(isTaken));

            // If no valid configuration exists under this assumption, it must be mandatory.
            if (prover.isUnsat()) {
                LOGGER.trace("Anomaly -> False Optional Decision detected: {}", d.getDisplayId());
                anomalousDecisions.add(d);
            }

            prover.pop();
        }

        return anomalousDecisions;
    }

    /**
     * Checks the model for "Dead" decisions.
     * <p>
     * A decision is "Dead" if there is no possible way to choose it. Across all
     * valid configurations, the rules or visibility conditions prevent it from ever
     * becoming active.
     * </p>
     * <p>
     * SMT Logic:<br/>
     * Is there any valid configuration where this decision is taken?
     * If the solver returns UNSATISFIABLE, activating this decision contradicts the model's rules,
     * making the decision unreachable.
     * </p>
     *
     * @param dopler The DOPLER model to analyze.
     * @return A list of decisions identified as Dead.
     * @throws IllegalStateException if the base model is UNSATISFIABLE.
     */
    public static List<IDecision<?>> findDeadDecisions(final Dopler dopler) {
        try (SMTContext context = SMTContext.create(SMTUtils.createSolverContext())) {
            SMTGlobalConstraintEncoder.encodeToSMT(dopler, context);
            ProverEnvironment prover = context.prover();
            BooleanFormulaManager bfm = context.fm().getBooleanFormulaManager();

            if (prover.isUnsat()) {
                throw new IllegalStateException("Base model is UNSAT. Cannot perform dead decision checks.");
            }

            return findDeadDecisions(dopler, bfm, prover, context);

        } catch (SolverException | InterruptedException e) {
            LOGGER.error("SMT Error in findDeadDecisions: {}", e.getMessage(), e);
            throw new RuntimeException("SMT check failed", e);
        }
    }

    private static List<IDecision<?>> findDeadDecisions(
            final Dopler dopler, BooleanFormulaManager bfm, ProverEnvironment prover, SMTContext context)
            throws SolverException, InterruptedException {

        List<IDecision<?>> deadDecisions = new ArrayList<>();

        for (IDecision<?> d : dopler.getDecisions()) {
            BooleanFormula isTaken =
                    (BooleanFormula) context.getVar(SolverUtils.toStringConst(d) + SMTConstants.TAKEN_SUFFIX);

            prover.push();

            // Force this decision to be taken
            prover.addConstraint(isTaken);

            // If no valid configuration allows this, the decision is dead.
            if (prover.isUnsat()) {
                LOGGER.trace("Anomaly -> Dead Decision detected: {}", d.getDisplayId());
                deadDecisions.add(d);
            }

            prover.pop();
        }

        return deadDecisions;
    }
}
