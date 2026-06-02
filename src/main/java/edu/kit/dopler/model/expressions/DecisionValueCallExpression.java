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
package edu.kit.dopler.model.expressions;

import com.google.ortools.sat.BoolVar;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.Literal;
import edu.kit.dopler.model.decisions.IDecision;
import edu.kit.dopler.model.values.IValue;
import java.util.List;
import java.util.Map;

public final class DecisionValueCallExpression extends DecisionCallExpression {

    public DecisionValueCallExpression(final IDecision<?> decision) {
        super(decision);
    }

    @Override
    public boolean evaluate() {
        return getDecision().isTaken();
    }

    public IValue<?> getValue() {
        return getDecision().getValue();
    }

    @Override
    public Literal toCpLiteral(
            CpModel model, Map<IDecision<?>, List<IntVar>> decisionVars, Map<IDecision<?>, Literal> isTakenVars) {
        return (BoolVar) decisionVars.get(this.getDecision()).getFirst();
    }

    @Override
    public String toString() {
        return String.format("getValue(%s)", getDecision());
    }
}
