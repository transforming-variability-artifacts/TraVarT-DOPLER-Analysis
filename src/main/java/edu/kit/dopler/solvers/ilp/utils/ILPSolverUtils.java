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

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import edu.kit.dopler.model.Dopler;
import edu.kit.dopler.solvers.ilp.ILPContext;
import edu.kit.dopler.solvers.ilp.encoders.ILPGlobalConstraintEncoder;
import java.util.HashMap;
import java.util.Map;

public final class ILPSolverUtils {

    private ILPSolverUtils() {
        throw new IllegalStateException("Utility class can't be instantiated");
    }

    /**
     * Checks if the given DOPLER model is satisfiable.
     * @param dopler The DOPLER model to verify.
     * @return True if at least one valid configuration exists, otherwise false.
     */
    public static boolean isSatisfiable(final Dopler dopler) {
        try (ILPContext context = ILPContext.create()) {
            ILPGlobalConstraintEncoder.encodeToILP(dopler, context);

            MPSolver.ResultStatus status = context.solver().solve();
            return status == MPSolver.ResultStatus.FEASIBLE || status == MPSolver.ResultStatus.OPTIMAL;
        }
    }

    /**
     * Solves and counts all finite discrete configurations of the provided model.
     * Continuous variables are optimized/solved relative to each discrete configuration,
     * but do not define unique configurations themselves.
     * @param dopler The DOPLER model to check.
     * @return The total number of valid configurations.
     */
    public static int countConfigurations(final Dopler dopler) {
        int count = 0;

        try (ILPContext context = ILPContext.create()) {
            ILPGlobalConstraintEncoder.encodeToILP(dopler, context);

            MPSolver solver = context.solver();
            MPVariable[] boolVars = context.getCoreBoolVars().values().toArray(new MPVariable[0]);
            MPVariable[] realVars = context.getCoreRealVars().values().toArray(new MPVariable[0]);

            double M = ILPLogic.getBigM(realVars);

            while (true) {
                MPSolver.ResultStatus status = solver.solve();

                if (status != MPSolver.ResultStatus.FEASIBLE && status != MPSolver.ResultStatus.OPTIMAL) {
                    break; // Exhausted all configurations or model is infeasible
                }

                count++;

                boolean[] selected = new boolean[boolVars.length];
                int trueCount = 0;

                Map<MPVariable, Double> realVarSolutions = new HashMap<>();
                for (MPVariable rVar : realVars) {
                    realVarSolutions.put(rVar, rVar.solutionValue());
                }

                for (int i = 0; i < boolVars.length; i++) {
                    selected[i] = boolVars[i].solutionValue() > 0.5;
                    if (selected[i]) {
                        trueCount++;
                    }
                }

                // The exclusion constraint ensures that at least one decision is different
                // Sum(Vars_true) - Sum(Vars_false) - Sum(diff_reals) <= Count(Vars_true) - 1
                MPConstraint exclude = solver.makeConstraint(Double.NEGATIVE_INFINITY, trueCount - 1);

                // Exclude exact boolean matches
                for (int i = 0; i < boolVars.length; i++) {
                    exclude.setCoefficient(boolVars[i], selected[i] ? 1 : -1);
                }

                // Exclude exact real matches +- Epsilon
                /*
                                for (MPVariable rVar : realVars) {
                                    double val = realVarSolutions.get(rVar);

                                    // greater_var = 1 => rVar >= val + EPSILON
                                    // Formula: rVar - M * greater_var >= val + EPSILON - M
                                    MPVariable greaterVar = context.createAuxBoolVar("greater_" + rVar.name() + "_c" + count);
                                    MPConstraint cGreater =
                                            solver.makeConstraint(val + ILPConstants.EPSILON - M, Double.POSITIVE_INFINITY);
                                    cGreater.setCoefficient(rVar, 1);
                                    cGreater.setCoefficient(greaterVar, -M);

                                    // lesser_var = 1 => rVar <= val - EPSILON
                                    // Formula: rVar + M * lesser_var <= val - EPSILON + M
                                    MPVariable lesserVar = context.createAuxBoolVar("lesser_" + rVar.name() + "_c" + count);
                                    MPConstraint cLesser =
                                            solver.makeConstraint(Double.NEGATIVE_INFINITY, val - ILPConstants.EPSILON + M);
                                    cLesser.setCoefficient(rVar, 1);
                                    cLesser.setCoefficient(lesserVar, M);

                                    // Add the delta bounds to the exclusion constraint.
                                    exclude.setCoefficient(greaterVar, -1);
                                    exclude.setCoefficient(lesserVar, -1);
                                }
                */
            }
        }

        return count;
    }
}
