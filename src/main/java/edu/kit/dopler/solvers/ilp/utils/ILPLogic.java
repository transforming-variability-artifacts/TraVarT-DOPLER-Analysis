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
import edu.kit.dopler.solvers.ilp.ILPConstants;
import edu.kit.dopler.solvers.ilp.ILPContext;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility for mapping Boolean and Numeric logic into linear constraints.
 */
public final class ILPLogic {

    private static final Logger LOGGER = LoggerFactory.getLogger(ILPLogic.class);

    private ILPLogic() {
        throw new IllegalStateException("Utility class can't be instantiated");
    }

    /**
     * Dynamically calculates a safe Big-M based on the bounds of the provided variables.
     * Big-M is needed for constraints based on boolean variables.
     * @param vars The variables to derive the Big-M from.
     * @return A calculated Big-M double value.
     */
    public static double getBigM(MPVariable... vars) {
        double maxM = 0.0;
        for (MPVariable v : vars) {
            double bound = Math.max(Math.abs(v.lb()), Math.abs(v.ub()));
            if (bound >= MPSolver.infinity()) {
                LOGGER.trace("Variable {} has infinite bounds. Using fallback Big-M.", v.name());
                return ILPConstants.FALLBACK_BIG_M;
            }
            if (bound > maxM) {
                maxM = bound;
            }
        }
        // Multiply by 2.0 to cover differences (e.g. ub - lb) plus safety margin
        return (maxM == 0) ? ILPConstants.FALLBACK_BIG_M : (maxM * 2.0) + 1.0;
    }

    /**
     * Encodes: z = AND(v1, v2, ..., vn)
     * Mathematical Encoding:
     * 1. z <= vi for all i: Forces z to 0 if any vi is 0.
     * 2. z >= sum(vi) - (n - 1): Forces z to 1 if all vi are 1.
     * @param ctx  The ILP Context.
     * @param vars The boolean variables to evaluate.
     * @return A new boolean auxiliary variable 'z' representing the logical AND.
     */
    public static MPVariable and(ILPContext ctx, MPVariable... vars) {
        if (vars.length == 0) {
            throw new IllegalArgumentException("No variables provided.");
        }
        if (vars.length == 1) {
            return vars[0];
        }

        MPVariable z = ctx.createAuxBoolVar("and_n");
        double inf = MPSolver.infinity();

        // z - sum(vi) >= 1 - n  =>  z >= sum(vi) - (n - 1)
        MPConstraint sumConstraint = ctx.solver().makeConstraint(1 - vars.length, inf);
        sumConstraint.setCoefficient(z, 1.0);

        java.util.Map<MPVariable, Integer> counts = new java.util.HashMap<>();
        for (MPVariable v : vars) {
            counts.put(v, counts.getOrDefault(v, 0) + 1);
            // z - vi <= 0  =>  z <= vi
            MPConstraint c = ctx.solver().makeConstraint(-inf, 0);
            c.setCoefficient(z, 1.0);
            c.setCoefficient(v, -1.0);
        }

        // Apply aggregated counts to prevent overwrite
        for (Map.Entry<MPVariable, Integer> entry : counts.entrySet()) {
            sumConstraint.setCoefficient(entry.getKey(), -entry.getValue().doubleValue());
        }
        return z;
    }

    /**
     * Encodes: z = OR(v1, v2, ..., vn)
     * Mathematical Encoding:
     * 1. z >= vi for all i: Forces z to 1 if any vi is 1.
     * 2. z <= sum(vi): Forces z to 0 if all vi are 0.
     * @param ctx  The ILP Context.
     * @param vars The boolean variables to evaluate.
     * @return A new boolean auxiliary variable 'z' representing the logical OR.
     */
    public static MPVariable or(ILPContext ctx, MPVariable... vars) {
        if (vars.length == 0) {
            throw new IllegalArgumentException("No variables provided.");
        }
        if (vars.length == 1) {
            return vars[0];
        }

        MPVariable z = ctx.createAuxBoolVar("or_n");
        double inf = MPSolver.infinity();

        // z - sum(vi) <= 0  =>  z <= sum(vi)
        MPConstraint sumConstraint = ctx.solver().makeConstraint(-inf, 0);
        sumConstraint.setCoefficient(z, 1.0);

        Map<MPVariable, Integer> counts = new HashMap<>();
        for (MPVariable v : vars) {
            counts.put(v, counts.getOrDefault(v, 0) + 1);
            // z - vi >= 0  =>  z >= vi
            MPConstraint c = ctx.solver().makeConstraint(0, inf);
            c.setCoefficient(z, 1.0);
            c.setCoefficient(v, -1.0);
        }

        // Apply strictly aggregated counts to prevent overwrite
        for (Map.Entry<MPVariable, Integer> entry : counts.entrySet()) {
            sumConstraint.setCoefficient(entry.getKey(), -entry.getValue().doubleValue());
        }
        return z;
    }

