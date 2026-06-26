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
package edu.kit.dopler.solvers.smt.utils;

import edu.kit.dopler.model.Dopler;
import edu.kit.dopler.solvers.smt.SMTContext;
import edu.kit.dopler.solvers.smt.encoders.SMTGlobalConstraintEncoder;
import org.sosy_lab.java_smt.api.SolverException;

public final class SMTSolver {

    private SMTSolver() {
        throw new IllegalStateException("Utility class can't be instantiated");
    }

    /**
     * Checks if the given DOPLER model is satisfiable.
     * @param dopler The DOPLER model to verify.
     * @return {@code true} if the SMT encoding is satisfiable or {@code false} if it's unsatisfiable.
     */
    public static boolean isSatisfiable(final Dopler dopler) {
        try (SMTContext context = SMTContext.create(SMTUtils.createSolverContext())) {
            SMTGlobalConstraintEncoder.encodeToSMT(dopler, context);
            return context.isSat();
        } catch (SolverException | InterruptedException e) {
            throw new RuntimeException("Failed to verify satisfiability", e);
        }
    }

    /**
     * Generates SMT Code for the model and checks it with cvc5 for strict syntax validity according to SMT-LIB 2.6.
     * @param dopler The DOPLER model to validate.
     * @throws IllegalStateException if the generated code is invalid, as this should not happen.
     */
    public static void assertValidCode(final Dopler dopler) {
        try (SMTContext context = SMTContext.create(SMTUtils.createSolverContext())) {
            SMTGlobalConstraintEncoder.encodeToSMT(dopler, context);
            String smtCode = context.toString();
            switch (SMTValidator.validate(smtCode)) {
                case SMTValidator.SMTValidationResult.Error err ->
                    throw new IllegalStateException("Invalid SMT Code generated: " + err.message());
                case SMTValidator.SMTValidationResult.Success ignored -> {}
            }
        }
    }
}
