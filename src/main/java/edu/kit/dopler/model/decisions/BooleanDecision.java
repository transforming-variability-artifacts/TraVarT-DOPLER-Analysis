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

import com.google.ortools.sat.BoolVar;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.Literal;
import edu.kit.dopler.model.basic.Rule;
import edu.kit.dopler.model.expressions.IExpression;
import edu.kit.dopler.model.values.AbstractValue;
import edu.kit.dopler.model.values.BooleanValue;
import edu.kit.dopler.model.values.IValue;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class BooleanDecision extends Decision<Boolean> {

    private AbstractValue<Boolean> value;

    public BooleanDecision(
            String displayId, String question, String description, IExpression visibilityCondition, Set<Rule> rules) {
        super(displayId, question, description, visibilityCondition, rules, DecisionType.BOOLEAN);
        value = BooleanValue.getFalse();
    }

    @Override
    public void createCpDecisionVariables(
            CpModel model, Map<IDecision<?>, List<IntVar>> decisionVars, Map<IDecision<?>, Literal> isTakenVars) {
        BoolVar boolVar = model.newBoolVar(this.getDisplayId());

        decisionVars.put(this, List.of(boolVar));
    }

    @Override
    public void enforceStandardValueInCp(
            CpModel model, Map<IDecision<?>, List<IntVar>> decisionVars, Map<IDecision<?>, Literal> isTakenVars) {
        model.addEquality(
                        decisionVars.get(this).getFirst(),
                        this.getStandardValue() ? model.trueLiteral() : model.falseLiteral())
                .onlyEnforceIf(isTakenVars.get(this).not());
    }

    @Override
    public Boolean getStandardValue() {
        return false;
    }

    @Override
    public IValue<Boolean> getValue() {
        return value;
    }

    @Override
    public void setValue(IValue<Boolean> value) {
        this.value = (Objects.requireNonNull(value.getValue())) ? BooleanValue.getTrue() : BooleanValue.getFalse();
        setSelected(value.getValue());
    }
}
