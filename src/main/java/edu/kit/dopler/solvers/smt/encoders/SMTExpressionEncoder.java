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
package edu.kit.dopler.solvers.smt.encoders;

import edu.kit.dopler.model.expressions.*;
import edu.kit.dopler.solvers.shared.SolverUtils;
import edu.kit.dopler.solvers.smt.SMTConstants;
import edu.kit.dopler.solvers.smt.SMTContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sosy_lab.java_smt.api.*;

public final class SMTExpressionEncoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(SMTExpressionEncoder.class);

    private SMTExpressionEncoder() {
        throw new IllegalStateException("Utility class can't be instantiated");
    }

    /**
     * Recursively encodes an AST of IExpression objects into an SMT Formula.
     *
     * @param expression The root of the expression tree to evaluate.
     * @param context    The active SMT context.
     * @return The corresponding {@link Formula}.
     */
    public static Formula encodeExpression(IExpression expression, SMTContext context) {
        FormulaManager fm = context.fm();
        BooleanFormulaManager bfm = fm.getBooleanFormulaManager();

        return switch (expression) {
            case AND and ->
                bfm.and((BooleanFormula) encodeExpression(and.getLeftExpression(), context), (BooleanFormula)
                        encodeExpression(and.getRightExpression(), context));
            case OR or ->
                bfm.or((BooleanFormula) encodeExpression(or.getLeftExpression(), context), (BooleanFormula)
                        encodeExpression(or.getRightExpression(), context));
            case XOR xor ->
                bfm.xor((BooleanFormula) encodeExpression(xor.getLeftExpression(), context), (BooleanFormula)
                        encodeExpression(xor.getRightExpression(), context));
            case NOT not -> bfm.not((BooleanFormula) encodeExpression(not.getOperand(), context));

            case Equals equals -> encodeEquals(equals, context);

            case GreaterThan gt ->
                fm.getRationalFormulaManager()
                        .greaterThan(
                                (NumeralFormula.RationalFormula) encodeExpression(gt.getLeftExpression(), context),
                                (NumeralFormula.RationalFormula) encodeExpression(gt.getRightExpression(), context));
            case LessThan lt ->
                fm.getRationalFormulaManager()
                        .lessThan(
                                (NumeralFormula.RationalFormula) encodeExpression(lt.getLeftExpression(), context),
                                (NumeralFormula.RationalFormula) encodeExpression(lt.getRightExpression(), context));

            case BooleanLiteralExpression boolLit -> bfm.makeBoolean(boolLit.getLiteral());
            case DoubleLiteralExpression dblLit ->
                fm.getRationalFormulaManager().makeNumber(String.valueOf(dblLit.getLiteral()));
            case StringLiteralExpression strLit -> fm.getStringFormulaManager().makeString(strLit.getLiteral());
            case EnumeratorLiteralExpression ignored ->
                throw new UnsupportedOperationException("Should be handled in equals.");

            case DecisionValueCallExpression valCall ->
                context.getVar(SolverUtils.toStringConst(valCall.getDecision()) + SMTConstants.VALUE_SUFFIX);

            // Delegate to isTaken, which already encodes visibility + enforcement,
            case DecisionVisibilityCallExpression visCall ->
                context.getVar(SolverUtils.toStringConst(visCall.getDecision()) + SMTConstants.TAKEN_SUFFIX);

            case IsTaken isTaken ->
                context.getVar(SolverUtils.toStringConst(isTaken.getDecision()) + SMTConstants.TAKEN_SUFFIX);

            case JavaExpression ignored -> {
                LOGGER.warn(
                        "JavaExpression encountered. This is currently not supported in SMT encoding. Defaulting to True.");
                yield bfm.makeTrue();
            }
        };
    }

    private static Formula encodeEquals(Equals equals, SMTContext context) {
        FormulaManager fm = context.fm();
        BooleanFormulaManager bfm = fm.getBooleanFormulaManager();
        IExpression left = equals.getLeftExpression();
        IExpression right = equals.getRightExpression();

        // Handle Enum Literal to Decision Value Equality
        if (right instanceof EnumeratorLiteralExpression rightExpression
                && left instanceof DecisionValueCallExpression leftCall) {
            String literalVal = rightExpression.getEnumerationLiteral().getValue();
            return context.getVar(SolverUtils.toStringConst(leftCall.getDecision()) + "_" + literalVal);
        }
        if (left instanceof EnumeratorLiteralExpression leftExpression
                && right instanceof DecisionValueCallExpression rightCall) {
            String literalVal = leftExpression.getEnumerationLiteral().getValue();
            return context.getVar(SolverUtils.toStringConst(rightCall.getDecision()) + "_" + literalVal);
        }

        Formula leftExpr = encodeExpression(left, context);
        Formula rightExpr = encodeExpression(right, context);

        if (leftExpr == null || rightExpr == null) {
            throw new IllegalStateException(
                    String.format("Equals operands evaluated to null. Left: %s, Right: %s.", left, right));
        }

        if (leftExpr instanceof BooleanFormula l && rightExpr instanceof BooleanFormula r) {
            return bfm.equivalence(l, r);
        } else if (leftExpr instanceof NumeralFormula.RationalFormula l
                && rightExpr instanceof NumeralFormula.RationalFormula r) {
            return fm.getRationalFormulaManager().equal(l, r);
        } else if (leftExpr instanceof StringFormula l && rightExpr instanceof StringFormula r) {
            return fm.getStringFormulaManager().equal(l, r);
        }

        throw new IllegalStateException("Equals operands have incompatible SMT types: "
                + leftExpr.getClass().getSimpleName() + " vs "
                + rightExpr.getClass().getSimpleName());
    }

    /**
     * Utility method to cast plain Java Objects into their matching SMT Formula types.
     *
     * @param context The active SMT context.
     * @param value   The raw object value.
     * @param sort    The expected FormulaType sort.
     * @return The correctly typed {@link Formula}.
     */
    public static Formula convertToSMTExpr(SMTContext context, Object value, FormulaType<?> sort) {
        FormulaManager fm = context.fm();
        if (value == null) {
            return null;
        }

        if (sort.isBooleanType()) {
            return fm.getBooleanFormulaManager().makeBoolean(Boolean.parseBoolean(String.valueOf(value)));
        } else if (sort.isRationalType()) {
            try {
                return fm.getRationalFormulaManager().makeNumber(String.valueOf(value));
            } catch (NumberFormatException e) {
                LOGGER.error("Failed to convert object {} to RationalFormula.", value, e);
                throw e;
            }
        }
        return fm.getStringFormulaManager().makeString(String.valueOf(value));
    }
}
