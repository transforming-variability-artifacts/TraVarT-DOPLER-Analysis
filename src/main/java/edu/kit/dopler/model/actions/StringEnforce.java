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
import edu.kit.dopler.exceptions.ActionExecutionException;
import edu.kit.dopler.model.decisions.IDecision;
import edu.kit.dopler.model.decisions.StringDecision;
import edu.kit.dopler.model.values.IValue;
import edu.kit.dopler.model.values.StringValue;
import java.util.List;
import java.util.Map;

public final class StringEnforce extends Enforce {

    public StringEnforce(IDecision<?> decision, IValue<?> value) {
        super(decision, value);
    }

    @Override
    public void execute() throws ActionExecutionException {
        try {
            StringDecision stringDecision = (StringDecision) getDecision();
            StringValue stringValue = (StringValue) getValue();
            stringDecision.setValue(stringValue);
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
        throw new UnsupportedOperationException("Not supported in the current CP-approach.");
    }

    @Override
    public String toString() {
        return String.format("%s = '%s'", getDecision(), getValue());
    }
}
