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

public sealed interface IValue<T> permits AbstractValue {

    T getValue();

    void setValue(T value);

    /**
     * Retrieves the CP representation of the current value for a given CP model.
     *
     * @param model
     *            the constraint programming model
     * @return the CP representation of the value as a {@code LinearArgument}
     */
    LinearArgument getCpValue(CpModel model);
}
