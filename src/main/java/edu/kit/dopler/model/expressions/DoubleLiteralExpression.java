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

import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.Literal;
import edu.kit.dopler.exceptions.InvalidTypeInLiteralExpressionCheckException;
import edu.kit.dopler.model.decisions.IDecision;
import edu.kit.dopler.model.values.DoubleValue;
import edu.kit.dopler.model.values.IValue;
import java.util.List;
import java.util.Map;

public final class DoubleLiteralExpression extends LiteralExpression {

    private double literal;

    public DoubleLiteralExpression(double literal) {
        this.literal = literal;
    }

    @Override
    public boolean evaluate() {
        return literal >= 1.0;
    }

    @Override
    public Literal toCpLiteral(
            CpModel model, Map<IDecision<?>, List<IntVar>> decisionVars, Map<IDecision<?>, Literal> isTakenVars) {
        throw new UnsupportedOperationException(
                "Not supported. In the CP solver a Literal can only be a boolean variable => use getLiteral and the Methods from CpUtils for DoubleLiteralExpression.");
    }

    public double getLiteral() {
        return literal;
    }

    public void setLiteral(double literal) {
        this.literal = literal;
    }

    /**
     * This methode is implemented for every LiteralExpression to check the equality
     * in the EQUALS expression
     *
     * @param value
     *            the value which need to be compared to the literal
     * @return returns a boolean if the values are equal
     * @throws InvalidTypeInLiteralExpressionCheckException
     *             is thrown when the value is not of type DoubleValue
     */
    @Override
    boolean equalsForLiteralExpressions(IValue<?> value) throws InvalidTypeInLiteralExpressionCheckException {
        if (value instanceof DoubleValue) {
            return literal == ((DoubleValue) value).getValue();
        } else {
            throw new InvalidTypeInLiteralExpressionCheckException("Parameter was not of Type StringValue in Equals");
        }
    }

    @Override
    public String toString() {
        return String.valueOf(literal);
    }
}
