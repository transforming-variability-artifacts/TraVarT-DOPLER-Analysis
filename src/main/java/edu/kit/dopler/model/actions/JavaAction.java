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
import edu.kit.dopler.model.decisions.IDecision;
import java.util.List;
import java.util.Map;

public final class JavaAction extends Action {

    @Override
    public void execute() {}

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
        return "not yet implemented";
    }
}