    /**
     * Encodes: z = x XOR y
     * Mathematical Encoding:
     * 1. z >= x - y
     * 2. z >= y - x
     * 3. z <= x + y
     * 4. z <= 2 - x - y
     * @param ctx The ILP Context.
     * @param x   The first boolean variable.
     * @param y   The second boolean variable.
     * @return A new boolean auxiliary variable 'z' representing x XOR y.
     */
    public static MPVariable xor(ILPContext ctx, MPVariable x, MPVariable y) {
        if (x == y) {
            MPVariable z = ctx.createAuxBoolVar("xor_identity");
            MPConstraint c = ctx.solver().makeConstraint(0, 0);
            c.setCoefficient(z, 1.0);
            return z; // x XOR x is always 0
        }

        MPVariable z = ctx.createAuxBoolVar("xor");
        double inf = MPSolver.infinity();

        // 1: z - x + y >= 0 => z >= x - y
        MPConstraint c1 = ctx.solver().makeConstraint(0, inf);
        c1.setCoefficient(z, 1);
        c1.setCoefficient(x, -1);
        c1.setCoefficient(y, 1);

        // 2: z - y + x >= 0 => z >= y - x
        MPConstraint c2 = ctx.solver().makeConstraint(0, inf);
        c2.setCoefficient(z, 1);
        c2.setCoefficient(y, -1);
        c2.setCoefficient(x, 1);

        // 3: z - x - y <= 0 => z <= x + y
        MPConstraint c3 = ctx.solver().makeConstraint(-inf, 0);
        c3.setCoefficient(z, 1);
        c3.setCoefficient(x, -1);
        c3.setCoefficient(y, -1);

        // 4: z + x + y <= 2 => z <= 2 - (x + y)
        MPConstraint c4 = ctx.solver().makeConstraint(-inf, 2);
        c4.setCoefficient(z, 1);
        c4.setCoefficient(x, 1);
        c4.setCoefficient(y, 1);

        return z;
    }

    /**
     * Encodes: z = (x == y) for boolean variables.
     * Functionally equivalent to NOT (x XOR y).
     * Mathematical Encoding:
     * 1. z <= 1 - x + y
     * 2. z <= 1 + x - y
     * 3. z >= x + y - 1
     * 4. z >= 1 - x - y
     * @param ctx The ILP Context.
     * @param x   The first boolean variable.
     * @param y   The second boolean variable.
     * @return A new boolean auxiliary variable 'z'.
     */
    public static MPVariable equals(ILPContext ctx, MPVariable x, MPVariable y) {
        if (x == y) {
            MPVariable z = ctx.createAuxBoolVar("eq_identity");
            MPConstraint c = ctx.solver().makeConstraint(1, 1);
            c.setCoefficient(z, 1.0);
            return z; // x == x is always 1
        }

        MPVariable z = ctx.createAuxBoolVar("eq_bool");
        double inf = MPSolver.infinity();

        // 1: z <= 1 - x + y
        MPConstraint c1 = ctx.solver().makeConstraint(-inf, 1);
        c1.setCoefficient(z, 1);
        c1.setCoefficient(x, 1);
        c1.setCoefficient(y, -1);

        // 2: z <= 1 + x - y
        MPConstraint c2 = ctx.solver().makeConstraint(-inf, 1);
        c2.setCoefficient(z, 1);
        c2.setCoefficient(x, -1);
        c2.setCoefficient(y, 1);

        // 3: z >= x + y - 1
        MPConstraint c3 = ctx.solver().makeConstraint(-1, inf);
        c3.setCoefficient(z, 1);
        c3.setCoefficient(x, -1);
        c3.setCoefficient(y, -1);

        // 4: z >= 1 - x - y
        MPConstraint c4 = ctx.solver().makeConstraint(1, inf);
        c4.setCoefficient(z, 1);
        c4.setCoefficient(x, 1);
        c4.setCoefficient(y, 1);

        return z;
    }

