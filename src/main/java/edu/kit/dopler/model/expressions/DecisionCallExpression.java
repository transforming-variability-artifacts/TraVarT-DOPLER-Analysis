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

import edu.kit.dopler.model.decisions.IDecision;

@SuppressWarnings("rawtypes")
public abstract sealed class DecisionCallExpression extends Expression
        permits DecisionValueCallExpression, DecisionVisibilityCallExpression, IsTaken {

    private final IDecision<?> decision;

    public DecisionCallExpression(IDecision<?> decision) {
        this.decision = decision;
    }

    public IDecision getDecision() {
        return decision;
    }
}
