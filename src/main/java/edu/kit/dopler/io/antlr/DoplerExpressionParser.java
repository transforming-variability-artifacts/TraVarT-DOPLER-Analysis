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
package edu.kit.dopler.io.antlr;

import edu.kit.dopler.io.antlr.resources.DoplerParser;
import edu.kit.dopler.io.antlr.resources.DoplerParser.*;
import edu.kit.dopler.model.Dopler;
import edu.kit.dopler.model.actions.*;
import edu.kit.dopler.model.basic.Enumeration;
import edu.kit.dopler.model.basic.EnumerationLiteral;
import edu.kit.dopler.model.basic.Rule;
import edu.kit.dopler.model.decisions.BooleanDecision;
import edu.kit.dopler.model.decisions.Decision.DecisionType;
import edu.kit.dopler.model.decisions.EnumerationDecision;
import edu.kit.dopler.model.decisions.IDecision;
import edu.kit.dopler.model.decisions.NumberDecision;
import edu.kit.dopler.model.expressions.*;
import edu.kit.dopler.model.values.BooleanValue;
import edu.kit.dopler.model.values.DoubleValue;
import edu.kit.dopler.model.values.StringValue;
import java.util.*;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.tree.TerminalNode;

public class DoplerExpressionParser extends DecisionParserBase {
    // Stack to traverse Expressions
    Deque<IExpression> expressionStack = new ArrayDeque<>();
    private boolean idSet;
    // Variables of current Rule
    private Set<Rule> currentRules = new HashSet<>();
    // Actions of current Rule
    private Set<IAction> currentActions = new HashSet<>();
    private IExpression currentVisibilityCondition;

    public DoplerExpressionParser(Dopler dopler) {
        this.dopler = dopler;
    }

    @Override
    public Dopler getDopler() {
        return dopler;
    }

    @Override
    public void enterJsonObject(JsonObjectContext ctx) {
        idSet = false;
    }

    @Override
    public void exitJsonObject(JsonObjectContext ctx) {
        setDecisionValues();
    }

    @Override
    public void enterRow(RowContext ctx) {
        idSet = false;
    }

    @Override
    public void exitRow(RowContext ctx) {
        setDecisionValues();
    }

    private void setDecisionValues() {
        IDecision<?> currentDecision = findDecisionByID(currentID);
        if (currentDecision == null) return;
        if (currentVisibilityCondition != null) currentDecision.setVisibilityCondition(currentVisibilityCondition);
        currentRules.forEach(currentDecision::addRule);
        resetValues();
    }

    private void resetValues() {
        currentID = "";
        currentRules.clear();
        currentActions.clear();
        currentVisibilityCondition = null;
    }

    @Override
    public void enterId(IdContext ctx) {
        if (!idSet) {
            currentID = ctx.IDENTIFIER().getText();
            idSet = true;
        } else {
            expressionStack.push(new DecisionValueCallExpression(
                    findDecisionByID(ctx.IDENTIFIER().getText())));
            IDecision<?> decision = findDecisionByID(ctx.IDENTIFIER().getText());
            if (decision != null) {
                expressionStack.push(new DecisionValueCallExpression(decision));
            }
            currentVisibilityCondition = expressionStack.pop();
        }
    }

    @Override
    public void exitUnaryExpression(UnaryExpressionContext ctx) {
        expressionStack.push(new NOT(expressionStack.pop()));
    }

    @Override
    public void enterDecisionVisibilityCallExpression(DecisionVisibilityCallExpressionContext ctx) {
        expressionStack.clear();
    }

