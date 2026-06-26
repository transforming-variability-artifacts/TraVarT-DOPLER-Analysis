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
package edu.kit.dopler.solvers.smt;

import java.util.Objects;
import org.sosy_lab.java_smt.SolverContextFactory;

public final class SMTConstants {
    public static final String TAKEN_SUFFIX = "_TAKEN";
    public static final String VALUE_SUFFIX = "_VALUE";

    private static SMTSolvers defaultSolver = SMTSolvers.CVC5;

    private SMTConstants() {
        throw new IllegalStateException("Utility class can't be instantiated");
    }

    public static SolverContextFactory.Solvers getDefaultModel() {
        return defaultSolver.getSolver();
    }

    public static void setDefaultModel(SMTSolvers solver) {
        Objects.requireNonNull(solver, "Default solver cannot be null");
        defaultSolver = solver;
    }

    public enum SMTSolvers {
        CVC5(SolverContextFactory.Solvers.CVC5),
        Z3(SolverContextFactory.Solvers.Z3);

        private final SolverContextFactory.Solvers solver;

        SMTSolvers(SolverContextFactory.Solvers solver) {
            this.solver = solver;
        }

        public SolverContextFactory.Solvers getSolver() {
            return solver;
        }
    }
}
