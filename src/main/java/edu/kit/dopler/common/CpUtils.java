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
package edu.kit.dopler.common;

import edu.kit.dopler.model.decisions.EnumerationDecision;

/**
 * Utility class providing helper methods and constants for Constraint
 * Programming (CP) models.
 */
public final class CpUtils {
    /**
     * The String that is used for the construction of enum-decision-variable-names.
     */
    public static final String CP_ENUM_SEPARATOR = "#";
    /**
     * CP does not support floating point values (only integers). We use integers
     * scaled with {@value DOUBLES_SCALING_FACTOR} to emulate floating point values.
     */
    private static final Double DOUBLES_SCALING_FACTOR = 0.0001;

    /**
     * Constructs the name of the CP variable representing a literal of an
     * enumeration decision.
     *
     * @param enumerationDecision
     *            The enumeration decision
     * @param enumerationLiteralName
     *            The name of the enumeration literal
     * @return A string representing the name
     */
    public static String getEnumDecisionLiteralVariableName(
            EnumerationDecision enumerationDecision, String enumerationLiteralName) {
        return enumerationDecision.getDisplayId() + CpUtils.CP_ENUM_SEPARATOR + enumerationLiteralName;
    }

    /**
     * Scales a double value to a long value that can be used by the CP solver.
     *
     * @param value
     *            The double value to be scaled.
     * @return The resulting long value after scaling and rounding.
     */
    public static Long scaleDoubleToLong(double value) {
        return Math.round(value / DOUBLES_SCALING_FACTOR);
    }

    /**
     * Scales a long value used by the CP solver to the double value it represents.
     *
     * @param value
     *            The long value to be scaled.
     * @return The resulting double.
     */
    public static Double scaleLongToDouble(Long value) {
        return value * DOUBLES_SCALING_FACTOR;
    }
}