    @Override
    public void exitDecisionVisibilityCallExpression(DecisionVisibilityCallExpressionContext ctx) {
        if (ctx.children.size() != 1 && ctx.children.get(1) instanceof TerminalNode operator) {
            IExpression right = expressionStack.pop();
            IExpression left = expressionStack.pop();

            switch (operator.getSymbol().getType()) {
                case DoplerParser.AND:
                    expressionStack.push(new AND(left, right));
                    break;
                case DoplerParser.OR:
                    expressionStack.push(new OR(left, right));
                    break;
                case DoplerParser.EQUALS:
                    expressionStack.push(new Equals(left, right));
                    break;
                case DoplerParser.GREATER_THAN:
                    expressionStack.push(new GreaterThan(left, right));
                    break;
                case DoplerParser.LESS_THAN:
                    expressionStack.push(new LessThan(left, right));
                    break;
            }
        }

        if (!expressionStack.isEmpty()) currentVisibilityCondition = expressionStack.pop();
    }

    @Override
    public void enterIsTaken(IsTakenContext ctx) {
        TerminalNode decisionID = ctx.IDENTIFIER();
        if (decisionID != null) {
            expressionStack.push(new IsTaken(findDecisionByID(decisionID.getText())));
        }
    }

    @Override
    public void enterDecisionValueCallExpression(DecisionValueCallExpressionContext ctx) {
        TerminalNode decisionID = ctx.IDENTIFIER();
        if (decisionID != null) {
            if (findDecisionByID(decisionID.getText()) != null) {
                expressionStack.push(new DecisionValueCallExpression(findDecisionByID(decisionID.getText())));
            } else if (findEnumerationLiteralByName(decisionID.getText()) != null) {
                expressionStack.push(
                        new EnumeratorLiteralExpression(findEnumerationLiteralByName(decisionID.getText())));
            }
        }
    }

    @Override
    public void enterBooleanLiteralExpression(BooleanLiteralExpressionContext ctx) {
        expressionStack.push(new BooleanLiteralExpression(
                Boolean.parseBoolean(ctx.BooleanLiteralExpression().getText())));
    }

    @Override
    public void enterStringLiteralExpression(StringLiteralExpressionContext ctx) {
        expressionStack.push(
                new StringLiteralExpression(ctx.StringLiteralExpression().getText()));
    }

    @Override
    public void enterDoubleLiteralExpression(DoubleLiteralExpressionContext ctx) {
        expressionStack.push(new DoubleLiteralExpression(
                Double.parseDouble(ctx.DoubleLiteralExpression().getText())));
    }

    @Override
    public void enterEnumerationLiteralExpression(EnumerationLiteralExpressionContext ctx) {
        String[] enumerationArray = ctx.EnumerationLiteralExpression().getText().split("\\.");
        if (enumerationArray.length < 2) return;
        IDecision<?> decision = findDecisionByID(enumerationArray[0]);
        if (decision instanceof EnumerationDecision enumerationDecision) {
            for (Enumeration enumeration : dopler.getEnumSet()) {
                Optional<EnumerationLiteral> enumerationLiteral = enumeration.getEnumerationLiterals().stream()
                        .filter(e -> e.getValue().equals(enumerationArray[1]))
                        .findFirst();
                if (enumerationLiteral.isPresent()) {
                    expressionStack.push(new Equals(
                            new DecisionValueCallExpression(enumerationDecision),
                            new EnumeratorLiteralExpression(enumerationLiteral.get())));
                    break;
                }
            }
        }
    }

    @Override
    public void enterDrule(DruleContext ctx) {
        expressionStack.clear();
    }

    @Override
    public void exitDrule(DruleContext ctx) {
        if (expressionStack.isEmpty()) {
            return;
        }
        currentRules.add(new Rule(expressionStack.pop(), new HashSet<>(currentActions)));
        expressionStack.clear();
        currentActions.clear();
    }

    @Override
    public void exitAllows(AllowsContext ctx) {
        String[] enumerationArray = ctx.EnumerationLiteralExpression().getText().split("\\.");
        if (enumerationArray.length > 1) {
            String identifier = enumerationArray[0];
            if (!identifier.isEmpty()) {
                IDecision<?> decision = findDecisionByID(identifier);
                if (decision != null && decision.getDecisionType() == DecisionType.ENUM) {
                    currentActions.add(
                            new Allows((EnumerationDecision) decision, new StringValue(enumerationArray[1])));
                }
            }
        }
    }

