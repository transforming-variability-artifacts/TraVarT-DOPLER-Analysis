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
package edu.kit.dopler.solvers.shared;

import edu.kit.dopler.model.decisions.IDecision;

public final class SolverUtils {

    private SolverUtils() {
        throw new IllegalStateException("Utility class can't be instantiated");
    }

    /**
     * Generates a human-readable string identifier for a given decision.
     *
     * @param decision The DOPLER decision to generate an identifier for.
     * @return A string constant representing the decision.
     */
    public static String toStringConst(IDecision<?> decision) {
        return "DECISION_" + decision.getDisplayId();
    }
}
