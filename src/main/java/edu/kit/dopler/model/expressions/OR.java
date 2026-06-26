/*******************************************************************************
 * SPDX-License-Identifier: MPL-2.0
 *
 * Copyright (c) 2024 Karlsruhe Institute of Technology (KIT)
 * KASTEL - Dependability of Software-intensive Systems
 *
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *******************************************************************************/
package edu.kit.dopler.model.expressions;

import com.google.ortools.sat.BoolVar;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.Literal;
import edu.kit.dopler.exceptions.EvaluationException;
import edu.kit.dopler.model.decisions.IDecision;
import java.util.List;
import java.util.Map;

public final class OR extends BinaryExpression {

    private static final String SYMBOL = "||";

    public OR(final IExpression leftExpression, final IExpression rightExpression) {
        super(leftExpression, rightExpression);
    }

    @Override
    public boolean evaluate() throws EvaluationException {
        if (getLeftExpression() instanceof final BooleanLiteralExpression leftExpression
                && getRightExpression() instanceof final DecisionValueCallExpression rightExpression) {
            final boolean left = leftExpression.getLiteral();
            final boolean right = (boolean) rightExpression.getValue().getValue();
            return left || right;
        }
        if (getLeftExpression() instanceof final DecisionValueCallExpression leftExpression
                && getRightExpression() instanceof final BooleanLiteralExpression rightExpression) {
            final boolean left = (boolean) leftExpression.getValue().getValue();
            final boolean right = rightExpression.getLiteral();
            return left || right;
        } else if (getLeftExpression() instanceof final BooleanLiteralExpression leftExpression
                && getRightExpression() instanceof final BooleanLiteralExpression rightExpression) {
            final boolean left = leftExpression.getLiteral();
            final boolean right = rightExpression.getLiteral();
            return left || right;
        } else if (getLeftExpression() instanceof BinaryExpression
                && getRightExpression() instanceof BinaryExpression) {
            return getLeftExpression().evaluate() && getRightExpression().evaluate();
        } else {
            throw new EvaluationException("Only Boolean Values Supported");
        }
    }

    @Override
    public Literal toCpLiteral(
            CpModel model, Map<IDecision<?>, List<IntVar>> decisionVars, Map<IDecision<?>, Literal> isTakenVars) {
        Literal leftLiteral = this.getLeftExpression().toCpLiteral(model, decisionVars, isTakenVars);
        Literal rightLiteral = this.getRightExpression().toCpLiteral(model, decisionVars, isTakenVars);

        BoolVar equivalentLiteral = model.newBoolVar("equivalentLiteral");

        // ensure that: equivalentLiteral <=> (leftLiteral or rightLiteral)
        // "=>" as CNF
        model.addBoolOr(new Literal[] {equivalentLiteral.not(), leftLiteral, rightLiteral});

        // "<=" as CNF
        model.addBoolOr(new Literal[] {leftLiteral.not(), equivalentLiteral});
        model.addBoolOr(new Literal[] {rightLiteral.not(), equivalentLiteral});

        return equivalentLiteral;
    }

    @Override
    public String toString() {
        return String.format("(%s " + SYMBOL + " %s)", getLeftExpression(), getRightExpression());
    }
}
