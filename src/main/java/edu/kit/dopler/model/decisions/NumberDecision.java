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
import edu.kit.dopler.common.CpUtils;
import edu.kit.dopler.exceptions.EvaluationException;
import edu.kit.dopler.exceptions.ValidityConditionException;
import edu.kit.dopler.model.basic.Rule;
import edu.kit.dopler.model.expressions.IExpression;
import edu.kit.dopler.model.values.AbstractValue;
import edu.kit.dopler.model.values.DoubleValue;
import edu.kit.dopler.model.values.IValue;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class NumberDecision extends ValueDecision<Double> {

    private final AbstractValue<Double> value;
    private final double standardValue = 0.0;

    public NumberDecision(
            String displayId,
            String question,
            String description,
            IExpression visibilityCondition,
            Set<Rule> rules,
            Set<IExpression> validityConditions) {
        super(displayId, question, description, visibilityCondition, rules, validityConditions, DecisionType.NUMBER);
        value = new DoubleValue(standardValue);
    }

    @Override
    public void mapLogicToConstraintsInCp(
            CpModel model,
            Map<IDecision<?>, List<IntVar>> decisionVars,
            Map<IDecision<?>, Literal> isTakenVars,
            Map<IDecision<?>, List<Literal>> isTakenConditions) {
        this.enforceValidityConditionsInCp(model, decisionVars, isTakenVars); // adds constraints that enforce validity
        // conditions for a decision if necessary
        // (= if it is taken)

        super.mapLogicToConstraintsInCp(model, decisionVars, isTakenVars, isTakenConditions);
    }

    @Override
    public void createCpDecisionVariables(
            CpModel model, Map<IDecision<?>, List<IntVar>> decisionVars, Map<IDecision<?>, Literal> isTakenVars) {
        IntVar intVar = model.newIntVar(0, 1_000_000, this.getDisplayId());
        // normally, one should use Long min/max values as bounds here, but the solver
        // can't handle that.
        // This problem could only be solved if the range is parsed directly and
        // available here as double variables.
        // But that is against the dopler metamodel, since the range constraints are
        // modeled as validityConditions.

        decisionVars.put(this, List.of(intVar));
    }

    @Override
    public void enforceStandardValueInCp(
            CpModel model, Map<IDecision<?>, List<IntVar>> decisionVars, Map<IDecision<?>, Literal> isTakenVars) {
        model.addEquality(
                        decisionVars.get(this).getFirst(),
                        model.newConstant(CpUtils.scaleDoubleToLong(this.standardValue)))
                .onlyEnforceIf(isTakenVars.get(this).not());
    }

    @Override
    public void enforceValidityConditionsInCp(
            CpModel model, Map<IDecision<?>, List<IntVar>> decisionVars, Map<IDecision<?>, Literal> isTakenVars) {
        // add range constraints (only if the decision is taken)
        for (IExpression expression : this.getValidityConditions()) { // the range is encoded in the validityConditions
            model.addEquality(expression.toCpLiteral(model, decisionVars, isTakenVars), model.trueLiteral())
                    .onlyEnforceIf(isTakenVars.get(this));
        }
    }

    @Override
    public Double getStandardValue() {
        return standardValue;
    }

    @Override
    public IValue<Double> getValue() {
        return value;
    }

    @Override
    public void setValue(IValue<Double> value) throws ValidityConditionException {
        Double v = Objects.requireNonNull(value.getValue());
        this.value.setValue(v);
        try {
            if (checkValidity()) {
                setTaken(true);
            } else {

                this.value.setValue(standardValue);
                throw new ValidityConditionException("Value: " + v + "does not fullfil validity condition");
            }
        } catch (EvaluationException e) {
            throw new ValidityConditionException(e);
        }
    }
}
