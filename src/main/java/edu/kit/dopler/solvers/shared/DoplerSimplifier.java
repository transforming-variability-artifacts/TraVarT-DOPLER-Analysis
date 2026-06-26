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

import edu.kit.dopler.model.Dopler;
import edu.kit.dopler.model.decisions.IDecision;

/**
 * Simplify a DOPLER model based on an AnomalyReport.
 */
public final class DoplerSimplifier {

    private DoplerSimplifier() {
        throw new IllegalStateException("Utility class can't be instantiated");
    }

    /**
     * Simplifies the provided Dopler model by removing dead decisions.
     * @param model The Dopler model to simplify.
     *
     * @param report The AnomalyReport containing anomalies to process.
     */
    public static void simplify(Dopler model, AnomalyReport report) {
        if (report == null || !report.hasAnomalies()) {
            return;
        }

        // Remove dead decisions from the model
        if (report.deadDecisions() != null) {
            for (IDecision<?> deadDecision : report.deadDecisions()) {
                model.removeDecision(deadDecision);
            }
        }
        // Dead decision values are not removed for now because this would require removing any reference in every
        // statement.
    }
}
