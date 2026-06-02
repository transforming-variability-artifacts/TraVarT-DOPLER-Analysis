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
package edu.kit.dopler.solvers.smt.encoders;

import edu.kit.dopler.model.actions.IAction;
import edu.kit.dopler.model.basic.Rule;
import edu.kit.dopler.model.decisions.IDecision;
import edu.kit.dopler.model.expressions.IExpression;
import edu.kit.dopler.solvers.shared.SolverUtils;
import edu.kit.dopler.solvers.shared.encoders.AbstractGlobalDecisionEncoder;
import edu.kit.dopler.solvers.smt.SMTConstants;
import edu.kit.dopler.solvers.smt.SMTContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sosy_lab.java_smt.api.BooleanFormula;
import org.sosy_lab.java_smt.api.BooleanFormulaManager;
import org.sosy_lab.java_smt.api.FormulaManager;

public final class SMTGlobalDecisionEncoder extends AbstractGlobalDecisionEncoder<SMTContext, BooleanFormula> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SMTGlobalDecisionEncoder.class);
    private static final SMTGlobalDecisionEncoder INSTANCE = new SMTGlobalDecisionEncoder();

    private SMTGlobalDecisionEncoder() {}

    public static void mapLogicToConstraints(IDecision<?> decision, SMTContext smtContext) {
        INSTANCE.mapLogicToConstraintsInternal(decision, smtContext);
    }

    @Override
    protected BooleanFormula getTakenVariable(IDecision<?> decision, SMTContext smtContext) {
        return (BooleanFormula) smtContext.getVar(SolverUtils.toStringConst(decision) + SMTConstants.TAKEN_SUFFIX);
    }

    @Override
    protected void mapRules(IDecision<?> decision, BooleanFormula isTaken, SMTContext smtContext) {
        // SMT Implementation: Uses Z3's native bfm.and mapping for condition activation. Includes standard SLF4J
        // logging.
        Set<Rule> rules = decision.getRules();
        if (rules == null || rules.isEmpty()) {
            LOGGER.trace("No rules to map for decision: {}", decision.getDisplayId());
            return;
        }

        FormulaManager fm = smtContext.fm();
        BooleanFormulaManager bfm = fm.getBooleanFormulaManager();

        for (Rule rule : rules) {
            BooleanFormula ruleCondition = (rule.getCondition() == null)
                    ? bfm.makeTrue()
                    : (BooleanFormula) SMTExpressionEncoder.encodeExpression(rule.getCondition(), smtContext);

            // activation = isTaken AND ruleConditions
            BooleanFormula activation = bfm.and(isTaken, ruleCondition);

            // Actions apply their constraints based on the activation trigger
            for (IAction action : rule.getActions()) {
                SMTActionEncoder.encodeAction(action, activation, smtContext);
            }
        }
    }

    @Override
    protected void enforceValidity(IDecision<?> decision, BooleanFormula isTaken, SMTContext smtContext) {
        Set<IExpression> validationConditions = decision.getValidityConditions();
        if (validationConditions == null || validationConditions.isEmpty()) {
            LOGGER.trace("No validity conditions to enforce for decision: {}", decision.getDisplayId());
            return;
        }

        FormulaManager fm = smtContext.fm();
        BooleanFormulaManager bfm = fm.getBooleanFormulaManager();

        List<BooleanFormula> validityExpressions = new ArrayList<>();
        for (IExpression expression : validationConditions) {
            validityExpressions.add((BooleanFormula) SMTExpressionEncoder.encodeExpression(expression, smtContext));
        }
        BooleanFormula combinedValidity = bfm.and(validityExpressions);

        // isTaken => all conditions fulfilled
        smtContext.addClause(bfm.implication(isTaken, combinedValidity));
    }

    @Override
    protected void enforceDefaultValue(IDecision<?> decision, BooleanFormula isTaken, SMTContext smtContext) {
        FormulaManager fm = smtContext.fm();
        BooleanFormulaManager bfm = fm.getBooleanFormulaManager();
        BooleanFormula defaultValues = (BooleanFormula) SMTDecisionEncoder.encodeDecision(decision, smtContext);

        if (defaultValues != null) {
            // !isTaken => defaultValue
            smtContext.addClause(bfm.implication(bfm.not(isTaken), defaultValues));
        } else {
            LOGGER.warn(
                    "Could not encode standard values for decision: {}. Default state will be unconstrained.",
                    decision.getDisplayId());
        }
    }
}
