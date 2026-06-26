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
package edu.kit.dopler.model.actions;

import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.Literal;
import edu.kit.dopler.common.CpUtils;
import edu.kit.dopler.exceptions.ActionExecutionException;
import edu.kit.dopler.model.decisions.IDecision;
import edu.kit.dopler.model.decisions.NumberDecision;
import edu.kit.dopler.model.values.DoubleValue;
import edu.kit.dopler.model.values.IValue;
import java.util.List;
import java.util.Map;

public final class NumberEnforce extends Enforce {

    public NumberEnforce(NumberDecision decision, IValue<?> value) {
        super(decision, value);
    }

    @Override
    public void execute() throws ActionExecutionException {
        try {
            NumberDecision numberDecision = (NumberDecision) getDecision();
            DoubleValue doubleValue = (DoubleValue) getValue();
            numberDecision.setValue(doubleValue);
            getDecision().setTaken(true);
        } catch (Exception e) {
            throw new ActionExecutionException(e);
        }
    }

    @Override
    public void addCpConstraints(
            CpModel model,
            Literal conditionLiteral,
            Map<IDecision<?>, List<IntVar>> decisionVars,
            Map<IDecision<?>, Literal> isTakenVars,
            Map<IDecision<?>, List<Literal>> isTakenConditions) {
        model.addEquality(
                        decisionVars.get(this.getDecision()).getFirst(),
                        CpUtils.scaleDoubleToLong(((DoubleValue) this.getValue()).getValue()))
                .onlyEnforceIf(conditionLiteral);

        isTakenConditions.get(this.getDecision()).add(conditionLiteral);
    }

    @Override
    public String toString() {
        return String.format("%s = %s", getDecision(), getValue());
    }
}
