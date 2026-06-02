/*******************************************************************************
 * SPDX-License-Identifier: MPL-2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/.
 *
 * Copyright 2024 Karlsruhe Institute of Technology (KIT)
 * KASTEL - Dependability of Software-intensive Systems
 *******************************************************************************/
package edu.kit.dopler.model.expressions;

import static edu.kit.dopler.common.CpUtils.getEnumDecisionLiteralVariableName;

import com.google.ortools.sat.BoolVar;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.Literal;
import edu.kit.dopler.common.CpUtils;
import edu.kit.dopler.exceptions.EvaluationException;
import edu.kit.dopler.exceptions.InvalidTypeInLiteralExpressionCheckException;
import edu.kit.dopler.model.decisions.EnumerationDecision;
import edu.kit.dopler.model.decisions.IDecision;
import edu.kit.dopler.model.decisions.NumberDecision;
import edu.kit.dopler.model.values.IValue;
import java.util.List;
import java.util.Map;

public final class Equals extends BinaryExpression {

    private static final String SYMBOL = "=";

    public Equals(IExpression leftExpression, IExpression rightExpression) {
        super(leftExpression, rightExpression);
    }

    @Override
    public boolean evaluate() throws EvaluationException {

        try {
            if (getLeftExpression() instanceof LiteralExpression
                    && getRightExpression() instanceof DecisionValueCallExpression) {
                IValue<?> right = ((DecisionValueCallExpression) getRightExpression()).getValue();
                return ((LiteralExpression) getLeftExpression()).equalsForLiteralExpressions(right);
            }
            if (getLeftExpression() instanceof DecisionValueCallExpression
                    && getRightExpression() instanceof LiteralExpression) {
                IValue<?> left = ((DecisionValueCallExpression) getLeftExpression()).getValue();
                return ((LiteralExpression) getRightExpression()).equalsForLiteralExpressions(left);
            }

            return getLeftExpression() == getRightExpression();
        } catch (InvalidTypeInLiteralExpressionCheckException e) {
            throw new EvaluationException(e);
        }
    }

    @Override
    public Literal toCpLiteral(
            CpModel model, Map<IDecision<?>, List<IntVar>> decisionVars, Map<IDecision<?>, Literal> isTakenVars) {
        if (this.getLeftExpression() instanceof DecisionValueCallExpression l
                && l.getDecision() instanceof NumberDecision left
                && this.getRightExpression() instanceof DoubleLiteralExpression right) {
            IntVar leftVar = decisionVars.get(left).getFirst();
            long rightVal = CpUtils.scaleDoubleToLong(right.getLiteral());

            BoolVar equivalentLiteral = model.newBoolVar("equivalentLiteral");

            // ensure that: equivalentLiteral <=> (left == right)
            // "=>": equivalentLiteral => (left == right)
            model.addEquality(leftVar, rightVal).onlyEnforceIf(equivalentLiteral);

            // "<=": !equivalentLiteral => (left != right)
            model.addDifferent(leftVar, rightVal).onlyEnforceIf(equivalentLiteral.not());

            return equivalentLiteral;
        } else if (this.getLeftExpression() instanceof DoubleLiteralExpression left
                && this.getRightExpression() instanceof DecisionValueCallExpression r
                && r.getDecision() instanceof NumberDecision right) {
            long leftVal = CpUtils.scaleDoubleToLong(left.getLiteral());
            IntVar rightVar = decisionVars.get(right).getFirst();

            BoolVar equivalentLiteral = model.newBoolVar("equivalentLiteral");

            // ensure that: equivalentLiteral <=> (left == right)
            // "=>": equivalentLiteral => (right == left)
            model.addEquality(rightVar, leftVal).onlyEnforceIf(equivalentLiteral); // we need to switch left and right
            // here because of the method
            // signature

            // "<=": !equivalentLiteral => (right != left)
            model.addDifferent(rightVar, leftVal).onlyEnforceIf(equivalentLiteral.not());

            return equivalentLiteral;
        } else if (this.getLeftExpression() instanceof DoubleLiteralExpression left
                && this.getRightExpression() instanceof DoubleLiteralExpression right) {
            return (long) CpUtils.scaleDoubleToLong(left.getLiteral()) == CpUtils.scaleDoubleToLong(right.getLiteral())
                    ? model.trueLiteral()
                    : model.falseLiteral();

        } else if (this.getLeftExpression() instanceof DecisionValueCallExpression l
                && l.getDecision() instanceof EnumerationDecision enumerationDecision
                && this.getRightExpression() instanceof EnumeratorLiteralExpression enumeratorLiteralExpression) {
            // for enums:
            String enumerationVarName = getEnumDecisionLiteralVariableName(
                    enumerationDecision,
                    enumeratorLiteralExpression.getEnumerationLiteral().getValue());

            for (IntVar enumerationVar : decisionVars.get(enumerationDecision)) {
                if (enumerationVar.getName().equals(enumerationVarName)) {
                    BoolVar equivalentLiteral = model.newBoolVar("equivalentLiteral");

                    // ensure that: equivalentLiteral <=> (enumerationVar == true)
                    // "=>" as CNF
                    model.addEquality(enumerationVar, model.trueLiteral()).onlyEnforceIf(equivalentLiteral);

                    // "<=" as CNF
                    model.addEquality(enumerationVar, model.falseLiteral()).onlyEnforceIf(equivalentLiteral.not());

                    return equivalentLiteral;
                }
            }
            return null; // not reachable
        } else {
            // "normally" (with booleans)
            Literal leftLiteral = this.getLeftExpression().toCpLiteral(model, decisionVars, isTakenVars);
            Literal rightLiteral = this.getRightExpression().toCpLiteral(model, decisionVars, isTakenVars);

            BoolVar equivalentLiteral = model.newBoolVar("equivalentLiteral");

            // ensure that: equivalentLiteral <=> (leftLiteral == rightLiteral)
            // "=>" as CNF
            model.addBoolOr(new Literal[] {equivalentLiteral.not(), leftLiteral, rightLiteral.not()});
            model.addBoolOr(new Literal[] {equivalentLiteral.not(), leftLiteral.not(), rightLiteral});

            // "<=" as CNF
            model.addBoolOr(new Literal[] {equivalentLiteral, leftLiteral, rightLiteral});
            model.addBoolOr(new Literal[] {equivalentLiteral, leftLiteral.not(), rightLiteral.not()});

            return equivalentLiteral;
        }
    }

    @Override
    public String toString() {
        return String.format("(%s " + SYMBOL + " %s)", getLeftExpression(), getRightExpression());
    }
}