    /**
     * Encodes: z = NOT x
     * Mathematical Encoding: z + x = 1
     * @param ctx The ILP Context.
     * @param x   The boolean variable to negate.
     * @return A new boolean auxiliary variable 'z' representing NOT x.
     */
    public static MPVariable not(ILPContext ctx, MPVariable x) {
        MPVariable z = ctx.createAuxBoolVar("not");
        MPConstraint c = ctx.solver().makeConstraint(1, 1);
        c.setCoefficient(z, 1);
        c.setCoefficient(x, 1);
        return z;
    }

    /**
     * Encodes logical implication: x => y
     * Mathematical Encoding: y - x >= 0 => y >= x
     * If x is 1, y must be 1. If x is 0, y can be 0 or 1.
     * @param ctx The ILP Context.
     * @param x   The antecedent (if).
     * @param y   The consequent (then).
     */
    public static void implies(ILPContext ctx, MPVariable x, MPVariable y) {
        MPConstraint c = ctx.solver().makeConstraint(0, MPSolver.infinity());
        c.setCoefficient(y, 1);
        c.setCoefficient(x, -1);
    }

    /**
     * Encodes: z = (x == y) for numerical variables using the Big-M method.
     * Mathematical Encoding:
     * When z = 1 (forces x == y):
     * 1. x - y + M*z <= M   => x - y <= 0
     * 2. x - y - M*z >= -M  => x - y >= 0
     * When z = 0 (forces x != y):
     * Requires an auxiliary boolean 'b' to denote direction (x > y or x < y):
     * 3. x - y - M*z - M*b >= EPSILON - M      (if b=1, x < y. if b=0, x > y)
     * 4. x - y + M*z - M*b <= M - EPSILON
     * @param ctx The ILP Context.
     * @param x   The left numerical variable.
     * @param y   The right numerical variable.
     * @param M   The Big-M constant.
     * @return A boolean auxiliary variable 'z' that is 1 if x == y.
     */
    public static MPVariable numericEquals(ILPContext ctx, MPVariable x, MPVariable y, double M) {
        MPVariable z = ctx.createAuxBoolVar("num_eq"); // z = 1 if x == y
        MPVariable b = ctx.createAuxBoolVar("num_eq_aux"); // Direction if x != y

        // 1 & 2: If z = 1, force x == y
        MPConstraint c1 = ctx.solver().makeConstraint(-MPSolver.infinity(), M);
        c1.setCoefficient(x, 1);
        c1.setCoefficient(y, -1);
        c1.setCoefficient(z, M);

        MPConstraint c2 = ctx.solver().makeConstraint(-M, MPSolver.infinity());
        c2.setCoefficient(x, 1);
        c2.setCoefficient(y, -1);
        c2.setCoefficient(z, -M);

        // 3 & 4: If z = 0, force x != y (either x >= y + epsilon OR x <= y - epsilon)
        MPConstraint c3 = ctx.solver().makeConstraint(ILPConstants.EPSILON, MPSolver.infinity());
        c3.setCoefficient(x, 1);
        c3.setCoefficient(y, -1);
        c3.setCoefficient(z, M);
        c3.setCoefficient(b, M);

        MPConstraint c4 = ctx.solver().makeConstraint(-MPSolver.infinity(), M - ILPConstants.EPSILON);
        c4.setCoefficient(x, 1);
        c4.setCoefficient(y, -1);
        c4.setCoefficient(z, M);
        c4.setCoefficient(b, -M);

        return z;
    }
}
