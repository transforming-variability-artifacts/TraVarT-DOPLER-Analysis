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

import edu.kit.dopler.model.actions.*;
import edu.kit.dopler.model.decisions.Decision;
import edu.kit.dopler.model.decisions.IDecision;
import edu.kit.dopler.solvers.shared.SolverUtils;
import edu.kit.dopler.solvers.smt.SMTConstants;
import edu.kit.dopler.solvers.smt.SMTContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sosy_lab.java_smt.api.*;

public final class SMTActionEncoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(SMTActionEncoder.class);

    private SMTActionEncoder() {
        throw new IllegalStateException("Utility class can't be instantiated");
    }

    /**
     * Encodes a single action triggered by a rule into the SMT context.
     *
     * @param action     The action to encode (e.g., DisAllows, Enforce).
     * @param activation The boolean formula representing the condition under which this action fires.
     * @param context    The active SMT context holding variables and formula managers.
     * @throws IllegalStateException if required SMT variables for the target decision are missing.
     */
    public static void encodeAction(IAction action, BooleanFormula activation, SMTContext context) {
        switch (action) {
            case Allows ignored -> {
                // No-op
            }
            case DisAllows disAllows -> {
                FormulaManager fm = context.fm();
                BooleanFormulaManager bfm = fm.getBooleanFormulaManager();

                String targetConst = SolverUtils.toStringConst(disAllows.getDecision());
                IDecision<?> decision = disAllows.getDecision();

                BooleanFormula disallowExpr = null;

                // Check if the DisAllows targets a specific Enum literal
                if (decision.getDecisionType() == Decision.DecisionType.ENUM && disAllows.getDisAllowValue() != null) {
                    BooleanFormula enumVar = (BooleanFormula) context.getVar(
                            targetConst + "_" + disAllows.getDisAllowValue().getValue());
                    if (enumVar != null) {
                        disallowExpr = bfm.not(enumVar);
                    } else {
                        LOGGER.warn(
                                "DisAllows targets missing enum variable: {}_{}",
                                targetConst,
                                disAllows.getDisAllowValue().getValue());
                    }
                } else if (disAllows.getDisAllowValue() != null) {
                    Formula valVar = context.getVar(targetConst + SMTConstants.VALUE_SUFFIX);

                    if (valVar != null) {
                        Formula smtVal = SMTExpressionEncoder.convertToSMTExpr(
                                context, disAllows.getDisAllowValue().getValue(), fm.getFormulaType(valVar));

                        if (decision.getDecisionType() == Decision.DecisionType.BOOLEAN) {
                            boolean disallowedBool = Boolean.parseBoolean(
                                    String.valueOf(disAllows.getDisAllowValue().getValue()));
                            disallowExpr = disallowedBool ? bfm.not((BooleanFormula) valVar) : (BooleanFormula) valVar;
                        } else if (decision.getDecisionType() == Decision.DecisionType.NUMBER) {
                            disallowExpr = bfm.not(fm.getRationalFormulaManager()
                                    .equal((NumeralFormula.RationalFormula) valVar, (NumeralFormula.RationalFormula)
                                            smtVal));
                        } else if (decision.getDecisionType() == Decision.DecisionType.STRING) {
                            disallowExpr = bfm.not(
                                    fm.getStringFormulaManager().equal((StringFormula) valVar, (StringFormula) smtVal));
                        }
                    } else {
                        LOGGER.warn(
                                "DisAllows targets missing value variable: {}",
                                targetConst + SMTConstants.VALUE_SUFFIX);
                    }
                } else {
                    BooleanFormula targetIsTaken =
                            (BooleanFormula) context.getVar(targetConst + SMTConstants.TAKEN_SUFFIX);
                    if (targetIsTaken != null) {
                        disallowExpr = bfm.not(targetIsTaken);
                    } else {
                        LOGGER.warn(
                                "DisAllows targets missing taken variable: {}",
                                targetConst + SMTConstants.TAKEN_SUFFIX);
                    }
                }

                if (disallowExpr != null) {
                    context.addClause(bfm.implication(activation, disallowExpr));
                }
            }
            case Enforce enforce -> {
                FormulaManager fm = context.fm();
                BooleanFormulaManager bfm = fm.getBooleanFormulaManager();
                String targetConst = SolverUtils.toStringConst(enforce.getDecision());
                IDecision<?> decision = enforce.getDecision();

                BooleanFormula enforceValueExpr = null;

                if (decision.getDecisionType() == Decision.DecisionType.ENUM) {
                    // For enums, enforce means the specific literal boolean becomes true
                    enforceValueExpr = (BooleanFormula) context.getVar(
                            targetConst + "_" + enforce.getValue().getValue());
                } else {
                    // For Number/String/Bool, enforce means the value var equals the requested value
                    Formula valVar = context.getVar(targetConst + SMTConstants.VALUE_SUFFIX);

                    Formula smtVal = SMTExpressionEncoder.convertToSMTExpr(
                            context, enforce.getValue().getValue(), fm.getFormulaType(valVar));

                    if (decision.getDecisionType() == Decision.DecisionType.BOOLEAN) {
                        enforceValueExpr = bfm.equivalence((BooleanFormula) valVar, (BooleanFormula) smtVal);
                    } else if (decision.getDecisionType() == Decision.DecisionType.NUMBER) {
                        enforceValueExpr = fm.getRationalFormulaManager()
                                .equal((NumeralFormula.RationalFormula) valVar, (NumeralFormula.RationalFormula)
                                        smtVal);
                    } else if (decision.getDecisionType() == Decision.DecisionType.STRING) {
                        enforceValueExpr =
                                fm.getStringFormulaManager().equal((StringFormula) valVar, (StringFormula) smtVal);
                    }
                }

                if (enforceValueExpr != null) {
                    // Assert the value change: Trigger => (TargetVar == Value)
                    context.addClause(bfm.implication(activation, enforceValueExpr));
                }

                // Register this trigger as a reason the target decision is Taken
                context.addIsTakenConditions(decision, activation);
            }
            case JavaAction ignored -> {
                LOGGER.warn("JavaAction is currently not implemented and will be ignored in SMT encoding.");
            }
        }
    }
}
