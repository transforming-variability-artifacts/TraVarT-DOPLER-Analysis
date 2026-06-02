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
package edu.kit.dopler.solvers.smt.utils;

import edu.kit.dopler.solvers.smt.NativeLibLoader;
import edu.kit.dopler.solvers.smt.SMTConstants;
import java.util.List;
import org.sosy_lab.common.ShutdownManager;
import org.sosy_lab.common.configuration.Configuration;
import org.sosy_lab.common.configuration.ConfigurationBuilder;
import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.common.log.LogManager;
import org.sosy_lab.java_smt.SolverContextFactory;
import org.sosy_lab.java_smt.api.BooleanFormula;
import org.sosy_lab.java_smt.api.BooleanFormulaManager;
import org.sosy_lab.java_smt.api.SolverContext;

public final class SMTUtils {

    private SMTUtils() {
        throw new IllegalStateException("Utility class can't be instantiated");
    }

    /**
     * Creates a new SMT solver context using the default solver specified in {@link SMTConstants#DEFAULT_SOLVER}.
     *
     * @return An initialized {@link SolverContext} ready for use.
     */
    public static SolverContext createSolverContext() {
        return createSolverContext(SMTConstants.DEFAULT_SOLVER);
    }

    /**
     * Creates a new SMT solver context using a specific solver backend.
     *
     * @param solver The specific SMT solver to initialize (e.g., Z3, CVC5).
     * @return An initialized {@link SolverContext} ready for use.
     */
    public static SolverContext createSolverContext(SolverContextFactory.Solvers solver) {
        try {
            ConfigurationBuilder config = Configuration.builder();
            return SolverContextFactory.createSolverContext(
                    config.build(),
                    LogManager.createNullLogManager(),
                    ShutdownManager.create().getNotifier(),
                    solver,
                    NativeLibLoader::loadLibrary);
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Combines a list of boolean expressions using a logical AND operation.
     * This method safely handles empty or single-element lists to prevent solver errors.
     * Returns {@code true} if the list is empty.
     *
     * @param bfm The {@link BooleanFormulaManager} used to construct the formula.
     * @param expressions The list of {@link BooleanFormula} expressions to combine.
     * @return A {@link BooleanFormula} representing the logical AND of all expressions.
     */
    public static BooleanFormula and(BooleanFormulaManager bfm, List<BooleanFormula> expressions) {
        if (expressions.isEmpty()) {
            return bfm.makeTrue();
        } else if (expressions.size() == 1) {
            // Can be interpreted by most parsers, but 'and' statements with a single value are not valid SMT 2.
            return expressions.getFirst();
        }
        return bfm.and(expressions);
    }
}
