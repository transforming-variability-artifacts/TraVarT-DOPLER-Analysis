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
package edu.kit.dopler.solvers.smt;

import java.util.Objects;
import org.sosy_lab.java_smt.SolverContextFactory;

public final class SMTConstants {
    public static final String TAKEN_SUFFIX = "_TAKEN";
    public static final String VALUE_SUFFIX = "_VALUE";

    private static SolverContextFactory.Solvers defaultSolver = SolverContextFactory.Solvers.CVC5;

    private SMTConstants() {
        throw new IllegalStateException("Utility class can't be instantiated");
    }

    public static SolverContextFactory.Solvers getDefaultModel() {
        return defaultSolver;
    }

    public static void setDefaultModel(SolverContextFactory.Solvers solver) {
        Objects.requireNonNull(solver, "Default solver cannot be null");
        defaultSolver = solver;
    }
}
