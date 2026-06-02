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

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import edu.kit.dopler.model.Dopler;
import edu.kit.dopler.solvers.ilp.ILPContext;
import edu.kit.dopler.solvers.ilp.encoders.ILPGlobalConstraintEncoder;
import java.util.ArrayList;
import java.util.List;

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
     * @return The total number of valid configurations, or -1 if the model is unbounded.
     */
    public static int countConfigurations(final Dopler dopler) {
        int count = 0;

        try (ILPContext context = ILPContext.create()) {
            ILPGlobalConstraintEncoder.encodeToILP(dopler, context);

            MPSolver solver = context.solver();
            MPVariable[] allCoreVars = context.getCoreVars().values().toArray(new MPVariable[0]);

            // Only create exclusion based on discrete decisions.
            // TODO: Add support for Reals later, currently under counts.
            List<MPVariable> boolVarsList = new ArrayList<>();
            for (MPVariable var : allCoreVars) {
                if (isBooleanBound(var)) {
                    boolVarsList.add(var);
                }
            }
            MPVariable[] boolVars = boolVarsList.toArray(new MPVariable[0]);

            while (true) {
                MPSolver.ResultStatus status = solver.solve();

                if (status != MPSolver.ResultStatus.FEASIBLE && status != MPSolver.ResultStatus.OPTIMAL) {
                    break; // Exhausted all configurations or model is infeasible
                }

                count++;

                boolean[] selected = new boolean[boolVars.length];
                int falseCount = 0;

                for (int i = 0; i < boolVars.length; i++) {
                    selected[i] = boolVars[i].solutionValue() > 0.5;
                    if (!selected[i]) {
                        falseCount++;
                    }
                }

                // Sum(Vars_true) - Sum(Vars_false) <= Count(Vars_true) - 1
                MPConstraint exclude =
                        solver.makeConstraint(Double.NEGATIVE_INFINITY, boolVars.length - 1 - falseCount);

                for (int i = 0; i < boolVars.length; i++) {
                    exclude.setCoefficient(boolVars[i], selected[i] ? 1 : -1);
                }
            }
        }

        return count;
    }

    /**
     * Helper method to identify if a variable is boolean.
     */
    public static boolean isBooleanBound(MPVariable v) {
        return v.lb() >= 0.0 && v.ub() <= 1.0;
    }
}
