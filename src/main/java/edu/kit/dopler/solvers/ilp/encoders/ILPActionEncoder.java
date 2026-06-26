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
import edu.kit.dopler.model.actions.*;
import edu.kit.dopler.model.decisions.Decision;
import edu.kit.dopler.model.decisions.IDecision;
import edu.kit.dopler.solvers.ilp.ILPConstants;
import edu.kit.dopler.solvers.ilp.ILPContext;
import edu.kit.dopler.solvers.ilp.utils.ILPLogic;
import edu.kit.dopler.solvers.shared.SolverUtils;

public final class ILPActionEncoder {
    private ILPActionEncoder() {
        throw new IllegalStateException("Utility class can't be instantiated");
    }

    public static void encodeAction(IAction action, MPVariable activation, ILPContext ctx) {
        switch (action) {
            case Allows allows -> {
                // No-op
            }
            case DisAllows disAllows -> {
                String targetConst = SolverUtils.toStringConst(disAllows.getDecision());
                IDecision<?> decision = disAllows.getDecision();

                if (decision.getDecisionType() == Decision.DecisionType.ENUM && disAllows.getDisAllowValue() != null) {
                    MPVariable enumVar = ctx.getCoreVar(
                            targetConst + "_" + disAllows.getDisAllowValue().getValue());
                    if (enumVar != null) {
                        enforceExcludes(ctx, activation, enumVar);
                    }
                } else if (disAllows.getDisAllowValue() != null) {
                    MPVariable valVar = ctx.getCoreVar(targetConst + ILPConstants.VALUE_SUFFIX);
                    if (valVar != null) {
                        MPVariable targetVal = convertToILPConstant(
                                decision, disAllows.getDisAllowValue().getValue(), ctx);

                        if (decision.getDecisionType() == Decision.DecisionType.BOOLEAN) {
                            MPVariable eqVar = ILPLogic.equals(ctx, valVar, targetVal);
                            enforceExcludes(ctx, activation, eqVar);
                        } else {
                            double M = ILPLogic.getBigM(valVar, targetVal);
                            MPVariable eqVar = ILPLogic.numericEquals(ctx, valVar, targetVal, M);
                            enforceExcludes(ctx, activation, eqVar);
                        }
                    }
                } else {
                    MPVariable targetIsTaken = ctx.getCoreVar(targetConst + ILPConstants.TAKEN_SUFFIX);
                    if (targetIsTaken != null) {
                        enforceExcludes(ctx, activation, targetIsTaken);
                    }
                }

                ctx.addIsTakenConditions(decision, activation);
            }
            case Enforce enforce -> {
                String targetConst = SolverUtils.toStringConst(enforce.getDecision());
                IDecision<?> decision = enforce.getDecision();

                if (decision.getDecisionType() == Decision.DecisionType.ENUM) {
                    MPVariable enumVar = ctx.getCoreVar(
                            targetConst + "_" + enforce.getValue().getValue());
                    enforceImplies(ctx, activation, enumVar);
                } else {
                    MPVariable valVar = ctx.getCoreVar(targetConst + ILPConstants.VALUE_SUFFIX);
                    if (valVar != null) {
                        MPVariable targetVal = convertToILPConstant(
                                decision, enforce.getValue().getValue(), ctx);

                        if (decision.getDecisionType() == Decision.DecisionType.BOOLEAN) {
                            MPVariable eqVar = ILPLogic.equals(ctx, valVar, targetVal);
                            enforceImplies(ctx, activation, eqVar);
                        } else {
                            double M = ILPLogic.getBigM(valVar, targetVal);
                            MPVariable eqVar = ILPLogic.numericEquals(ctx, valVar, targetVal, M);
                            enforceImplies(ctx, activation, eqVar);
                        }
                    }
                }

                ctx.addIsTakenConditions(decision, activation);
            }
            case JavaAction ignored -> {
                // not yet implemented
            }
        }
    }

    /**
     * Hard constraint: condition => consequence
     * Delegated to the new ILPLogic utility.
     */
    private static void enforceImplies(ILPContext ctx, MPVariable condition, MPVariable consequence) {
        ILPLogic.implies(ctx, condition, consequence);
    }

    /**
     * Hard constraint: condition => NOT consequence
     */
    private static void enforceExcludes(ILPContext ctx, MPVariable condition, MPVariable consequence) {
        MPVariable notConsequence = ILPLogic.not(ctx, consequence);
        ILPLogic.implies(ctx, condition, notConsequence);
    }

    private static MPVariable convertToILPConstant(IDecision<?> decision, Object value, ILPContext ctx) {
        if (decision.getDecisionType() == Decision.DecisionType.BOOLEAN) {
            boolean val = Boolean.parseBoolean(String.valueOf(value));
            return ctx.createConstant(val ? 1.0 : 0.0);
        } else if (decision.getDecisionType() == Decision.DecisionType.STRING) {
            throw new UnsupportedOperationException("String are not supported in ILP.");
        } else {
            return ctx.createConstant(Double.parseDouble(String.valueOf(value)));
        }
    }
}
