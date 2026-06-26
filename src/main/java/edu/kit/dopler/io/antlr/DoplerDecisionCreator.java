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

import edu.kit.dopler.exceptions.InvalidCardinalityException;
import edu.kit.dopler.io.antlr.resources.DoplerLexer;
import edu.kit.dopler.io.antlr.resources.DoplerParser.*;
import edu.kit.dopler.model.Dopler;
import edu.kit.dopler.model.basic.Enumeration;
import edu.kit.dopler.model.basic.EnumerationLiteral;
import edu.kit.dopler.model.decisions.*;
import edu.kit.dopler.model.expressions.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public class DoplerDecisionCreator extends DecisionParserBase {

    private boolean csvFile = false;

    public DoplerDecisionCreator(String name) {
        dopler = new Dopler();
        dopler.setName(name);
    }

    @Override
    public void enterCsvFile(CsvFileContext ctx) {
        csvFile = true;
    }

    @Override
    public void enterId(IdContext ctx) {
        if ((matchesColumn(ctx, column_ID) && ctx.IDENTIFIER() != null) || !csvFile) {
            currentID = ctx.IDENTIFIER().getText();
        }
    }

    @Override
    public void enterCardinality(CardinalityContext ctx) {
        IDecision<?> currentDecision = findDecisionByID(currentID);
        List<TerminalNode> cardinals = ctx.DoubleLiteralExpression();
        if (cardinals.size() >= 2) {
            if (currentDecision instanceof EnumerationDecision) {
                EnumerationDecision enumerationDecision = (EnumerationDecision) currentDecision;
                try {
                    enumerationDecision.setCardinality(
                            Integer.parseInt(cardinals.get(0).getText()),
                            Integer.parseInt(cardinals.get(1).getText()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (InvalidCardinalityException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void enterQuestion(QuestionContext ctx) {
        if (ctx.QUESTION() != null) {
            currentQuestion = ctx.QUESTION().getSymbol().getText();
        }
    }

    @Override
    public void enterRange(RangeContext ctx) {
        IDecision<?> currentDecision = findDecisionByID(currentID);
        switch (currentDecision.getDecisionType()) {
            case BOOLEAN:
                return;
            case STRING:
                // The same way as in NUMBER trough ValidityConditions
                break;
            case NUMBER:
                List<TerminalNode> AllExpressions = ctx.DoubleLiteralExpression();
                Set<IExpression> currentValidityConditions = new HashSet<>();
                NumberDecision numberDecision = (NumberDecision) currentDecision;
                if (AllExpressions.size() % 2 == 0) {
                    for (int i = 0; i < AllExpressions.size(); i += 2) {
                        TerminalNode left = ctx.DoubleLiteralExpression(i);
                        TerminalNode right = ctx.DoubleLiteralExpression(i + 1);
                        currentValidityConditions.add(new OR(
                                new LessThan(
                                        new DoubleLiteralExpression(Double.parseDouble(left.getText())),
                                        new DecisionValueCallExpression(currentDecision)),
                                new Equals(
                                        new DoubleLiteralExpression(Double.parseDouble(left.getText())),
                                        new DecisionValueCallExpression(currentDecision))));
                        currentValidityConditions.add(new OR(
                                new GreaterThan(
                                        new DoubleLiteralExpression(Double.parseDouble(right.getText())),
                                        new DecisionValueCallExpression(currentDecision)),
                                new Equals(
                                        new DoubleLiteralExpression(Double.parseDouble(right.getText())),
                                        new DecisionValueCallExpression(currentDecision))));
                    }
                }
                numberDecision.setValidityConditions(currentValidityConditions);
                break;
            case ENUM:
                List<ParseTree> children = ctx.children;
                List<TerminalNode> leafs = new ArrayList<>();
                for (ParseTree child : children) {
                    leafs.addAll(getAllTerminalNodes(child));
                }

                Set<EnumerationLiteral> enumerationLiterals = new HashSet<>();
                String currentLiteral = "";
                for (int i = 0; i < leafs.size(); i++) {
                    if (leafs.get(i).getSymbol().getType() == DoplerLexer.PIPE) {
                        enumerationLiterals.add(new EnumerationLiteral(currentLiteral));
                        currentLiteral = "";
                    } else {
                        currentLiteral += leafs.get(i).getSymbol().getText();
                    }
                }
                enumerationLiterals.add(new EnumerationLiteral(currentLiteral));
                Enumeration enumeration = new Enumeration(enumerationLiterals);
                EnumerationDecision enumerationDecision = (EnumerationDecision) currentDecision;
                enumerationDecision.setEnumeration(enumeration);
                dopler.addEnum(enumeration);
        }
    }

    @Override
    public void enterDecisionType(DecisionTypeContext ctx) {
        for (ParseTree child : ctx.children) {
            if (child instanceof TerminalNode) {
                TerminalNode node = (TerminalNode) child;
                switch (node.getSymbol().getType()) {
                    case DoplerLexer.NumberDecision:
                        dopler.addDecision(new NumberDecision(
                                currentID,
                                currentQuestion,
                                currentDescription,
                                new BooleanLiteralExpression(true),
                                new HashSet<>(),
                                new HashSet<>()));
                        break;
                    case DoplerLexer.EnumerationDecision:
                        dopler.addDecision(new EnumerationDecision(
                                currentID,
                                currentQuestion,
                                currentDescription,
                                new BooleanLiteralExpression(true),
                                new HashSet<>(),
                                null,
                                1,
                                1));
                        break;
                    case DoplerLexer.BooleanDecision:
                        dopler.addDecision(new BooleanDecision(
                                currentID,
                                currentQuestion,
                                currentDescription,
                                new BooleanLiteralExpression(true),
                                new HashSet<>()));
                        break;
                    case DoplerLexer.StringDecision:
                        dopler.addDecision(new StringDecision(
                                currentID,
                                currentQuestion,
                                currentDescription,
                                new BooleanLiteralExpression(true),
                                new HashSet<>(),
                                new HashSet<>()));
                        break;
                }
            }
        }
    }
}
