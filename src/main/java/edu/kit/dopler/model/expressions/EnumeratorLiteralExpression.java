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
import edu.kit.dopler.model.basic.EnumerationLiteral;
import edu.kit.dopler.model.decisions.IDecision;
import edu.kit.dopler.model.values.IValue;
import java.util.List;
import java.util.Map;

public final class EnumeratorLiteralExpression extends LiteralExpression {

    private final EnumerationLiteral enumerationLiteral;

    public EnumeratorLiteralExpression(EnumerationLiteral literal) {
        this.enumerationLiteral = literal;
    }

    @Override
    public boolean evaluate() {
        return false;
    }

    @Override
    public Literal toCpLiteral(
            CpModel model, Map<IDecision<?>, List<IntVar>> decisionVars, Map<IDecision<?>, Literal> isTakenVars) {
        throw new UnsupportedOperationException("Not supported.");
    }

    /**
     * This methode is implemented for every LiteralExpression to check the equality
     * in the EQUALS expression
     *
     * @param value
     *            the value which need to be compared to the literal
     * @return returns a boolean if the values are equal
     * @throws InvalidTypeInLiteralExpressionCheckException
     *             is thrown when the value is not of type EnumerationLiteral
     */
    @Override
    boolean equalsForLiteralExpressions(IValue<?> value) throws InvalidTypeInLiteralExpressionCheckException {
        throw new UnsupportedOperationException("Not yet supported.");
        // TODO: This check doesn't make sense, IValue can never be EnumerationLiteral
        /*        if (value instanceof EnumerationLiteral) {
            return Objects.equals(enumerationLiteral.getValue(), ((EnumerationLiteral) value).getValue());
        } else {
            throw new InvalidTypeInLiteralExpressionCheckException("Parameter was not of Type StringValue in Equals");
        }*/
    }

    public EnumerationLiteral getEnumerationLiteral() {
        return enumerationLiteral;
    }

    @Override
    public String toString() {
        return enumerationLiteral.getValue();
    }
}
