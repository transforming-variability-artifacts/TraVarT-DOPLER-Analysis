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
package edu.kit.dopler.model.basic;

import java.util.Set;

public class Enumeration {

    private final Set<EnumerationLiteral> enumerationLiterals;

    public Enumeration(Set<EnumerationLiteral> enumerationLiterals) {
        this.enumerationLiterals = enumerationLiterals;
    }

    public void addEnumLiteral(EnumerationLiteral enumLiteral) {
        enumerationLiterals.add(enumLiteral);
    }

    public Set<EnumerationLiteral> getEnumerationLiterals() {
        return enumerationLiterals;
    }
}
