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

import org.sosy_lab.java_smt.SolverContextFactory;

public class SMTConstants {
    public static final String TAKEN_SUFFIX = "_TAKEN";
    public static final String VALUE_SUFFIX = "_VALUE";
    public static final SolverContextFactory.Solvers DEFAULT_SOLVER = SolverContextFactory.Solvers.CVC5;
}
