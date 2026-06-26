/*******************************************************************************
 * SPDX-License-Identifier: MPL-2.0
 *
 * Copyright (c) 2026 Karlsruhe Institute of Technology (KIT)
 * KASTEL - Dependability of Software-intensive Systems
 *
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *******************************************************************************/
package edu.kit.dopler.solvers.ilp;

import java.util.Objects;

public final class ILPConstants {

    public static final String TAKEN_SUFFIX = "_TAKEN";
    public static final String VALUE_SUFFIX = "_VALUE";

    public static final double EPSILON = 1e-6;
    // Seems to at least work for all test cases
    public static final double FALLBACK_BIG_M = 10000.0;

    private static ILPModels defaultModel = ILPModels.CBC;

    private ILPConstants() {
        throw new IllegalStateException("Utility class can't be instantiated");
    }

    public static ILPModels getDefaultModel() {
        return defaultModel;
    }

    public static void setDefaultModel(ILPModels model) {
        Objects.requireNonNull(model, "Default model cannot be null");
        defaultModel = model;
    }

    public enum ILPModels {
        SCIP("SCIP"),
        CBC("CBC");

        private final String name;

        ILPModels(String name) {
            this.name = name;
        }

        public String getModelName() {
            return name;
        }
    }
}
