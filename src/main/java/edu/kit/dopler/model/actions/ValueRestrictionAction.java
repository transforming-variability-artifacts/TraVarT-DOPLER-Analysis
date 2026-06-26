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

import edu.kit.dopler.model.decisions.IDecision;

public abstract sealed class ValueRestrictionAction extends Action permits Allows, DisAllows, Enforce {

    private final IDecision<?> decision;

    public ValueRestrictionAction(IDecision<?> decision) {
        this.decision = decision;
    }

    public IDecision<?> getDecision() {
        return decision;
    }
}