    @Override
    public void exitDisallows(DisallowsContext ctx) {
        String[] enumerationArray = ctx.EnumerationLiteralExpression().getText().split("\\.");
        if (enumerationArray.length > 1) {
            String identifier = enumerationArray[0];
            if (!identifier.isEmpty()) {
                IDecision<?> decision = findDecisionByID(identifier);
                if (decision != null && decision.getDecisionType() == DecisionType.ENUM) {
                    currentActions.add(
                            new DisAllows((EnumerationDecision) decision, new StringValue(enumerationArray[1])));
                }
            }
        }
    }

    @Override
    public void enterEnumEnForce(EnumEnForceContext ctx) {
        if (ctx.IDENTIFIER() == null) {
            return;
        }
        String identifier = ctx.IDENTIFIER().getText();
        List<TerminalNode> leafs = ctx.children.subList(2, ctx.children.size()).stream()
                .flatMap(child -> getAllTerminalNodes(child).stream())
                .toList();
        String value = leafs.stream().map(TerminalNode::getText).collect(Collectors.joining());
        if (!identifier.isEmpty()) {
            IDecision<?> decision = findDecisionByID(identifier);
            if (decision != null && decision.getDecisionType() == DecisionType.ENUM) {
                currentActions.add(new EnumEnforce((EnumerationDecision) decision, new StringValue(value)));
            }
        }
    }

    @Override
    public void enterBooleanEnForce(BooleanEnForceContext ctx) {
        String identifier = ctx.IDENTIFIER().getText();
        if (!identifier.isEmpty()) {
            IDecision<?> decision = findDecisionByID(ctx.IDENTIFIER().getText());
            if (decision != null && decision.getDecisionType() == DecisionType.BOOLEAN) {
                boolean value =
                        Boolean.parseBoolean(ctx.BooleanLiteralExpression().getText());
                currentActions.add(new BooleanEnforce((BooleanDecision) decision, new BooleanValue(value)));
            }
        }
    }

    @Override
    public void enterDoubleEnForce(DoubleEnForceContext ctx) {
        String identifier = ctx.IDENTIFIER().getText();
        if (!identifier.isEmpty()) {
            IDecision<?> decision = findDecisionByID(ctx.IDENTIFIER().getText());
            if (decision != null && decision.getDecisionType() == DecisionType.NUMBER) {
                Double value = Double.parseDouble(ctx.DoubleLiteralExpression().getText());
                currentActions.add(new NumberEnforce((NumberDecision) decision, new DoubleValue(value)));
            }
        }
    }

    @Override
    public void exitAndExpression(AndExpressionContext ctx) {
        IExpression right = expressionStack.pop();
        IExpression left = expressionStack.pop();
        expressionStack.push(new AND(left, right));
    }

    @Override
    public void exitOrExpression(OrExpressionContext ctx) {
        IExpression right = expressionStack.pop();
        IExpression left = expressionStack.pop();
        expressionStack.push(new OR(left, right));
    }

    @Override
    public void exitXorExpression(XorExpressionContext ctx) {
        IExpression right = expressionStack.pop();
        IExpression left = expressionStack.pop();
        expressionStack.push(new XOR(left, right));
    }

    @Override
    public void exitEqualityExpression(EqualityExpressionContext ctx) {
        IExpression right = expressionStack.pop();
        IExpression left = expressionStack.pop();
        expressionStack.push(new Equals(left, right));
    }

    @Override
    public void exitGreaterThanExpression(GreaterThanExpressionContext ctx) {
        IExpression right = expressionStack.pop();
        IExpression left = expressionStack.pop();
        expressionStack.push(new GreaterThan(left, right));
    }

    @Override
    public void exitLessThanExpression(LessThanExpressionContext ctx) {
        IExpression right = expressionStack.pop();
        IExpression left = expressionStack.pop();
        expressionStack.push(new LessThan(left, right));
    }
}
