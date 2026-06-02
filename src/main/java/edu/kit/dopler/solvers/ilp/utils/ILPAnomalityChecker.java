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
package edu.kit.dopler.solvers.ilp.utils;

import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import edu.kit.dopler.model.Dopler;
import edu.kit.dopler.model.decisions.IDecision;
import edu.kit.dopler.solvers.ilp.ILPConstants;
import edu.kit.dopler.solvers.ilp.ILPContext;
import edu.kit.dopler.solvers.ilp.encoders.ILPGlobalConstraintEncoder;
import edu.kit.dopler.solvers.shared.AnomalyReport;
import edu.kit.dopler.solvers.shared.SolverUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Detects logical anomalies (False Optionals, Dead Decisions) using ILP constraints.
 */
public final class ILPAnomalityChecker {

    private static final Logger LOGGER = LoggerFactory.getLogger(ILPAnomalityChecker.class);

    private ILPAnomalityChecker() {
        throw new IllegalStateException("Utility class can't be instantiated");
    }

    /**
     * Detects logical anomalies in a DOPLER model using ILP.
     * <p>
     * It aggregates multiple anomaly checks (Dead Decisions, False Optionals) and
     * returns a single report detailing exactly which decisions are anomalous.
     * </p>
     *
     * @param dopler The DOPLER model to analyze.
     * @return An {@link AnomalyReport} containing the results of the anomaly detection.
     */
    public static AnomalyReport detectAnomalies(final Dopler dopler) {
        try (ILPContext context = ILPContext.create()) {
            ILPGlobalConstraintEncoder.encodeToILP(dopler, context);
            MPSolver solver = context.solver();

            MPSolver.ResultStatus status = solver.solve();
            // Are there any valid configurations?
            if (status == MPSolver.ResultStatus.INFEASIBLE) {
                LOGGER.warn("The DOPLER model is INFEASIBLE. Cannot perform anomaly checks.");
                return new AnomalyReport(true, Collections.emptyList(), Collections.emptyList());
            }

            List<IDecision<?>> dead = findDeadDecisions(dopler, context, solver);
            List<IDecision<?>> falseOptional = findFalseOptionalDecisions(dopler, context, solver);

            return new AnomalyReport(false, dead, falseOptional);
        } catch (Exception e) {
            LOGGER.error("Error during ILP anomaly detection: {}", e.getMessage(), e);
            return new AnomalyReport(true, Collections.emptyList(), Collections.emptyList());
        }
    }

    /**
     * Checks the model for "Dead" decisions.
     * <p>
     * A decision is "Dead" if there is no possible way to choose it. Across all
     * valid configurations, the rules or visibility conditions prevent it from ever
     * becoming active.
     * </p>
     * <p>
     * ILP Logic:<br/>
     * Is there any valid configuration where this decision is taken?
     * If the solver returns INFEASIBLE, activating this decision contradicts the model's rules,
     * making the decision unreachable.
     * </p>
     * @param dopler  The DOPLER model.
     * @param context The ILP context containing the variables.
     * @param solver  The OR-Tools solver instance.
     * @return A list of Dead Decisions.
     */
    private static List<IDecision<?>> findDeadDecisions(final Dopler dopler, ILPContext context, MPSolver solver) {

        List<IDecision<?>> deadDecisions = new ArrayList<>();

        for (IDecision<?> d : dopler.getDecisions()) {
            MPVariable var = getTakenVariable(d, context);
            if (var == null) {
                continue;
            }

            double originalLb = var.lb();
            double originalUb = var.ub();

            // Force decision to be taken
            var.setLb(1.0);
            var.setUb(1.0);

            if (solver.solve() == MPSolver.ResultStatus.INFEASIBLE) {
                LOGGER.trace("Anomaly -> Dead Decision detected: {}", d.getDisplayId());
                deadDecisions.add(d);
            }

            // "Pop" state
            var.setLb(originalLb);
            var.setUb(originalUb);
        }

        return deadDecisions;
    }

    /**
     * Checks the model for "False Optional" decisions.
     * <p>
     * A decision is "False Optional" if it appears to the user as optional, but the
     * rules force it to be taken in every valid configuration.
     * </p>
     * <p>
     * ILP Logic:<br/>
     * Is there any valid configuration where this decision is not taken?
     * If the solver returns INFEASIBLE, skipping the decision violates the rules,
     * therefore the decision is mandatory.
     * </p>
     * meaning it is inherently mandatory.
     * @param dopler  The DOPLER model.
     * @param context The ILP context containing the variables.
     * @param solver  The OR-Tools solver instance.
     * @return A list of False Optional decisions.
     */
    private static List<IDecision<?>> findFalseOptionalDecisions(
            final Dopler dopler, ILPContext context, MPSolver solver) {

        List<IDecision<?>> falseOptionalDecisions = new ArrayList<>();

        for (IDecision<?> d : dopler.getDecisions()) {
            MPVariable var = getTakenVariable(d, context);
            if (var == null) {
                continue;
            }

            double originalLb = var.lb();
            double originalUb = var.ub();

            // Force decision to NOT be taken
            var.setLb(0.0);
            var.setUb(0.0);

            if (solver.solve() == MPSolver.ResultStatus.INFEASIBLE) {
                LOGGER.trace("Anomaly -> False Optional Decision detected: {}", d.getDisplayId());
                falseOptionalDecisions.add(d);
            }

            // "Pop" state
            var.setLb(originalLb);
            var.setUb(originalUb);
        }

        return falseOptionalDecisions;
    }

    private static MPVariable getTakenVariable(IDecision<?> d, ILPContext context) {
        return context.getCoreVar(SolverUtils.toStringConst(d) + ILPConstants.TAKEN_SUFFIX);
    }
}
