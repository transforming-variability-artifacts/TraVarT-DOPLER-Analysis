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

import static org.junit.Assert.*;

import edu.kit.dopler.exceptions.EvaluationException;
import edu.kit.dopler.model.actions.BooleanEnforce;
import edu.kit.dopler.model.actions.IAction;
import edu.kit.dopler.model.basic.Rule;
import edu.kit.dopler.model.expressions.BooleanLiteralExpression;
import edu.kit.dopler.model.expressions.IExpression;
import edu.kit.dopler.model.values.BooleanValue;
import edu.kit.dopler.model.values.IValue;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

public class BooleanDecisionTest {

    @Test
    public void setValueTest() {
        IValue<Boolean> value = BooleanValue.getTrue();
        IExpression expression = new BooleanLiteralExpression(true);
        BooleanDecision decision = new BooleanDecision("test", "test", "test", expression, Collections.emptySet());
        decision.setValue(value);
        assertTrue(decision.isTaken());
        assertSame(decision.getValue().getValue(), value.getValue());
    }

    @Test
    public void testDecisionType() {

        IExpression expression = new BooleanLiteralExpression(true);
        BooleanDecision decision = new BooleanDecision("test", "test", "test", expression, Collections.emptySet());
        assertSame(decision.getDecisionType(), Decision.DecisionType.BOOLEAN);
    }

    @Test
    public void testStandardValue() {
        IExpression expression = new BooleanLiteralExpression(true);
        BooleanDecision decision = new BooleanDecision("test", "test", "test", expression, Collections.emptySet());
        assertSame(decision.getStandardValue(), BooleanValue.getFalse().getValue());
    }

    @Test
    public void testSetTaken() {
        IExpression expression = new BooleanLiteralExpression(true);
        BooleanDecision decision = new BooleanDecision("test", "test", "test", expression, Collections.emptySet());
        decision.setTaken(true);

        assertTrue(decision.isTaken());
    }

    @Test
    public void checkVisibilityWithTrueVisibilityCond() throws EvaluationException {
        IExpression expression = new BooleanLiteralExpression(true);
        BooleanDecision decision = new BooleanDecision("test", "test", "test", expression, Collections.emptySet());
        assertTrue(decision.isVisible());
    }

    @Test
    public void checkVisibilityWithFalseVisibilityCond() throws EvaluationException {
        IExpression expression = new BooleanLiteralExpression(false);
        BooleanDecision decision = new BooleanDecision("test", "test", "test", expression, Collections.emptySet());
        assertFalse(decision.isVisible());
    }

    @Test
    public void BooleanEnforceActionTest() {
        IValue<Boolean> value = BooleanValue.getTrue();
        IExpression expression = new BooleanLiteralExpression(true);
        BooleanDecision decision = new BooleanDecision("test", "test", "test", expression, new HashSet<>());
        BooleanDecision decision2 = new BooleanDecision("test", "test", "test", expression, new HashSet<>());

        assertFalse(decision2.isTaken());
        assertFalse(decision2.getValue().getValue());

        Rule rule = new Rule(expression, Collections.emptySet());
        Set<IAction> set = new HashSet<>();
        IAction action = new BooleanEnforce(decision2, value);
        set.add(action);
        rule.setActions(set);
        decision.addRule(rule);

        try {
            decision.executeRules();

        } catch (Exception e) {
            System.out.println(e);
        }

        assertTrue(decision2.isTaken());
        assertTrue(decision2.getValue().getValue());
    }
}
