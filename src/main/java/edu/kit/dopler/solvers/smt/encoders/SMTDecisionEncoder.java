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
package edu.kit.dopler.solvers.smt.encoders;

import edu.kit.dopler.model.basic.EnumerationLiteral;
import edu.kit.dopler.model.decisions.*;
import edu.kit.dopler.solvers.shared.SolverUtils;
import edu.kit.dopler.solvers.smt.SMTConstants;
import edu.kit.dopler.solvers.smt.SMTContext;
import edu.kit.dopler.solvers.smt.utils.SMTUtils;
import java.util.ArrayList;
import java.util.List;
import org.sosy_lab.java_smt.api.*;

public final class SMTDecisionEncoder {

    private SMTDecisionEncoder() {
        throw new IllegalStateException("Utility class can't be instantiated");
    }

    /**
     * @param decision The DOPLER decision model to encode.
     * @param ctx  The active SMT context holding initialized variables.
     * @return A boolean formula representing the default equality constraint for this decision.
     */
    public static Formula encodeDecision(IDecision<?> decision, SMTContext ctx) {
        FormulaManager fm = ctx.fm();
        BooleanFormulaManager bfm = fm.getBooleanFormulaManager();
        String constName = SolverUtils.toStringConst(decision);

        return switch (decision) {
            case BooleanDecision bd -> {
                BooleanFormula valVar = (BooleanFormula) ctx.getVar(constName + SMTConstants.VALUE_SUFFIX);
                yield bfm.equivalence(valVar, bfm.makeBoolean(bd.getStandardValue()));
            }
            case NumberDecision nd -> {
                Formula valVar = ctx.getVar(constName + SMTConstants.VALUE_SUFFIX);
                yield fm.getRationalFormulaManager()
                        .equal(
                                (NumeralFormula.RationalFormula) valVar,
                                fm.getRationalFormulaManager()
                                        .makeNumber(nd.getStandardValue().toString()));
            }
            case StringDecision sd -> {
                Formula valVar = ctx.getVar(constName + SMTConstants.VALUE_SUFFIX);
                yield fm.getStringFormulaManager()
                        .equal(
                                (StringFormula) valVar,
                                fm.getStringFormulaManager().makeString(sd.getStandardValue()));
            }
            case EnumerationDecision ed -> {
                // Literals present in the standard value are forced true, all others false.
                List<BooleanFormula> defaults = new ArrayList<>();
                for (EnumerationLiteral literal : ed.getEnumeration().getEnumerationLiterals()) {
                    BooleanFormula litVar = (BooleanFormula) ctx.getVar(constName + "_" + literal.getValue());
                    boolean isDefault = ed.getStandardValue().contains(literal.getValue());
                    defaults.add(isDefault ? litVar : bfm.not(litVar));
                }
                yield SMTUtils.and(bfm, defaults);
            }
        };
    }
}
