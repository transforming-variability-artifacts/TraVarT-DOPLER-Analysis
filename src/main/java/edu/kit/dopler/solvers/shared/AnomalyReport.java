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
import java.util.Collections;
import java.util.List;

/**
 * A report detailing any anomalies found in a DOPLER model.
 * An anomaly indicates a modeling error where the declared options
 * contradict the constraints of the system.
 * <ul>
 * <li><b>Dead Decision:</b> A decision that can never be taken in any valid configuration.</li>
 * <li><b>False Optional Decision:</b> A decision marked as optional, but constraints force it to always be taken (mandatory).</li>
 * <li><b>Dead Value:</b> A specific option within a decision that can never be selected without violating constraints.</li>
 * <li><b>False Optional Value:</b> A decision that has multiple choices, but constraints force exactly one value.</li>
 * </ul>
 */
public record AnomalyReport(
        boolean isBaseModelUnsat,
        List<IDecision<?>> deadDecisions,
        List<IDecision<?>> falseOptionalDecisions,
        List<ValueAnomaly> deadValues,
        List<ValueAnomaly> falseOptionalValues) {

    /**
     * @return {@code true} if any anomaly is found or the base model is invalid.
     */
    public boolean hasAnomalies() {
        return isBaseModelUnsat
                || !deadDecisions.isEmpty()
                || !falseOptionalDecisions.isEmpty()
                || !deadValues.isEmpty()
                || !falseOptionalValues.isEmpty();
    }

    public static AnomalyReport empty(boolean isBaseModelUnsat) {
        return new AnomalyReport(
                isBaseModelUnsat,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList());
    }
}
