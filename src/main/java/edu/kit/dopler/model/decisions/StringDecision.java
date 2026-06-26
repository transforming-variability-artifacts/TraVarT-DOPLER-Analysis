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

import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.Literal;
import edu.kit.dopler.exceptions.EvaluationException;
import edu.kit.dopler.exceptions.ValidityConditionException;
import edu.kit.dopler.model.basic.Rule;
import edu.kit.dopler.model.expressions.IExpression;
import edu.kit.dopler.model.values.AbstractValue;
import edu.kit.dopler.model.values.IValue;
import edu.kit.dopler.model.values.StringValue;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class StringDecision extends ValueDecision<String> {

    private final AbstractValue<String> value;
    private final String standardValue = "";

    public StringDecision(
            String displayId,
            String question,
            String description,
            IExpression visibilityCondition,
            Set<Rule> rules,
            Set<IExpression> validityConditions) {
        super(displayId, question, description, visibilityCondition, rules, validityConditions, DecisionType.STRING);
        value = new StringValue("");
    }

    @Override
    public String getStandardValue() {
        return standardValue;
    }

    @Override
    public IValue<String> getValue() {
        return value;
    }

    @Override
    public void setValue(IValue<String> value) throws ValidityConditionException {
        String v = Objects.requireNonNull(value.getValue());
        this.value.setValue(v);
        try {
            if (checkValidity()) {
                setSelected(true);
            } else {
                this.value.setValue(standardValue);
                throw new ValidityConditionException("Value: " + v + "does not fullfil validity condition");
            }
        } catch (EvaluationException e) {
            throw new ValidityConditionException(e);
        }
    }

    @Override
    public void createCpDecisionVariables(
            CpModel model, Map<IDecision<?>, List<IntVar>> decisionVars, Map<IDecision<?>, Literal> isTakenVars) {
        throw new UnsupportedOperationException("Not supported in the current CP-approach.");
    }

    @Override
    public void enforceStandardValueInCp(
            CpModel model, Map<IDecision<?>, List<IntVar>> decisionVars, Map<IDecision<?>, Literal> isTakenVars) {
        throw new UnsupportedOperationException("Not supported in the current CP-approach.");
    }

    @Override
    public void enforceValidityConditionsInCp(
            CpModel model, Map<IDecision<?>, List<IntVar>> decisionVars, Map<IDecision<?>, Literal> isTakenVars) {
        throw new UnsupportedOperationException("Not supported in the current CP-approach.");
    }
}
