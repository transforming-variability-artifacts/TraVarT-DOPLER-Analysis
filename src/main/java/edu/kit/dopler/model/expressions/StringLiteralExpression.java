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

import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.Literal;
import edu.kit.dopler.exceptions.InvalidTypeInLiteralExpressionCheckException;
import edu.kit.dopler.model.decisions.IDecision;
import edu.kit.dopler.model.values.IValue;
import edu.kit.dopler.model.values.StringValue;
import java.util.List;
import java.util.Map;

public final class StringLiteralExpression extends LiteralExpression {

    private String literal;

    public StringLiteralExpression(String literal) {
        this.literal = literal;
    }

    @Override
    public boolean evaluate() {
        return false;
    }

    @Override
    public Literal toCpLiteral(
            CpModel model, Map<IDecision<?>, List<IntVar>> decisionVars, Map<IDecision<?>, Literal> isTakenVars) {
        throw new UnsupportedOperationException("Not supported in the current CP-approach.");
    }

    public String getLiteral() {
        return literal;
    }

    public void setLiteral(String literal) {
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
     *             is thrown when the value is not of type StringValue
     */
    @Override
    boolean equalsForLiteralExpressions(IValue<?> value) throws InvalidTypeInLiteralExpressionCheckException {
        if (value instanceof StringValue) {
            return literal.equals(value.getValue());
        } else {
            throw new InvalidTypeInLiteralExpressionCheckException("Parameter was not of Type StringValue in Equals");
        }
    }

    @Override
    public String toString() {
        return String.format("%s", literal);
    }
}
