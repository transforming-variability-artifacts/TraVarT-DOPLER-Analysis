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
package edu.kit.dopler.model.values;

import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.LinearArgument;
import java.util.Objects;

public final class StringValue extends AbstractValue<String> {

    public StringValue(String value) {
        super(Objects.requireNonNull(value));
    }

    @Override
    public LinearArgument getCpValue(CpModel model) {
        throw new UnsupportedOperationException("Not supported in the current CP-approach.");
    }

    @Override
    public String toString() {
        return getValue();
    }
}
