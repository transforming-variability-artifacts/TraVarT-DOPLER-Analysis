/*******************************************************************************
 * SPDX-License-Identifier: MPL-2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/.
 *
 * Copyright 2026 Karlsruhe Institute of Technology (KIT)
 * KASTEL - Dependability of Software-intensive Systems
 *******************************************************************************/
package edu.kit.dopler.solvers.shared;

import edu.kit.dopler.model.decisions.IDecision;
import java.util.List;

/**
 * A report detailing any anomalies found in a DOPLER model.
 */
public record AnomalyReport(
        boolean isBaseModelUnsat, List<IDecision<?>> deadDecisions, List<IDecision<?>> falseOptionalDecisions) {
    /**
     * @return {@code true} if any anomaly is found or the base model is invalid.
     */
    public boolean hasAnomalies() {
        return isBaseModelUnsat || !deadDecisions.isEmpty() || !falseOptionalDecisions.isEmpty();
    }
}
