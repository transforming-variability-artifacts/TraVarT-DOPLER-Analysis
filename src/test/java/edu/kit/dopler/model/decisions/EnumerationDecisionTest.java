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
import edu.kit.dopler.model.basic.Enumeration;
import edu.kit.dopler.model.basic.EnumerationLiteral;
import edu.kit.dopler.model.expressions.BooleanLiteralExpression;
import edu.kit.dopler.model.expressions.Expression;
import edu.kit.dopler.model.values.IValue;
import edu.kit.dopler.model.values.StringValue;
import java.util.HashSet;
import java.util.Set;
import junit.framework.TestCase;

public class EnumerationDecisionTest extends TestCase {

    private EnumerationDecision enumerationDecision;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Set<EnumerationLiteral> enumerationLiterals = new HashSet<>();
        enumerationLiterals.add(new EnumerationLiteral("test1"));
        enumerationLiterals.add((new EnumerationLiteral("test2")));
        Enumeration enumeration = new Enumeration(enumerationLiterals);
        Expression expression = new BooleanLiteralExpression(true);
        enumerationDecision =
                new EnumerationDecision("test", "test", "test", expression, new HashSet<>(), enumeration, 0, 2);
    }

    public void testSetValueWithOutOfRangeValue() {
        assertThrows(ValidityConditionException.class, () -> enumerationDecision.setValue(new StringValue("test3")));
    }

    public void testSetValueWithDisallowedValue() {
        enumerationDecision.addDissallowed(new EnumerationLiteral("test2"));
        assertThrows(ValidityConditionException.class, () -> enumerationDecision.setValue(new StringValue("test2")));
    }

    public void testSetValueCorrect() throws ValidityConditionException {
        assertFalse(enumerationDecision.isTaken());
        assertSame(enumerationDecision.getValue().getValue(), enumerationDecision.getStandardValue());
        IValue<String> testLiteral = new StringValue("test1");
        enumerationDecision.setValue(testLiteral);
        assertSame(enumerationDecision.getValue().getValue(), testLiteral.getValue());
        assertTrue(enumerationDecision.isTaken());
    }

    public void testStandardValue() {
        assertSame(enumerationDecision.getStandardValue(), "null");
    }
}
