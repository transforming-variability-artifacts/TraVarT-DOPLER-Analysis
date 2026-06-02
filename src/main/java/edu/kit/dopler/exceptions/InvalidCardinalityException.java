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
package edu.kit.dopler.exceptions;

import java.security.PrivilegedActionException;

public class InvalidCardinalityException extends Exception {

    /**
     * Constructs a new exception with the specified detail message. The cause is
     * not initialized, and may subsequently be initialized by a call to
     * {@link #initCause}.
     *
     * @param message
     *            the detail message. The detail message is saved for later
     *            retrieval by the {@link #getMessage()} method.
     */
    public InvalidCardinalityException(final String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message of
     * {@code (cause==null ? null : cause.toString())} (which typically contains the
     * class and detail message of {@code cause}). This constructor is useful for
     * exceptions that are little more than wrappers for other throwables (for
     * example, {@link PrivilegedActionException}).
     *
     * @param cause
     *            the cause (which is saved for later retrieval by the
     *            {@link #getCause()} method). (A {@code null} value is permitted,
     *            and indicates that the cause is nonexistent or unknown.)
     * @since 1.4
     */
    public InvalidCardinalityException(final Throwable cause) {
        super(cause);
    }
}
