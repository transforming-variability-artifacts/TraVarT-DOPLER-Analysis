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

import java.util.Objects;

public abstract sealed class AbstractValue<T> implements IValue<T> permits BooleanValue, DoubleValue, StringValue {

    private T value;

    protected AbstractValue(final T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(final T value) {
        this.value = Objects.requireNonNull(value);
    }
}
