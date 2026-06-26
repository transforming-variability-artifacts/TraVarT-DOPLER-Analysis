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

import static edu.kit.dopler.common.CpUtils.getEnumDecisionLiteralVariableName;

import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.Literal;
import edu.kit.dopler.exceptions.ActionExecutionException;
import edu.kit.dopler.model.decisions.EnumerationDecision;
import edu.kit.dopler.model.decisions.IDecision;
import edu.kit.dopler.model.values.IValue;
import edu.kit.dopler.model.values.StringValue;
import java.util.List;
import java.util.Map;

public final class EnumEnforce extends Enforce {

    public EnumEnforce(final EnumerationDecision decision, final IValue<?> value) {
        super(decision, value);
    }

    @Override
    public void execute() throws ActionExecutionException {
        try {
            final EnumerationDecision enumerationDecision = (EnumerationDecision) getDecision();
            final StringValue enumerationValue = (StringValue) getValue();
            enumerationDecision.setValue(enumerationValue);
            getDecision().setTaken(true);
        } catch (final Exception e) {
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
        String enforceString = getEnumDecisionLiteralVariableName(
                (EnumerationDecision) this.getDecision(), this.getValue().toString());

        // find the CP var representing the enum literal that is to be enforced:
        for (IntVar cpVar : decisionVars.get(this.getDecision())) {
            if (cpVar.getName().equals(enforceString)) {
                model.addEquality(cpVar, model.trueLiteral()).onlyEnforceIf(conditionLiteral);
            }
        }
    }

    @Override
    public String toString() {
        return String.format("%s = %s", getDecision(), getValue());
    }
}
