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

import static edu.kit.dopler.common.CpUtils.getEnumDecisionLiteralVariableName;

import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.LinearExpr;
import com.google.ortools.sat.Literal;
import edu.kit.dopler.exceptions.InvalidCardinalityException;
import edu.kit.dopler.exceptions.ValidityConditionException;
import edu.kit.dopler.model.basic.Enumeration;
import edu.kit.dopler.model.basic.EnumerationLiteral;
import edu.kit.dopler.model.basic.Rule;
import edu.kit.dopler.model.expressions.IExpression;
import edu.kit.dopler.model.values.AbstractValue;
import edu.kit.dopler.model.values.IValue;
import edu.kit.dopler.model.values.StringValue;
import java.util.*;

public final class EnumerationDecision extends Decision<String> {

    private final AbstractValue<String> value;
    private final Set<EnumerationLiteral> disAllowed;
    private Enumeration enumeration;
    private int minCardinality;
    private int maxCardinality;

    public EnumerationDecision(
            String displayId,
            String question,
            String description,
            IExpression visibilityCondition,
            Set<Rule> rules,
            Enumeration enumeration,
            int minCardinality,
            int maxCardinality) {
        super(displayId, question, description, visibilityCondition, rules, DecisionType.ENUM);
        this.enumeration = enumeration;
        this.minCardinality = minCardinality;
        this.maxCardinality = maxCardinality;
        value = new StringValue("null");
        disAllowed = new HashSet<>();
    }

    @Override
    public String getStandardValue() {
        return "null";
    }

    @Override
    public IValue<String> getValue() {
        return value;
    }

    @Override
    public void setValue(IValue<String> value) throws ValidityConditionException {
        String v = Objects.requireNonNull(value.getValue());
        if (inRange(v) && allowed(v)) {
            this.value.setValue(v);
            setTaken(true);
        } else {
            throw new ValidityConditionException("Value: " + v + " not in Range");
        }
    }

    @Override
    public void createCpDecisionVariables(
            CpModel model, Map<IDecision<?>, List<IntVar>> decisionVars, Map<IDecision<?>, Literal> isTakenVars) {
        Set<EnumerationLiteral> enumerationLiterals = this.getEnumeration().getEnumerationLiterals();

        // create and add variables (one for each enum entry)
        List<IntVar> enumVars = new ArrayList<>();
        for (EnumerationLiteral el : enumerationLiterals) {
            enumVars.add(model.newBoolVar(getEnumDecisionLiteralVariableName(this, el.getValue())));
        }

        decisionVars.put(this, enumVars);

        // add cardinality constraints (only if the decision is taken)
        LinearExpr sum = LinearExpr.sum(enumVars.toArray(new IntVar[0]));
        model.addGreaterOrEqual(sum, this.getMinCardinality()).onlyEnforceIf(isTakenVars.get(this));
        model.addLessOrEqual(sum, this.getMaxCardinality()).onlyEnforceIf(isTakenVars.get(this));
    }

    @Override
    public void enforceStandardValueInCp(
            CpModel model, Map<IDecision<?>, List<IntVar>> decisionVars, Map<IDecision<?>, Literal> isTakenVars) {
        decisionVars.get(this).forEach(var -> model.addEquality(var, model.falseLiteral())
                .onlyEnforceIf(isTakenVars.get(this).not()));
    }

    public void setCardinality(int minCardinality, int maxCardinality) throws InvalidCardinalityException {
        if (minCardinality < 0) {
            throw new InvalidCardinalityException("min cardinality is negative" + minCardinality);
        } else if (minCardinality > maxCardinality) {
            throw new InvalidCardinalityException(
                    "min cardinality: " + minCardinality + "is bigger than max cardinality: " + maxCardinality);
        } else {
            this.minCardinality = minCardinality;
            this.maxCardinality = maxCardinality;
        }
    }

    private boolean inRange(String value) {
        return enumeration.getEnumerationLiterals().stream()
                .anyMatch(enumerationLiteral -> enumerationLiteral.getValue().equals(value));
    }

    private boolean allowed(String value) {
        return disAllowed.stream()
                .noneMatch(enumerationLiteral -> enumerationLiteral.getValue().equals(value));
    }

    public void addDissallowed(EnumerationLiteral literal) {
        disAllowed.add(literal);
    }

    public void removeDissallowed(EnumerationLiteral literal) {
        disAllowed.remove(literal);
    }

    public Enumeration getEnumeration() {
        return enumeration;
    }

    public void setEnumeration(Enumeration enumeration) {
        this.enumeration = enumeration;
    }

    public int getMaxCardinality() {
        return maxCardinality;
    }

    public int getMinCardinality() {
        return minCardinality;
    }
}
