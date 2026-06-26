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

import edu.kit.dopler.model.Dopler;
import edu.kit.dopler.model.basic.EnumerationLiteral;
import edu.kit.dopler.model.decisions.Decision;
import edu.kit.dopler.model.decisions.EnumerationDecision;
import edu.kit.dopler.model.decisions.IDecision;
import edu.kit.dopler.solvers.shared.SolverUtils;
import edu.kit.dopler.solvers.shared.encoders.AbstractGlobalConstraintEncoder;
import edu.kit.dopler.solvers.smt.SMTConstants;
import edu.kit.dopler.solvers.smt.SMTContext;
import java.util.ArrayList;
import java.util.List;
import org.sosy_lab.java_smt.api.*;

public final class SMTGlobalConstraintEncoder extends AbstractGlobalConstraintEncoder<SMTContext> {

    private static final SMTGlobalConstraintEncoder INSTANCE = new SMTGlobalConstraintEncoder();

    private SMTGlobalConstraintEncoder() {}

    public static void encodeToSMT(Dopler dopler, SMTContext smtContext) {
        INSTANCE.encode(dopler, smtContext);
    }

    @Override
    protected void createVariables(Dopler dopler, SMTContext smtContext) {
        FormulaManager fm = smtContext.fm();
        BooleanFormulaManager bfm = fm.getBooleanFormulaManager();

        for (IDecision<?> decision : dopler.getDecisions()) {
            String baseName = SolverUtils.toStringConst(decision);
            smtContext.putVar(
                    baseName + SMTConstants.TAKEN_SUFFIX, bfm.makeVariable(baseName + SMTConstants.TAKEN_SUFFIX));

            if (decision.getDecisionType() == Decision.DecisionType.ENUM) {
                EnumerationDecision enumDec = (EnumerationDecision) decision;
                // Each literal is a separate boolean variable
                for (EnumerationLiteral literal : enumDec.getEnumeration().getEnumerationLiterals()) {
                    String litName = baseName + "_" + literal.getValue();
                    smtContext.putVar(litName, bfm.makeVariable(litName));
                }
            } else {
                FormulaType<?> sort =
                        switch (decision.getDecisionType()) {
                            case BOOLEAN -> FormulaType.BooleanType;
                            case NUMBER -> FormulaType.RationalType;
                            case STRING -> FormulaType.StringType;
                            case ENUM -> throw new IllegalStateException("Should not be called for type ENUM");
                        };
                smtContext.putVar(
                        baseName + SMTConstants.VALUE_SUFFIX,
                        fm.makeVariable(sort, baseName + SMTConstants.VALUE_SUFFIX));
            }
        }
    }

    @Override
    protected void mapDecisionLogic(IDecision<?> decision, SMTContext smtContext) {
        SMTGlobalDecisionEncoder.mapLogicToConstraints(decision, smtContext);
    }

    @Override
    protected void enforceVisibilityConsistency(Dopler dopler, SMTContext smtContext) {
        FormulaManager fm = smtContext.fm();
        BooleanFormulaManager bfm = fm.getBooleanFormulaManager();

        for (IDecision<?> decision : dopler.getDecisions()) {
            String baseName = SolverUtils.toStringConst(decision);
            BooleanFormula isTaken = (BooleanFormula) smtContext.getVar(baseName + SMTConstants.TAKEN_SUFFIX);
            BooleanFormula isVisible = (BooleanFormula)
                    SMTExpressionEncoder.encodeExpression(decision.getVisibilityCondition(), smtContext);

            List<BooleanFormula> triggers = new ArrayList<>();
            triggers.add(isVisible);
            triggers.addAll(smtContext.getIsTakenConditions(decision));

            BooleanFormula anyTrigger = bfm.or(triggers);
            // isTaken is true if and only if visibility or rules trigger it
            smtContext.addClause(bfm.equivalence(isTaken, anyTrigger));
        }
    }

    @Override
    protected void applyEnumCardinality(Dopler dopler, SMTContext smtContext) {
        FormulaManager fm = smtContext.fm();
        BooleanFormulaManager bfm = fm.getBooleanFormulaManager();
        IntegerFormulaManager ifm = fm.getIntegerFormulaManager();

        for (IDecision<?> decision : dopler.getDecisions()) {
            if (decision.getDecisionType() == Decision.DecisionType.ENUM) {
                EnumerationDecision enumDecision = (EnumerationDecision) decision;
                String baseName = SolverUtils.toStringConst(decision);
                BooleanFormula isTaken = (BooleanFormula) smtContext.getVar(baseName + SMTConstants.TAKEN_SUFFIX);

                NumeralFormula.IntegerFormula sum = ifm.makeNumber(0);
                for (EnumerationLiteral literal : enumDecision.getEnumeration().getEnumerationLiterals()) {
                    BooleanFormula litVar = (BooleanFormula) smtContext.getVar(baseName + "_" + literal.getValue());
                    // Convert Bool to 1/0 for summation
                    NumeralFormula.IntegerFormula val = bfm.ifThenElse(litVar, ifm.makeNumber(1), ifm.makeNumber(0));
                    sum = ifm.add(sum, val);
                }

                // Define the valid range
                BooleanFormula cardinalityValid = bfm.and(
                        ifm.greaterOrEquals(sum, ifm.makeNumber(enumDecision.getMinCardinality())),
                        ifm.lessOrEquals(sum, ifm.makeNumber(enumDecision.getMaxCardinality())));

                // isTaken => min <= sum <= max
                smtContext.addClause(bfm.implication(isTaken, cardinalityValid));
                // !isTaken => sum == 0 (all literals must be false)
                smtContext.addClause(bfm.implication(bfm.not(isTaken), ifm.equal(sum, ifm.makeNumber(0))));
            }
        }
    }
}
