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
package edu.kit.dopler.model.expressions;

import com.google.ortools.sat.BoolVar;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.Literal;
import edu.kit.dopler.common.CpUtils;
import edu.kit.dopler.exceptions.EvaluationException;
import edu.kit.dopler.model.decisions.IDecision;
import edu.kit.dopler.model.decisions.NumberDecision;
import java.util.List;
import java.util.Map;

public final class LessThan extends BinaryExpression {

    private static final String SYMBOL = "<";

    public LessThan(IExpression leftExpression, IExpression rightExpression) {
        super(leftExpression, rightExpression);
    }

    @Override
    public boolean evaluate() throws EvaluationException {

        if (getLeftExpression() instanceof DoubleLiteralExpression
                && getRightExpression() instanceof DecisionValueCallExpression) {
            double left = ((DoubleLiteralExpression) getLeftExpression()).getLiteral();
            double right = (double) ((DecisionValueCallExpression) getRightExpression())
                    .getValue()
                    .getValue();
            return left < right;
        } else if (getLeftExpression() instanceof DecisionValueCallExpression
                && getRightExpression() instanceof DoubleLiteralExpression) {
            double left = (double) ((DecisionValueCallExpression) getLeftExpression())
                    .getValue()
                    .getValue();
            double right = ((DoubleLiteralExpression) getRightExpression()).getLiteral();
            return left < right;
        } else if (getLeftExpression() instanceof DoubleLiteralExpression
                && getRightExpression() instanceof DoubleLiteralExpression) {
            double right = ((DoubleLiteralExpression) getRightExpression()).getLiteral();
            double left = ((DoubleLiteralExpression) getLeftExpression()).getLiteral();
            return left < right;
        } else if (getLeftExpression() instanceof DecisionValueCallExpression
                && getRightExpression() instanceof StringLiteralExpression) {
            String left = (String) ((DecisionValueCallExpression) getLeftExpression())
                    .getValue()
                    .getValue();
            String right = ((StringLiteralExpression) getRightExpression()).getLiteral();
            return left.compareTo(right) < 0;
        } else if (getLeftExpression() instanceof StringLiteralExpression
                && getRightExpression() instanceof StringLiteralExpression) {
            String left = ((StringLiteralExpression) getRightExpression()).getLiteral();
            String right = ((StringLiteralExpression) getLeftExpression()).getLiteral();
            return left.compareTo(right) < 0;
        } else if (getLeftExpression() instanceof StringLiteralExpression
                && getRightExpression() instanceof DecisionValueCallExpression) {
            String left = ((StringLiteralExpression) getLeftExpression()).getLiteral();
            String right = (String) ((DecisionValueCallExpression) getRightExpression())
                    .getValue()
                    .getValue();
            return left.compareTo(right) < 0;
        } else {
            throw new EvaluationException("Only Double and String Values Supported");
        }
    }

    @Override
    public Literal toCpLiteral(
            CpModel model, Map<IDecision<?>, List<IntVar>> decisionVars, Map<IDecision<?>, Literal> isTakenVars) {
        // in our use case (Google OR CP solver) the expressions can only be
        // DoubleLiteralExpression or DecisionValueCallExpression (calling a
        // NumberDecision)
        if (this.getLeftExpression() instanceof DecisionValueCallExpression l
                && l.getDecision() instanceof NumberDecision left
                && this.getRightExpression() instanceof DoubleLiteralExpression right) {
            IntVar leftVar = decisionVars.get(left).getFirst();
            long rightVal = CpUtils.scaleDoubleToLong(right.getLiteral());

            BoolVar equivalentLiteral = model.newBoolVar("equivalentLiteral");

            // ensure that: equivalentLiteral <=> (left < right)
            // "=>": equivalentLiteral => (left < right)
            model.addLessThan(leftVar, rightVal).onlyEnforceIf(equivalentLiteral);

            // "<=": !equivalentLiteral => (left >= right)
            model.addGreaterOrEqual(leftVar, rightVal).onlyEnforceIf(equivalentLiteral.not());

            return equivalentLiteral;
        } else if (this.getLeftExpression() instanceof DoubleLiteralExpression left
                && this.getRightExpression() instanceof DecisionValueCallExpression r
                && r.getDecision() instanceof NumberDecision right) {
            long leftVal = CpUtils.scaleDoubleToLong(left.getLiteral());
            IntVar rightVar = decisionVars.get(right).getFirst();

            BoolVar equivalentLiteral = model.newBoolVar("equivalentLiteral");

            // ensure that: equivalentLiteral <=> (left < right)
            // "=>": equivalentLiteral => (right > left)
            model.addGreaterThan(rightVar, leftVal).onlyEnforceIf(equivalentLiteral); // here we need to use
            // addLessThan() and switch left
            // and right because
            // addGreaterThan can't handle
            // (long,IntVar)

            // "<=": !equivalentLiteral => (right <= left)
            model.addLessOrEqual(rightVar, leftVal).onlyEnforceIf(equivalentLiteral.not());

            return equivalentLiteral;
        } else if (this.getLeftExpression() instanceof DoubleLiteralExpression left
                && this.getRightExpression() instanceof DoubleLiteralExpression right) {
            return left.getLiteral() < right.getLiteral() ? model.trueLiteral() : model.falseLiteral();
        }

        System.err.println("Should not be reachable! (LessThan.toCPLiteral(...))");
        return null; // not reachable
    }

    @Override
    public String toString() {
        return String.format("(%s " + SYMBOL + " %s)", getLeftExpression(), getRightExpression());
    }
}
