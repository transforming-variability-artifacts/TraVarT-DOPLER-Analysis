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
package edu.kit.dopler.solvers.ilp.encoders;

import com.google.ortools.linearsolver.MPVariable;
import edu.kit.dopler.model.basic.EnumerationLiteral;
import edu.kit.dopler.model.decisions.*;
import edu.kit.dopler.solvers.ilp.ILPConstants;
import edu.kit.dopler.solvers.ilp.ILPContext;
import edu.kit.dopler.solvers.ilp.utils.ILPLogic;
import edu.kit.dopler.solvers.shared.SolverUtils;
import java.util.ArrayList;
import java.util.List;

public final class ILPDecisionEncoder {
    private ILPDecisionEncoder() {
        throw new IllegalStateException("Utility class can't be instantiated");
    }

    /**
     * Enforces that the decision holds its default value.
     * @param decision The DOPLER decision model to encode.
     * @param ctx  The active ILP context holding initialized variables.
     * @return An auxiliary boolean MPVariable that is 1 if the default value holds.
     */
    public static MPVariable encodeDecision(IDecision<?> decision, ILPContext ctx) {
        String constName = SolverUtils.toStringConst(decision);

        return switch (decision) {
            case BooleanDecision bd -> {
                MPVariable valVar = ctx.getCoreVar(constName + ILPConstants.VALUE_SUFFIX);
                MPVariable targetVal = ctx.createConstant(bd.getStandardValue() ? 1.0 : 0.0);
                yield ILPLogic.equals(ctx, valVar, targetVal);
            }
            case NumberDecision nd -> {
                MPVariable valVar = ctx.getCoreVar(constName + ILPConstants.VALUE_SUFFIX);
                MPVariable targetVal = ctx.createConstant(nd.getStandardValue());
                double M = ILPLogic.getBigM(valVar, targetVal);
                yield ILPLogic.numericEquals(ctx, valVar, targetVal, M);
            }
            case StringDecision ignored -> throw new UnsupportedOperationException("String are not supported in ILP.");
            case EnumerationDecision ed -> {
                List<MPVariable> literalDefaults = new ArrayList<>();
                for (EnumerationLiteral literal : ed.getEnumeration().getEnumerationLiterals()) {
                    MPVariable litVar = ctx.getCoreVar(constName + "_" + literal.getValue());
                    boolean isDefault = ed.getStandardValue().contains(literal.getValue());
                    // If default, litVar must be 1. If not default, litVar must be 0.
                    literalDefaults.add(isDefault ? litVar : ILPLogic.not(ctx, litVar));
                }
                yield ILPLogic.and(ctx, literalDefaults.toArray(new MPVariable[0]));
            }
        };
    }
}
