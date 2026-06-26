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

import edu.kit.dopler.model.decisions.IDecision;
import edu.kit.dopler.model.values.IValue;

public abstract sealed class Enforce extends ValueRestrictionAction
        permits BooleanEnforce, EnumEnforce, NumberEnforce, StringEnforce {

    public static final String FUNCTION_NAME = "enforce";

    private final IValue<?> value;

    public Enforce(final IDecision<?> decision, final IValue<?> value) {
        super(decision);
        this.value = value;
    }

    public IValue<?> getValue() {
        return value;
    }
}
