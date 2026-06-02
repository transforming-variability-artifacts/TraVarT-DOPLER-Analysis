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
package edu.kit.dopler.model.values;

import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.LinearArgument;

public final class BooleanValue extends AbstractValue<Boolean> {

    public BooleanValue(Boolean value) {
        super(value);
    }

    public static BooleanValue getTrue() {
        return new BooleanValue(true);
    }

    public static BooleanValue getFalse() {
        return new BooleanValue(false);
    }

    @Override
    public LinearArgument getCpValue(CpModel model) {
        return getValue() ? model.trueLiteral() : model.falseLiteral();
    }

    @Override
    public String toString() {
        return String.valueOf(getValue());
    }
}
