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
package edu.kit.dopler.model.decisions;

import static org.junit.Assert.assertThrows;

import edu.kit.dopler.exceptions.ValidityConditionException;
import edu.kit.dopler.model.expressions.*;
import edu.kit.dopler.model.values.IValue;
import edu.kit.dopler.model.values.StringValue;
import java.util.HashSet;
import java.util.Set;
import junit.framework.TestCase;

public class StringDecisionTest extends TestCase {

    private StringDecision stringDecision;

    public void setUp() throws Exception {
        super.setUp();

        Expression expression = new BooleanLiteralExpression(true);
        stringDecision = new StringDecision("test", "test", "test", expression, new HashSet<>(), new HashSet<>());
    }

    public void testSetValueWhichDontFullFillsValidityCondition() {
        StringLiteralExpression stringLiteralExpression = new StringLiteralExpression("test");
        DecisionValueCallExpression decisionValueCallExpression = new DecisionValueCallExpression(stringDecision);
        Equals equals = new Equals(decisionValueCallExpression, stringLiteralExpression);
        Set<IExpression> expressions = new HashSet<>();
        expressions.add(equals);
        stringDecision.setValidityConditions(expressions);
        assertThrows(ValidityConditionException.class, () -> stringDecision.setValue(new StringValue("test2")));
        assertEquals(new StringValue("").getValue(), stringDecision.getValue().getValue());
        assertFalse(stringDecision.isTaken());
    }

    public void testSetValueWhichFullFillsValidityCondition() throws ValidityConditionException {
        StringLiteralExpression stringLiteralExpression = new StringLiteralExpression("test");
        DecisionValueCallExpression decisionValueCallExpression = new DecisionValueCallExpression(stringDecision);
        Equals equals = new Equals(decisionValueCallExpression, stringLiteralExpression);
        Set<IExpression> expressions = new HashSet<>();
        expressions.add(equals);
        stringDecision.setValidityConditions(expressions);
        IValue<String> stringIValue = new StringValue("test");
        stringDecision.setValue(stringIValue);
        assertEquals(stringIValue.getValue(), stringDecision.getValue().getValue());
        assertTrue(stringDecision.isTaken());
    }

    public void testStandardValue() {
        assertEquals("", stringDecision.getStandardValue());
    }
}
