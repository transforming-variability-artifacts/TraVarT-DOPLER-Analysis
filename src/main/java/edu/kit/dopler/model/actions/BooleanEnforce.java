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
package edu.kit.dopler.model.actions;

import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.Literal;
import edu.kit.dopler.exceptions.ActionExecutionException;
import edu.kit.dopler.model.decisions.BooleanDecision;
import edu.kit.dopler.model.decisions.IDecision;
import edu.kit.dopler.model.values.BooleanValue;
import edu.kit.dopler.model.values.IValue;
import java.util.List;
import java.util.Map;

public final class BooleanEnforce extends Enforce {

    public BooleanEnforce(BooleanDecision decision, IValue<Boolean> value) {
        super(decision, value);
    }

    @Override
    public void execute() throws ActionExecutionException {
        try {
            BooleanDecision booleanIDecision = (BooleanDecision) getDecision();
            BooleanValue booleanIValue = (BooleanValue) getValue();
            booleanIDecision.setValue(booleanIValue);
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
        // decision to be enforced = decisionVars.get(this.getDecision())
        // value to enforce = this.getValue()
        model.addEquality(
                        decisionVars.get(this.getDecision()).getFirst(),
                        this.getValue().getCpValue(model))
                .onlyEnforceIf(conditionLiteral);

        isTakenConditions.get(this.getDecision()).add(conditionLiteral);
    }

    @Override
    public String toString() {
        return String.format("%s = %s", getDecision(), getValue());
    }
}
