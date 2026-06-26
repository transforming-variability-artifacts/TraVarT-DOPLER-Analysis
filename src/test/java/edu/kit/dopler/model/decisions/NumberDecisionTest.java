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
package edu.kit.dopler.model.decisions;

import static org.junit.Assert.assertThrows;

import edu.kit.dopler.exceptions.EvaluationException;
import edu.kit.dopler.exceptions.ValidityConditionException;
import edu.kit.dopler.model.expressions.*;
import edu.kit.dopler.model.values.DoubleValue;
import java.util.HashSet;
import java.util.Set;
import junit.framework.TestCase;

public class NumberDecisionTest extends TestCase {

    private NumberDecision numberDecision;

    public void setUp() throws Exception {
        super.setUp();

        Expression expression = new BooleanLiteralExpression(true);
        numberDecision = new NumberDecision("test", "test", "test", expression, new HashSet<>(), new HashSet<>());
    }

    public void testSetValueWhichDontFullFillsValidityCondition() {
        DoubleLiteralExpression doubleLiteralExpression = new DoubleLiteralExpression(2.0);
        DecisionValueCallExpression decisionValueCallExpression = new DecisionValueCallExpression(numberDecision);
        GreaterThan greaterThan = new GreaterThan(decisionValueCallExpression, doubleLiteralExpression);
        Set<IExpression> expressions = new HashSet<>();
        expressions.add(greaterThan);
        numberDecision.setValidityConditions(expressions);
        assertThrows(ValidityConditionException.class, () -> numberDecision.setValue(new DoubleValue(1.0)));
        assertEquals(new DoubleValue(0.0).getValue(), numberDecision.getValue().getValue());
    }

    public void testSetValueWhichFullFillsValidityCondition() throws ValidityConditionException, EvaluationException {
        DoubleLiteralExpression doubleLiteralExpression = new DoubleLiteralExpression(2.0);
        DecisionValueCallExpression decisionValueCallExpression = new DecisionValueCallExpression(numberDecision);
        LessThan lessThan = new LessThan(decisionValueCallExpression, doubleLiteralExpression);
        Set<IExpression> expressions = new HashSet<>();
        expressions.add(lessThan);
        numberDecision.setValidityConditions(expressions);
        DoubleValue doubleValue = new DoubleValue(1.0);
        numberDecision.setValue(doubleValue);
        assertEquals(doubleValue.getValue(), numberDecision.getValue().getValue());
        assertTrue(numberDecision.isTaken());
        assertTrue(numberDecision.checkValidity());
    }

    public void testStandardValue() {
        assertEquals(0.0, numberDecision.getStandardValue());
    }
}
