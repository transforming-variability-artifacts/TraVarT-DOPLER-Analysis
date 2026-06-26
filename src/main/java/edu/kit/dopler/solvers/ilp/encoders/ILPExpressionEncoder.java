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
package edu.kit.dopler.solvers.ilp.encoders;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import edu.kit.dopler.model.expressions.*;
import edu.kit.dopler.solvers.ilp.ILPConstants;
import edu.kit.dopler.solvers.ilp.ILPContext;
import edu.kit.dopler.solvers.ilp.utils.ILPLogic;
import edu.kit.dopler.solvers.shared.SolverUtils;

public final class ILPExpressionEncoder {
    private ILPExpressionEncoder() {
        throw new IllegalStateException("Utility class can't be instantiated");
    }

    public static MPVariable encodeExpression(IExpression expression, ILPContext ctx) {
        return switch (expression) {
            case AND and ->
                ILPLogic.and(
                        ctx,
                        encodeExpression(and.getLeftExpression(), ctx),
                        encodeExpression(and.getRightExpression(), ctx));
            case OR or ->
                ILPLogic.or(
                        ctx,
                        encodeExpression(or.getLeftExpression(), ctx),
                        encodeExpression(or.getRightExpression(), ctx));
            case XOR xor ->
                ILPLogic.xor(
                        ctx,
                        encodeExpression(xor.getLeftExpression(), ctx),
                        encodeExpression(xor.getRightExpression(), ctx));
            case NOT not -> ILPLogic.not(ctx, encodeExpression(not.getOperand(), ctx));

            case Equals equals -> encodeEquals(equals, ctx);
            case GreaterThan gt -> encodeGreaterThan(gt, ctx);
            case LessThan lt -> encodeLessThan(lt, ctx);

            case BooleanLiteralExpression boolLit -> ctx.createConstant(boolLit.getLiteral() ? 1.0 : 0.0);
            case DoubleLiteralExpression dblLit -> ctx.createConstant(dblLit.getLiteral());
            case StringLiteralExpression ignored ->
                throw new UnsupportedOperationException("String are not supported in ILP.");
            case EnumeratorLiteralExpression ignored ->
                throw new UnsupportedOperationException("Should be handled in equals.");

            case DecisionValueCallExpression valCall ->
                ctx.getCoreVar(SolverUtils.toStringConst(valCall.getDecision()) + ILPConstants.VALUE_SUFFIX);
            case DecisionVisibilityCallExpression visCall ->
                ctx.getCoreVar(SolverUtils.toStringConst(visCall.getDecision()) + ILPConstants.TAKEN_SUFFIX);
            case IsTaken isTaken ->
                ctx.getCoreVar(SolverUtils.toStringConst(isTaken.getDecision()) + ILPConstants.TAKEN_SUFFIX);
            case JavaExpression ignored -> ctx.createConstant(1.0);
        };
    }

    private static MPVariable encodeEquals(Equals equals, ILPContext ctx) {
        IExpression left = equals.getLeftExpression();
        IExpression right = equals.getRightExpression();

        // Handle Enum Decision vs Enum Literal
        if (left instanceof EnumeratorLiteralExpression lit && right instanceof DecisionValueCallExpression dec) {
            return ctx.getCoreVar(SolverUtils.toStringConst(dec.getDecision()) + "_"
                    + lit.getEnumerationLiteral().getValue());
        } else if (right instanceof EnumeratorLiteralExpression lit
                && left instanceof DecisionValueCallExpression dec) {
            return ctx.getCoreVar(SolverUtils.toStringConst(dec.getDecision()) + "_"
                    + lit.getEnumerationLiteral().getValue());
        }

        // Numeric/Boolean Equality
        MPVariable leftVar = encodeExpression(left, ctx);
        MPVariable rightVar = encodeExpression(right, ctx);

        if (isBooleanBound(leftVar) && isBooleanBound(rightVar)) {
            return ILPLogic.equals(ctx, leftVar, rightVar);
        }

        return ILPLogic.numericEquals(ctx, leftVar, rightVar, ILPLogic.getBigM(leftVar, rightVar));
    }

    private static MPVariable encodeGreaterThan(GreaterThan gt, ILPContext ctx) {
        MPVariable left = encodeExpression(gt.getLeftExpression(), ctx);
        MPVariable right = encodeExpression(gt.getRightExpression(), ctx);
        MPVariable z = ctx.createAuxBoolVar("gt_eval");
        double M = ILPLogic.getBigM(left, right);

        // z = 1 <=> left > right
        // left - right >= EPSILON - M(1 - z) => left - right - M*z >= EPSILON - M
        MPConstraint c1 = ctx.solver().makeConstraint(ILPConstants.EPSILON - M, MPSolver.infinity());
        c1.setCoefficient(left, 1.0);
        c1.setCoefficient(right, -1.0);
        c1.setCoefficient(z, -M);

        // left - right <= M * z => left - right - M*z <= 0
        MPConstraint c2 = ctx.solver().makeConstraint(-MPSolver.infinity(), 0.0);
        c2.setCoefficient(left, 1.0);
        c2.setCoefficient(right, -1.0);
        c2.setCoefficient(z, -M);

        return z;
    }

    private static MPVariable encodeLessThan(LessThan lt, ILPContext ctx) {
        MPVariable left = encodeExpression(lt.getLeftExpression(), ctx);
        MPVariable right = encodeExpression(lt.getRightExpression(), ctx);
        MPVariable z = ctx.createAuxBoolVar("lt_eval");
        double M = ILPLogic.getBigM(left, right);

        // z = 1 <=> left < right (which is right > left)
        MPConstraint c1 = ctx.solver().makeConstraint(ILPConstants.EPSILON - M, MPSolver.infinity());
        c1.setCoefficient(right, 1.0);
        c1.setCoefficient(left, -1.0);
        c1.setCoefficient(z, -M);

        MPConstraint c2 = ctx.solver().makeConstraint(-MPSolver.infinity(), 0.0);
        c2.setCoefficient(right, 1.0);
        c2.setCoefficient(left, -1.0);
        c2.setCoefficient(z, -M);

        return z;
    }

    /**
     * Helper method to identify if a variable is boolean.
     */
    private static boolean isBooleanBound(MPVariable v) {
        // No strict check because a boolean can also be bound to either 0 or 1
        return v.lb() >= 0.0 && v.ub() <= 1.0;
    }
}
