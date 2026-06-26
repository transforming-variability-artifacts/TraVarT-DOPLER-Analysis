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

public final class DoubleValue extends AbstractValue<Double> {

    public DoubleValue(final double value) {
        super(value);
    }

    @Override
    public LinearArgument getCpValue(CpModel model) {
        throw new UnsupportedOperationException("should not be called in CP-approach"); // the CP approach uses
        // getValue() and then the
        // utility method
        // scaleDoubleToCp(double value)
        // to get a cp usable
        // DoubleValue
    }

    @Override
    public String toString() {
        return String.valueOf(getValue());
    }
}
