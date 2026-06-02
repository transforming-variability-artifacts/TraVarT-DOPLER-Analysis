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
package edu.kit.dopler.solvers.ilp.encoders;

import com.google.ortools.linearsolver.MPVariable;
import edu.kit.dopler.model.actions.IAction;
import edu.kit.dopler.model.basic.Rule;
import edu.kit.dopler.model.decisions.IDecision;
import edu.kit.dopler.model.expressions.IExpression;
import edu.kit.dopler.solvers.ilp.ILPConstants;
import edu.kit.dopler.solvers.ilp.ILPContext;
import edu.kit.dopler.solvers.ilp.utils.ILPLogic;
import edu.kit.dopler.solvers.shared.SolverUtils;
import edu.kit.dopler.solvers.shared.encoders.AbstractGlobalDecisionEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ILPGlobalDecisionEncoder extends AbstractGlobalDecisionEncoder<ILPContext, MPVariable> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ILPGlobalDecisionEncoder.class);
    private static final ILPGlobalDecisionEncoder INSTANCE = new ILPGlobalDecisionEncoder();

    private ILPGlobalDecisionEncoder() {}

    public static void mapLogicToConstraints(IDecision<?> decision, ILPContext ctx) {
        INSTANCE.mapLogicToConstraintsInternal(decision, ctx);
    }

    @Override
    protected MPVariable getTakenVariable(IDecision<?> decision, ILPContext ctx) {
        return ctx.getCoreVar(SolverUtils.toStringConst(decision) + ILPConstants.TAKEN_SUFFIX);
    }

    @Override
    protected void mapRules(IDecision<?> decision, MPVariable isTaken, ILPContext ctx) {
        Set<Rule> rules = decision.getRules();
        if (rules == null || rules.isEmpty()) return;

        for (Rule rule : rules) {
            if (rule.getActions() == null) continue;

            MPVariable ruleCondition = ILPExpressionEncoder.encodeExpression(rule.getCondition(), ctx);

            // activation = isTaken AND ruleConditions
            MPVariable activation = ILPLogic.and(ctx, isTaken, ruleCondition);

            // Actions apply their constraints based on the activation trigger
            for (IAction action : rule.getActions()) {
                ILPActionEncoder.encodeAction(action, activation, ctx);
            }
        }
    }

    @Override
    protected void enforceValidity(IDecision<?> decision, MPVariable isTaken, ILPContext ctx) {
        Set<IExpression> validationConditions = decision.getValidityConditions();
        if (validationConditions == null || validationConditions.isEmpty()) return;

        List<MPVariable> validityVars = new ArrayList<>();
        for (IExpression expression : validationConditions) {
            validityVars.add(ILPExpressionEncoder.encodeExpression(expression, ctx));
        }

        MPVariable combinedValidity = ILPLogic.and(ctx, validityVars.toArray(new MPVariable[0]));

        // isTaken => all conditions fulfilled
        ILPLogic.implies(ctx, isTaken, combinedValidity);
    }

    @Override
    protected void enforceDefaultValue(IDecision<?> decision, MPVariable isTaken, ILPContext ctx) {
        MPVariable defaultValuesMet = ILPDecisionEncoder.encodeDecision(decision, ctx);
        if (defaultValuesMet != null) {
            // !isTaken => defaultValue
            MPVariable notTaken = ILPLogic.not(ctx, isTaken);
            ILPLogic.implies(ctx, notTaken, defaultValuesMet);
        } else {
            LOGGER.warn(
                    "Could not encode standard values for decision: {}. Default state will be unconstrained.",
                    decision.getDisplayId());
        }
    }
}
