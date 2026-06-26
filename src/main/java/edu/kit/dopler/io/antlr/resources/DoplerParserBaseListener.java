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
package edu.kit.dopler.io.antlr.resources;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * This class provides an empty implementation of {@link DoplerParserListener},
 * which can be extended to create a listener which only needs to handle a
 * subset of the available methods.
 */
@SuppressWarnings("CheckReturnValue")
public class DoplerParserBaseListener implements DoplerParserListener {
    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterDocument(DoplerParser.DocumentContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitDocument(DoplerParser.DocumentContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterJsonDocument(DoplerParser.JsonDocumentContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitJsonDocument(DoplerParser.JsonDocumentContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterJsonValue(DoplerParser.JsonValueContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitJsonValue(DoplerParser.JsonValueContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterJsonObject(DoplerParser.JsonObjectContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitJsonObject(DoplerParser.JsonObjectContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterJsonPair(DoplerParser.JsonPairContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitJsonPair(DoplerParser.JsonPairContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterCsvFile(DoplerParser.CsvFileContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitCsvFile(DoplerParser.CsvFileContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterHdr(DoplerParser.HdrContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitHdr(DoplerParser.HdrContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterRow(DoplerParser.RowContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitRow(DoplerParser.RowContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterField(DoplerParser.FieldContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitField(DoplerParser.FieldContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterHeader(DoplerParser.HeaderContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitHeader(DoplerParser.HeaderContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterId(DoplerParser.IdContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitId(DoplerParser.IdContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterCardinality(DoplerParser.CardinalityContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitCardinality(DoplerParser.CardinalityContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterQuestion(DoplerParser.QuestionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitQuestion(DoplerParser.QuestionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterRange(DoplerParser.RangeContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitRange(DoplerParser.RangeContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterRangeItem(DoplerParser.RangeItemContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitRangeItem(DoplerParser.RangeItemContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterSpecialCharacter(DoplerParser.SpecialCharacterContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitSpecialCharacter(DoplerParser.SpecialCharacterContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterSubrange(DoplerParser.SubrangeContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitSubrange(DoplerParser.SubrangeContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterExpression(DoplerParser.ExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitExpression(DoplerParser.ExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterUnaryExpression(DoplerParser.UnaryExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitUnaryExpression(DoplerParser.UnaryExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterDecisionVisibilityCallExpression(DoplerParser.DecisionVisibilityCallExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitDecisionVisibilityCallExpression(DoplerParser.DecisionVisibilityCallExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterIsTaken(DoplerParser.IsTakenContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitIsTaken(DoplerParser.IsTakenContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterDecisionValueCallExpression(DoplerParser.DecisionValueCallExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitDecisionValueCallExpression(DoplerParser.DecisionValueCallExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterBinaryExpression(DoplerParser.BinaryExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitBinaryExpression(DoplerParser.BinaryExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterAndExpression(DoplerParser.AndExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitAndExpression(DoplerParser.AndExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterOrExpression(DoplerParser.OrExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitOrExpression(DoplerParser.OrExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterXorExpression(DoplerParser.XorExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitXorExpression(DoplerParser.XorExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterEqualityExpression(DoplerParser.EqualityExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitEqualityExpression(DoplerParser.EqualityExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterGreaterThanExpression(DoplerParser.GreaterThanExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitGreaterThanExpression(DoplerParser.GreaterThanExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterLessThanExpression(DoplerParser.LessThanExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitLessThanExpression(DoplerParser.LessThanExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterGreaterEqualsExpression(DoplerParser.GreaterEqualsExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitGreaterEqualsExpression(DoplerParser.GreaterEqualsExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterLessEqualsExpression(DoplerParser.LessEqualsExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitLessEqualsExpression(DoplerParser.LessEqualsExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterLiteralExpression(DoplerParser.LiteralExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitLiteralExpression(DoplerParser.LiteralExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterEnumerationLiteralExpression(DoplerParser.EnumerationLiteralExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitEnumerationLiteralExpression(DoplerParser.EnumerationLiteralExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterBooleanLiteralExpression(DoplerParser.BooleanLiteralExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitBooleanLiteralExpression(DoplerParser.BooleanLiteralExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterStringLiteralExpression(DoplerParser.StringLiteralExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitStringLiteralExpression(DoplerParser.StringLiteralExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterDoubleLiteralExpression(DoplerParser.DoubleLiteralExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitDoubleLiteralExpression(DoplerParser.DoubleLiteralExpressionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterDecisionType(DoplerParser.DecisionTypeContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitDecisionType(DoplerParser.DecisionTypeContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterDrule(DoplerParser.DruleContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitDrule(DoplerParser.DruleContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterAction(DoplerParser.ActionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitAction(DoplerParser.ActionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterValueRestrictionAction(DoplerParser.ValueRestrictionActionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitValueRestrictionAction(DoplerParser.ValueRestrictionActionContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterAllows(DoplerParser.AllowsContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitAllows(DoplerParser.AllowsContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterDisallows(DoplerParser.DisallowsContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitDisallows(DoplerParser.DisallowsContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterEnForce(DoplerParser.EnForceContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitEnForce(DoplerParser.EnForceContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterEnumEnForce(DoplerParser.EnumEnForceContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitEnumEnForce(DoplerParser.EnumEnForceContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterStringEnForce(DoplerParser.StringEnForceContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitStringEnForce(DoplerParser.StringEnForceContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterBooleanEnForce(DoplerParser.BooleanEnForceContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitBooleanEnForce(DoplerParser.BooleanEnForceContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterDoubleEnForce(DoplerParser.DoubleEnForceContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitDoubleEnForce(DoplerParser.DoubleEnForceContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void enterEveryRule(ParserRuleContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void exitEveryRule(ParserRuleContext ctx) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void visitTerminal(TerminalNode node) {}

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    @Override
    public void visitErrorNode(ErrorNode node) {}
}
