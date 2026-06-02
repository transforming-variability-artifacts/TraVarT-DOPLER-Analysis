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

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link DoplerParser}.
 */
public interface DoplerParserListener extends ParseTreeListener {
    /**
     * Enter a parse tree produced by {@link DoplerParser#document}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterDocument(DoplerParser.DocumentContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#document}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitDocument(DoplerParser.DocumentContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#jsonDocument}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterJsonDocument(DoplerParser.JsonDocumentContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#jsonDocument}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitJsonDocument(DoplerParser.JsonDocumentContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#jsonValue}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterJsonValue(DoplerParser.JsonValueContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#jsonValue}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitJsonValue(DoplerParser.JsonValueContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#jsonObject}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterJsonObject(DoplerParser.JsonObjectContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#jsonObject}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitJsonObject(DoplerParser.JsonObjectContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#jsonPair}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterJsonPair(DoplerParser.JsonPairContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#jsonPair}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitJsonPair(DoplerParser.JsonPairContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#csvFile}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterCsvFile(DoplerParser.CsvFileContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#csvFile}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitCsvFile(DoplerParser.CsvFileContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#hdr}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterHdr(DoplerParser.HdrContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#hdr}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitHdr(DoplerParser.HdrContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#row}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterRow(DoplerParser.RowContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#row}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitRow(DoplerParser.RowContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#field}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterField(DoplerParser.FieldContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#field}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitField(DoplerParser.FieldContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#header}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterHeader(DoplerParser.HeaderContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#header}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitHeader(DoplerParser.HeaderContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#id}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterId(DoplerParser.IdContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#id}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitId(DoplerParser.IdContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#cardinality}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterCardinality(DoplerParser.CardinalityContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#cardinality}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitCardinality(DoplerParser.CardinalityContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#question}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterQuestion(DoplerParser.QuestionContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#question}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitQuestion(DoplerParser.QuestionContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#range}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterRange(DoplerParser.RangeContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#range}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitRange(DoplerParser.RangeContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#rangeItem}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterRangeItem(DoplerParser.RangeItemContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#rangeItem}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitRangeItem(DoplerParser.RangeItemContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#specialCharacter}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterSpecialCharacter(DoplerParser.SpecialCharacterContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#specialCharacter}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitSpecialCharacter(DoplerParser.SpecialCharacterContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#subrange}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterSubrange(DoplerParser.SubrangeContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#subrange}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitSubrange(DoplerParser.SubrangeContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#expression}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterExpression(DoplerParser.ExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#expression}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitExpression(DoplerParser.ExpressionContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#unaryExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterUnaryExpression(DoplerParser.UnaryExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#unaryExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitUnaryExpression(DoplerParser.UnaryExpressionContext ctx);

    /**
     * Enter a parse tree produced by
     * {@link DoplerParser#decisionVisibilityCallExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterDecisionVisibilityCallExpression(DoplerParser.DecisionVisibilityCallExpressionContext ctx);

    /**
     * Exit a parse tree produced by
     * {@link DoplerParser#decisionVisibilityCallExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitDecisionVisibilityCallExpression(DoplerParser.DecisionVisibilityCallExpressionContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#isTaken}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterIsTaken(DoplerParser.IsTakenContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#isTaken}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitIsTaken(DoplerParser.IsTakenContext ctx);

    /**
     * Enter a parse tree produced by
     * {@link DoplerParser#decisionValueCallExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterDecisionValueCallExpression(DoplerParser.DecisionValueCallExpressionContext ctx);

    /**
     * Exit a parse tree produced by
     * {@link DoplerParser#decisionValueCallExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitDecisionValueCallExpression(DoplerParser.DecisionValueCallExpressionContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#binaryExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterBinaryExpression(DoplerParser.BinaryExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#binaryExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitBinaryExpression(DoplerParser.BinaryExpressionContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#andExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterAndExpression(DoplerParser.AndExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#andExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitAndExpression(DoplerParser.AndExpressionContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#orExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterOrExpression(DoplerParser.OrExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#orExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitOrExpression(DoplerParser.OrExpressionContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#xorExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterXorExpression(DoplerParser.XorExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#xorExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitXorExpression(DoplerParser.XorExpressionContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#equalityExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterEqualityExpression(DoplerParser.EqualityExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#equalityExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitEqualityExpression(DoplerParser.EqualityExpressionContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#greaterThanExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterGreaterThanExpression(DoplerParser.GreaterThanExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#greaterThanExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitGreaterThanExpression(DoplerParser.GreaterThanExpressionContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#lessThanExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterLessThanExpression(DoplerParser.LessThanExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#lessThanExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitLessThanExpression(DoplerParser.LessThanExpressionContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#greaterEqualsExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterGreaterEqualsExpression(DoplerParser.GreaterEqualsExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#greaterEqualsExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitGreaterEqualsExpression(DoplerParser.GreaterEqualsExpressionContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#lessEqualsExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterLessEqualsExpression(DoplerParser.LessEqualsExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#lessEqualsExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitLessEqualsExpression(DoplerParser.LessEqualsExpressionContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#literalExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterLiteralExpression(DoplerParser.LiteralExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#literalExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitLiteralExpression(DoplerParser.LiteralExpressionContext ctx);

    /**
     * Enter a parse tree produced by
     * {@link DoplerParser#enumerationLiteralExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterEnumerationLiteralExpression(DoplerParser.EnumerationLiteralExpressionContext ctx);

    /**
     * Exit a parse tree produced by
     * {@link DoplerParser#enumerationLiteralExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitEnumerationLiteralExpression(DoplerParser.EnumerationLiteralExpressionContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#booleanLiteralExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterBooleanLiteralExpression(DoplerParser.BooleanLiteralExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#booleanLiteralExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitBooleanLiteralExpression(DoplerParser.BooleanLiteralExpressionContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#stringLiteralExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterStringLiteralExpression(DoplerParser.StringLiteralExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#stringLiteralExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitStringLiteralExpression(DoplerParser.StringLiteralExpressionContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#doubleLiteralExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterDoubleLiteralExpression(DoplerParser.DoubleLiteralExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#doubleLiteralExpression}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitDoubleLiteralExpression(DoplerParser.DoubleLiteralExpressionContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#decisionType}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterDecisionType(DoplerParser.DecisionTypeContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#decisionType}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitDecisionType(DoplerParser.DecisionTypeContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#drule}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterDrule(DoplerParser.DruleContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#drule}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitDrule(DoplerParser.DruleContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#action}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterAction(DoplerParser.ActionContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#action}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitAction(DoplerParser.ActionContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#valueRestrictionAction}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterValueRestrictionAction(DoplerParser.ValueRestrictionActionContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#valueRestrictionAction}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitValueRestrictionAction(DoplerParser.ValueRestrictionActionContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#allows}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterAllows(DoplerParser.AllowsContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#allows}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitAllows(DoplerParser.AllowsContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#disallows}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterDisallows(DoplerParser.DisallowsContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#disallows}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitDisallows(DoplerParser.DisallowsContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#enForce}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterEnForce(DoplerParser.EnForceContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#enForce}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitEnForce(DoplerParser.EnForceContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#enumEnForce}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterEnumEnForce(DoplerParser.EnumEnForceContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#enumEnForce}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitEnumEnForce(DoplerParser.EnumEnForceContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#stringEnForce}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterStringEnForce(DoplerParser.StringEnForceContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#stringEnForce}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitStringEnForce(DoplerParser.StringEnForceContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#booleanEnForce}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterBooleanEnForce(DoplerParser.BooleanEnForceContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#booleanEnForce}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitBooleanEnForce(DoplerParser.BooleanEnForceContext ctx);

    /**
     * Enter a parse tree produced by {@link DoplerParser#doubleEnForce}.
     *
     * @param ctx
     *            the parse tree
     */
    void enterDoubleEnForce(DoplerParser.DoubleEnForceContext ctx);

    /**
     * Exit a parse tree produced by {@link DoplerParser#doubleEnForce}.
     *
     * @param ctx
     *            the parse tree
     */
    void exitDoubleEnForce(DoplerParser.DoubleEnForceContext ctx);
}
