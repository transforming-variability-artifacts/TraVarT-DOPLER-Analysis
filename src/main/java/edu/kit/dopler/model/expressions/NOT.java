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
package edu.kit.dopler.model.expressions;

import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.Literal;
import edu.kit.dopler.exceptions.EvaluationException;
import edu.kit.dopler.model.decisions.IDecision;
import java.util.List;
import java.util.Map;

public final class NOT extends UnaryExpression {

    private static final String SYMBOL = "!";

    public NOT(IExpression operand) {
        super(operand);
    }

    @Override
    public boolean evaluate() throws EvaluationException {
        return !getOperand().evaluate();
    }

    @Override
    public Literal toCpLiteral(
            CpModel model, Map<IDecision<?>, List<IntVar>> decisionVars, Map<IDecision<?>, Literal> isTakenVars) {
        return this.getOperand().toCpLiteral(model, decisionVars, isTakenVars).not();
    }

    @Override
    public String toString() {
        return String.format("%s%s", SYMBOL, getOperand());
    }
}
