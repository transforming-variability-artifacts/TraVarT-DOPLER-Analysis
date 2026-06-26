/*******************************************************************************
 * SPDX-License-Identifier: MPL-2.0
 *
 * Copyright (c) 2024 Karlsruhe Institute of Technology (KIT)
 * KASTEL - Dependability of Software-intensive Systems
 *
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *******************************************************************************/
package edu.kit.dopler.model.decisions;

import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.Literal;
import edu.kit.dopler.exceptions.ActionExecutionException;
import edu.kit.dopler.exceptions.EvaluationException;
import edu.kit.dopler.exceptions.ValidityConditionException;
import edu.kit.dopler.model.basic.Rule;
import edu.kit.dopler.model.expressions.IExpression;
import edu.kit.dopler.model.values.IValue;
import java.util.List;
import java.util.Map;
import java.util.Set;

public sealed interface IDecision<T> permits Decision {

    String getDisplayId();

    void setDisplayId(String displayId);

    String getQuestion();

    void setQuestion(String question);

    String getDescription();

    void setDescription(String description);

    Set<Rule> getRules();

    void addRule(Rule rule);

    void removeRule(Rule rule);

    void executeRules() throws ActionExecutionException, EvaluationException;

    T getStandardValue();

    IValue<T> getValue();

    void setValue(IValue<T> value) throws ValidityConditionException;

    boolean isSelected();

    void setSelected(final boolean select);

    IExpression getVisibilityCondition();

    void setVisibilityCondition(IExpression visibilityCondition);

    boolean isVisible() throws EvaluationException;

    boolean isTaken();

    void setTaken(boolean taken);

    Decision.DecisionType getDecisionType();

    default Set<IExpression> getValidityConditions() {
        return Set.of();
    }

    /**
     * Creates CP variable(s) representing the current decision. The variables are
     * added to the CP model and stored in the decisionVars map.
     *
     * @param model
     *            the constraint programming model to which the variables will be
     *            added
     * @param decisionVars
     *            a map associating each decision of a dopler model with a list of
     *            CP variables representing it
     * @param isTakenVars
     *            a map associating each decision of a dopler model with a boolean
     *            literal indicating whether the decision is taken
     */
    void createCpDecisionVariables(
            CpModel model, Map<IDecision<?>, List<IntVar>> decisionVars, Map<IDecision<?>, Literal> isTakenVars);

    /**
     * Adds constraints that map model-level logic (rules, standard values, and
     * validity conditions) to CP constraints
     *
     * @param model
     *            the constraint programming model to which the constraints will be
     *            added
     * @param decisionVars
     *            a map associating each decision of a dopler model with a list of
     *            CP variables representing it
     * @param isTakenVars
     *            a map associating each decision of a dopler model with a boolean
     *            literal indicating whether the decision is taken
     * @param isTakenConditions
     *            a (helper) map associating each decision of a dopler model with a
     *            list of boolean literals that can later be used to add constraints
     *            for isTakenVars to be logically correct in the model
     */
    void mapLogicToConstraintsInCp(
            CpModel model,
            Map<IDecision<?>, List<IntVar>> decisionVars,
            Map<IDecision<?>, Literal> isTakenVars,
            Map<IDecision<?>, List<Literal>> isTakenConditions);
}
