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

import io.github.cvc5.*;
import io.github.cvc5.modes.InputLanguage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SMTValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SMTValidator.class);

    public sealed interface SMTValidationResult permits SMTValidationResult.Success, SMTValidationResult.Error {
        record Success() implements SMTValidationResult {}

        record Error(String message) implements SMTValidationResult {}
    }

    private SMTValidator() {
        throw new IllegalStateException("Utility class can't be instantiated");
    }

    /**
     * Validates raw SMT-LIB 2.6 strings using cvc5.
     *
     * @param smt The SMT-LIB code string.
     * @return {@link SMTValidationResult.Success} if syntax is valid, otherwise {@link SMTValidationResult.Error} with the message.
     */
    public static SMTValidationResult validate(String smt) {
        try {
            TermManager termManager = new TermManager();
            Solver solver = new Solver(termManager);
            solver.setOption("strict-parsing", "true");

            SymbolManager sm = new SymbolManager(termManager);
            InputParser parser = new InputParser(solver, sm);

            parser.setStringInput(InputLanguage.SMT_LIB_2_6, smt, "input.smt2");

            while (true) {
                Command cmd = parser.nextCommand();
                if (cmd.isNull()) {
                    break;
                }
                cmd.invoke(solver, sm);
            }

            return new SMTValidationResult.Success();
        } catch (Exception e) {
            LOGGER.debug("SMT Validation failed to parse input.", e);
            return new SMTValidationResult.Error(e.getMessage());
        }
    }
}
