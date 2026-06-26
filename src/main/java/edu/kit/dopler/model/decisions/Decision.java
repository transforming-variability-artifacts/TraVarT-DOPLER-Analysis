/*******************************************************************************
 * SPDX-License-Identifier: MPL-2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/.
 *
 * Copyright 2024 Karlsruhe Institute of Technology (KIT)
 * KASTEL - Dependability of Software-intensive Systems
 *******************************************************************************/
package edu.kit.dopler.model.decisions;

import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.Literal;
import edu.kit.dopler.exceptions.ActionExecutionException;
import edu.kit.dopler.exceptions.EvaluationException;
import edu.kit.dopler.model.actions.IAction;
import edu.kit.dopler.model.basic.Rule;
import edu.kit.dopler.model.expressions.IExpression;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public abstract sealed class Decision<T> implements IDecision<T>
        permits BooleanDecision, EnumerationDecision, ValueDecision {

    private static int uid = 0;
    private final int id;
    private final Set<Rule> rules;
    private String displayId;
    private String question;
    private String description;
    private IExpression visibilityCondition;
    private boolean taken;
    private boolean select;
    private DecisionType decisionType;

    protected Decision(
            String displayId,
            String question,
            String description,
            IExpression visibilityCondition,
            Set<Rule> rules,
            DecisionType decisionType) {
        this.id = uid++;
        this.displayId = displayId;
        this.question = question;
        this.description = description;
        this.visibilityCondition = visibilityCondition;
        this.taken = false;
        this.rules = rules;
        this.decisionType = decisionType;
    }

    @Override
    public String getDisplayId() {
        return displayId;
    }

    @Override
    public void setDisplayId(String displayId) {
        this.displayId = displayId;
    }

    @Override
    public final boolean isSelected() {
        return select;
    }

    @Override
    public final void setSelected(final boolean select) {
        this.select = select;
        setTaken(true);
    }

    @Override
    public String getQuestion() {
        return question;
    }

    @Override
    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Set<Rule> getRules() {
        return rules;
    }

    @Override
    public void addRule(Rule rule) {
        rules.add(rule);
    }

    @Override
    public void removeRule(Rule rule) {
        rules.remove(rule);
    }

    @Override
    public void executeRules() throws ActionExecutionException, EvaluationException {
        for (Rule rule : rules) {
            rule.executeActions();
        }
    }

    @Override
    public IExpression getVisibilityCondition() {
        return visibilityCondition;
    }

    @Override
    public void setVisibilityCondition(IExpression visibilityCondition) {
        this.visibilityCondition = visibilityCondition;
    }

    public boolean isVisible() throws EvaluationException {
        return visibilityCondition.evaluate();
    }

    @Override
    public void mapLogicToConstraintsInCp(
            CpModel model,
            Map<IDecision<?>, List<IntVar>> decisionVars,
            Map<IDecision<?>, Literal> isTakenVars,
            Map<IDecision<?>, List<Literal>> isTakenConditions) {
        this.mapRulesToCp(model, decisionVars, isTakenVars, isTakenConditions); // map rules to CP (= add constraints,
        // representing the rules and their
        // actions, to the model and fill the
        // isTakenConditions map, which will
        // then contain literals, each
        // indicating whether a rule-action did
        // enforce the value of a decision or
        // not)

        this.enforceStandardValueInCp(model, decisionVars, isTakenVars); // adds constraints that enforce a standard
        // value for a decision if necessary (= if it
        // is not taken)
    }

    /**
     * Maps the rules associated with the current decisions into the CP model. This
     * involves adding constraints, representing the rules and their actions, to the
     * model. Additionally, the isTakenConditions map gets filled with CP literals,
     * each indicating whether a rule-action did enforce the value of a decision or
     * not
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
    private void mapRulesToCp(
            CpModel model,
            Map<IDecision<?>, List<IntVar>> decisionVars,
            Map<IDecision<?>, Literal> isTakenVars,
            Map<IDecision<?>, List<Literal>> isTakenConditions) {
        for (Rule rule : this.rules) {
            Literal ruleCondtionLiteral = rule.getCondition().toCpLiteral(model, decisionVars, isTakenVars);

            Literal conditionLiteral = model.newBoolVar("decisionTaken_AND_ruleCondition");
            // ensure that: conditionLiteral <=> (isTakenVars.get(this) and
            // ruleCondtionLiteral)
            // =>
            model.addImplication(conditionLiteral, isTakenVars.get(this));
            model.addImplication(conditionLiteral, ruleCondtionLiteral);
            // <=
            model.addBoolOr(new Literal[] {isTakenVars.get(this).not(), ruleCondtionLiteral.not(), conditionLiteral});

            for (IAction action : rule.getActions()) {
                action.addCpConstraints(model, conditionLiteral, decisionVars, isTakenVars, isTakenConditions);
            }
        }
    }

    /**
     * Adds constraints that enforce a standard (default) value for the current
     * decision if necessary (= if it is not taken).
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
     */
    protected abstract void enforceStandardValueInCp(
            CpModel model, Map<IDecision<?>, List<IntVar>> decisionVars, Map<IDecision<?>, Literal> isTakenVars);

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Decision<?> decision = (Decision<?>) o;
        return Objects.equals(this.displayId, decision.displayId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.displayId);
    }

    @Override
    public boolean isTaken() {
        return taken;
    }

    @Override
    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    public DecisionType getDecisionType() {
        return decisionType;
    }

    public void setDecisionType(DecisionType decisionType) {
        this.decisionType = decisionType;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return displayId;
    }

    public enum DecisionType {
        BOOLEAN("Boolean"),
        NUMBER("Double"),
        STRING("String"),
        ENUM("Enumeration");

        private final String type;

        DecisionType(final String type) {
            this.type = type;
        }

        public boolean equalString(final String type) {
            return this.type.equals(type);
        }

        @Override
        public String toString() {
            return type;
        }
    }
}
