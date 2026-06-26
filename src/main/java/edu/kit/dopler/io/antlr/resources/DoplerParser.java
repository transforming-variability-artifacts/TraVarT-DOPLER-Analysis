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
package edu.kit.dopler.io.antlr.resources;

import java.util.List;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class DoplerParser extends Parser {
    public static final int CR = 1,
            LF = 2,
            QUESTION_KEY = 3,
            QUESTION = 4,
            TYPE_KEY = 5,
            RANGE_KEY = 6,
            CARDINALITY_KEY = 7,
            CONSTRAINT_RULE_KEY = 8,
            VISIBLE_RELEVANT_KEY = 9,
            DOPLER_KEY = 10,
            WS_DEFAULT = 11,
            WS_JSON = 12,
            ALLOW = 13,
            DISALLOW = 14,
            ISTAKEN = 15,
            GETVALUE = 16,
            EQUALS = 17,
            GREATER_THAN = 18,
            LESS_THAN = 19,
            LESS_EQUALS = 20,
            GREATER_EQUALS = 21,
            OR = 22,
            AND = 23,
            XOR = 24,
            PIPE = 25,
            SET = 26,
            NOT = 27,
            MINUS = 28,
            LPAREN = 29,
            RPAREN = 30,
            LBRACE = 31,
            RBRACE = 32,
            COLON = 33,
            IF = 34,
            ANPERSAND = 35,
            PERCENT = 36,
            COMMA = 37,
            SEMICOLON = 38,
            DQ = 39,
            BooleanLiteralExpression = 40,
            TRUE = 41,
            FALSE = 42,
            DoubleLiteralExpression = 43,
            EnumerationLiteralExpression = 44,
            StringLiteralExpression = 45,
            StringDecision = 46,
            NumberDecision = 47,
            EnumerationDecision = 48,
            ENUMERATION = 49,
            BooleanDecision = 50,
            SPECIAL_CHAR = 51,
            IDENTIFIER = 52;
    public static final int RULE_document = 0,
            RULE_jsonDocument = 1,
            RULE_jsonValue = 2,
            RULE_jsonObject = 3,
            RULE_jsonPair = 4,
            RULE_csvFile = 5,
            RULE_hdr = 6,
            RULE_row = 7,
            RULE_field = 8,
            RULE_header = 9,
            RULE_id = 10,
            RULE_cardinality = 11,
            RULE_question = 12,
            RULE_range = 13,
            RULE_rangeItem = 14,
            RULE_specialCharacter = 15,
            RULE_subrange = 16,
            RULE_expression = 17,
            RULE_unaryExpression = 18,
            RULE_decisionVisibilityCallExpression = 19,
            RULE_isTaken = 20,
            RULE_decisionValueCallExpression = 21,
            RULE_binaryExpression = 22,
            RULE_andExpression = 23,
            RULE_orExpression = 24,
            RULE_xorExpression = 25,
            RULE_equalityExpression = 26,
            RULE_greaterThanExpression = 27,
            RULE_lessThanExpression = 28,
            RULE_greaterEqualsExpression = 29,
            RULE_lessEqualsExpression = 30,
            RULE_literalExpression = 31,
            RULE_enumerationLiteralExpression = 32,
            RULE_booleanLiteralExpression = 33,
            RULE_stringLiteralExpression = 34,
            RULE_doubleLiteralExpression = 35,
            RULE_decisionType = 36,
            RULE_drule = 37,
            RULE_action = 38,
            RULE_valueRestrictionAction = 39,
            RULE_allows = 40,
            RULE_disallows = 41,
            RULE_enForce = 42,
            RULE_enumEnForce = 43,
            RULE_stringEnForce = 44,
            RULE_booleanEnForce = 45,
            RULE_doubleEnForce = 46;
    public static final String[] ruleNames = makeRuleNames();
    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated
    public static final String[] tokenNames;

    public static final String _serializedATN =
            "\u0004\u00014\u01f2\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"
                    + "\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"
                    + "\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"
                    + "\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"
                    + "\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"
                    + "\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"
                    + "\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"
                    + "\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"
                    + "\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"
                    + "\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e"
                    + "\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007\"\u0002"
                    + "#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007&\u0002\'\u0007\'\u0002"
                    + "(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007+\u0002,\u0007,\u0002"
                    + "-\u0007-\u0002.\u0007.\u0001\u0000\u0001\u0000\u0003\u0000a\b\u0000\u0001"
                    + "\u0001\u0001\u0001\u0003\u0001e\b\u0001\u0001\u0001\u0001\u0001\u0001"
                    + "\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"
                    + "\u0002\u0001\u0002\u0003\u0002q\b\u0002\u0005\u0002s\b\u0002\n\u0002\f"
                    + "\u0002v\t\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002{\b\u0002"
                    + "\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003"
                    + "\u0001\u0003\u0003\u0003\u0084\b\u0003\u0005\u0003\u0086\b\u0003\n\u0003"
                    + "\f\u0003\u0089\t\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003"
                    + "\u0003\u0003\u008f\b\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"
                    + "\u0001\u0004\u0001\u0004\u0003\u0004\u0097\b\u0004\u0001\u0004\u0001\u0004"
                    + "\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"
                    + "\u0001\u0004\u0001\u0004\u0003\u0004\u00a3\b\u0004\u0001\u0004\u0001\u0004"
                    + "\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"
                    + "\u0001\u0004\u0001\u0004\u0003\u0004\u00af\b\u0004\u0001\u0004\u0001\u0004"
                    + "\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"
                    + "\u0001\u0004\u0001\u0004\u0003\u0004\u00bb\b\u0004\u0001\u0004\u0001\u0004"
                    + "\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"
                    + "\u0001\u0004\u0003\u0004\u00c6\b\u0004\u0001\u0004\u0001\u0004\u0001\u0004"
                    + "\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"
                    + "\u0003\u0004\u00d1\b\u0004\u0001\u0004\u0003\u0004\u00d4\b\u0004\u0001"
                    + "\u0005\u0001\u0005\u0004\u0005\u00d8\b\u0005\u000b\u0005\f\u0005\u00d9"
                    + "\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007"
                    + "\u0001\u0007\u0005\u0007\u00e3\b\u0007\n\u0007\f\u0007\u00e6\t\u0007\u0001"
                    + "\u0007\u0003\u0007\u00e9\b\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001"
                    + "\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0003"
                    + "\b\u00f7\b\b\u0001\t\u0001\t\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001"
                    + "\u000b\u0001\u000b\u0001\f\u0001\f\u0001\r\u0001\r\u0005\r\u0105\b\r\n"
                    + "\r\f\r\u0108\t\r\u0001\r\u0001\r\u0001\r\u0005\r\u010d\b\r\n\r\f\r\u0110"
                    + "\t\r\u0004\r\u0112\b\r\u000b\r\f\r\u0113\u0001\r\u0001\r\u0001\r\u0003"
                    + "\r\u0119\b\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e"
                    + "\u0001\u000e\u0001\u000e\u0003\u000e\u0122\b\u000e\u0001\u000f\u0001\u000f"
                    + "\u0001\u0010\u0001\u0010\u0001\u0010\u0005\u0010\u0129\b\u0010\n\u0010"
                    + "\f\u0010\u012c\t\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011"
                    + "\u0001\u0011\u0003\u0011\u0133\b\u0011\u0001\u0012\u0001\u0012\u0001\u0012"
                    + "\u0001\u0012\u0003\u0012\u0139\b\u0012\u0001\u0013\u0001\u0013\u0001\u0013"
                    + "\u0005\u0013\u013e\b\u0013\n\u0013\f\u0013\u0141\t\u0013\u0001\u0013\u0003"
                    + "\u0013\u0144\b\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"
                    + "\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0003"
                    + "\u0015\u0150\b\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001"
                    + "\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0003\u0016\u015a\b\u0016\u0001"
                    + "\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001"
                    + "\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001"
                    + "\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"
                    + "\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001"
                    + "\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001"
                    + "\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001"
                    + "\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001"
                    + "\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001"
                    + "\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0003\u001f\u0190\b\u001f\u0001"
                    + " \u0001 \u0001!\u0001!\u0001\"\u0001\"\u0001#\u0001#\u0001$\u0001$\u0001"
                    + "%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0003%\u01a3\b%\u0001%\u0001"
                    + "%\u0003%\u01a7\b%\u0001%\u0001%\u0001%\u0003%\u01ac\b%\u0004%\u01ae\b"
                    + "%\u000b%\f%\u01af\u0001%\u0001%\u0003%\u01b4\b%\u0001%\u0001%\u0005%\u01b8"
                    + "\b%\n%\f%\u01bb\t%\u0001&\u0001&\u0001\'\u0001\'\u0001\'\u0003\'\u01c2"
                    + "\b\'\u0001(\u0001(\u0001(\u0001(\u0001(\u0001(\u0001(\u0001(\u0003(\u01cc"
                    + "\b(\u0001)\u0001)\u0001)\u0001)\u0001)\u0001)\u0001)\u0001)\u0003)\u01d6"
                    + "\b)\u0001*\u0001*\u0001*\u0001*\u0003*\u01dc\b*\u0001+\u0001+\u0001+\u0005"
                    + "+\u01e1\b+\n+\f+\u01e4\t+\u0001,\u0001,\u0001,\u0001,\u0001-\u0001-\u0001"
                    + "-\u0001-\u0001.\u0001.\u0001.\u0001.\u0001.\u0000\u0001J/\u0000\u0002"
                    + "\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e"
                    + " \"$&(*,.02468:<>@BDFHJLNPRTVXZ\\\u0000\u0006\u0002\u0000\u0003\u0003"
                    + "\u0005\t\u0003\u0000\u001d!#%33\u0001\u0000\u0011\u0017\u0002\u0000\u0011"
                    + "\u0013\u0016\u0017\u0002\u0000\u0011\u0011\u001a\u001a\u0002\u0000.02"
                    + "2\u0212\u0000`\u0001\u0000\u0000\u0000\u0002b\u0001\u0000\u0000\u0000"
                    + "\u0004z\u0001\u0000\u0000\u0000\u0006\u008e\u0001\u0000\u0000\u0000\b"
                    + "\u00d3\u0001\u0000\u0000\u0000\n\u00d5\u0001\u0000\u0000\u0000\f\u00dd"
                    + "\u0001\u0000\u0000\u0000\u000e\u00df\u0001\u0000\u0000\u0000\u0010\u00f6"
                    + "\u0001\u0000\u0000\u0000\u0012\u00f8\u0001\u0000\u0000\u0000\u0014\u00fa"
                    + "\u0001\u0000\u0000\u0000\u0016\u00fc\u0001\u0000\u0000\u0000\u0018\u0100"
                    + "\u0001\u0000\u0000\u0000\u001a\u0118\u0001\u0000\u0000\u0000\u001c\u0121"
                    + "\u0001\u0000\u0000\u0000\u001e\u0123\u0001\u0000\u0000\u0000 \u0125\u0001"
                    + "\u0000\u0000\u0000\"\u0132\u0001\u0000\u0000\u0000$\u0138\u0001\u0000"
                    + "\u0000\u0000&\u0143\u0001\u0000\u0000\u0000(\u0145\u0001\u0000\u0000\u0000"
                    + "*\u014f\u0001\u0000\u0000\u0000,\u0159\u0001\u0000\u0000\u0000.\u015b"
                    + "\u0001\u0000\u0000\u00000\u0161\u0001\u0000\u0000\u00002\u0167\u0001\u0000"
                    + "\u0000\u00004\u016d\u0001\u0000\u0000\u00006\u0173\u0001\u0000\u0000\u0000"
                    + "8\u0179\u0001\u0000\u0000\u0000:\u017f\u0001\u0000\u0000\u0000<\u0185"
                    + "\u0001\u0000\u0000\u0000>\u018f\u0001\u0000\u0000\u0000@\u0191\u0001\u0000"
                    + "\u0000\u0000B\u0193\u0001\u0000\u0000\u0000D\u0195\u0001\u0000\u0000\u0000"
                    + "F\u0197\u0001\u0000\u0000\u0000H\u0199\u0001\u0000\u0000\u0000J\u01b3"
                    + "\u0001\u0000\u0000\u0000L\u01bc\u0001\u0000\u0000\u0000N\u01c1\u0001\u0000"
                    + "\u0000\u0000P\u01cb\u0001\u0000\u0000\u0000R\u01d5\u0001\u0000\u0000\u0000"
                    + "T\u01db\u0001\u0000\u0000\u0000V\u01dd\u0001\u0000\u0000\u0000X\u01e5"
                    + "\u0001\u0000\u0000\u0000Z\u01e9\u0001\u0000\u0000\u0000\\\u01ed\u0001"
                    + "\u0000\u0000\u0000^a\u0003\u0002\u0001\u0000_a\u0003\n\u0005\u0000`^\u0001"
                    + "\u0000\u0000\u0000`_\u0001\u0000\u0000\u0000a\u0001\u0001\u0000\u0000"
                    + "\u0000bd\u0005\u001f\u0000\u0000ce\u0003\u0004\u0002\u0000dc\u0001\u0000"
                    + "\u0000\u0000de\u0001\u0000\u0000\u0000ef\u0001\u0000\u0000\u0000fg\u0005"
                    + " \u0000\u0000gh\u0005\u0000\u0000\u0001h\u0003\u0001\u0000\u0000\u0000"
                    + "ij\u0005\'\u0000\u0000jk\u0005\n\u0000\u0000kl\u0005\'\u0000\u0000lm\u0005"
                    + "!\u0000\u0000mt\u0005\u001f\u0000\u0000np\u0003\u0006\u0003\u0000oq\u0005"
                    + "%\u0000\u0000po\u0001\u0000\u0000\u0000pq\u0001\u0000\u0000\u0000qs\u0001"
                    + "\u0000\u0000\u0000rn\u0001\u0000\u0000\u0000sv\u0001\u0000\u0000\u0000"
                    + "tr\u0001\u0000\u0000\u0000tu\u0001\u0000\u0000\u0000uw\u0001\u0000\u0000"
                    + "\u0000vt\u0001\u0000\u0000\u0000w{\u0005 \u0000\u0000xy\u0005\u001f\u0000"
                    + "\u0000y{\u0005 \u0000\u0000zi\u0001\u0000\u0000\u0000zx\u0001\u0000\u0000"
                    + "\u0000{\u0005\u0001\u0000\u0000\u0000|}\u0005\'\u0000\u0000}~\u0003\u0014"
                    + "\n\u0000~\u007f\u0005\'\u0000\u0000\u007f\u0080\u0005!\u0000\u0000\u0080"
                    + "\u0087\u0005\u001f\u0000\u0000\u0081\u0083\u0003\b\u0004\u0000\u0082\u0084"
                    + "\u0005%\u0000\u0000\u0083\u0082\u0001\u0000\u0000\u0000\u0083\u0084\u0001"
                    + "\u0000\u0000\u0000\u0084\u0086\u0001\u0000\u0000\u0000\u0085\u0081\u0001"
                    + "\u0000\u0000\u0000\u0086\u0089\u0001\u0000\u0000\u0000\u0087\u0085\u0001"
                    + "\u0000\u0000\u0000\u0087\u0088\u0001\u0000\u0000\u0000\u0088\u008a\u0001"
                    + "\u0000\u0000\u0000\u0089\u0087\u0001\u0000\u0000\u0000\u008a\u008b\u0005"
                    + " \u0000\u0000\u008b\u008f\u0001\u0000\u0000\u0000\u008c\u008d\u0005\u001f"
                    + "\u0000\u0000\u008d\u008f\u0005 \u0000\u0000\u008e|\u0001\u0000\u0000\u0000"
                    + "\u008e\u008c\u0001\u0000\u0000\u0000\u008f\u0007\u0001\u0000\u0000\u0000"
                    + "\u0090\u0091\u0005\u0003\u0000\u0000\u0091\u0096\u0005\'\u0000\u0000\u0092"
                    + "\u0097\u0003\u0018\f\u0000\u0093\u0094\u0005\'\u0000\u0000\u0094\u0097"
                    + "\u0005\'\u0000\u0000\u0095\u0097\u0001\u0000\u0000\u0000\u0096\u0092\u0001"
                    + "\u0000\u0000\u0000\u0096\u0093\u0001\u0000\u0000\u0000\u0096\u0095\u0001"
                    + "\u0000\u0000\u0000\u0097\u0098\u0001\u0000\u0000\u0000\u0098\u00d4\u0005"
                    + "\'\u0000\u0000\u0099\u009a\u0005\'\u0000\u0000\u009a\u009b\u0005\u0005"
                    + "\u0000\u0000\u009b\u009c\u0005\'\u0000\u0000\u009c\u009d\u0005!\u0000"
                    + "\u0000\u009d\u00a2\u0005\'\u0000\u0000\u009e\u00a3\u0003H$\u0000\u009f"
                    + "\u00a0\u0005\'\u0000\u0000\u00a0\u00a3\u0005\'\u0000\u0000\u00a1\u00a3"
                    + "\u0001\u0000\u0000\u0000\u00a2\u009e\u0001\u0000\u0000\u0000\u00a2\u009f"
                    + "\u0001\u0000\u0000\u0000\u00a2\u00a1\u0001\u0000\u0000\u0000\u00a3\u00a4"
                    + "\u0001\u0000\u0000\u0000\u00a4\u00d4\u0005\'\u0000\u0000\u00a5\u00a6\u0005"
                    + "\'\u0000\u0000\u00a6\u00a7\u0005\u0006\u0000\u0000\u00a7\u00a8\u0005\'"
                    + "\u0000\u0000\u00a8\u00a9\u0005!\u0000\u0000\u00a9\u00ae\u0005\'\u0000"
                    + "\u0000\u00aa\u00af\u0003\u001a\r\u0000\u00ab\u00ac\u0005\'\u0000\u0000"
                    + "\u00ac\u00af\u0005\'\u0000\u0000\u00ad\u00af\u0001\u0000\u0000\u0000\u00ae"
                    + "\u00aa\u0001\u0000\u0000\u0000\u00ae\u00ab\u0001\u0000\u0000\u0000\u00ae"
                    + "\u00ad\u0001\u0000\u0000\u0000\u00af\u00b0\u0001\u0000\u0000\u0000\u00b0"
                    + "\u00d4\u0005\'\u0000\u0000\u00b1\u00b2\u0005\'\u0000\u0000\u00b2\u00b3"
                    + "\u0005\u0007\u0000\u0000\u00b3\u00b4\u0005\'\u0000\u0000\u00b4\u00b5\u0005"
                    + "!\u0000\u0000\u00b5\u00ba\u0005\'\u0000\u0000\u00b6\u00bb\u0003\u0016"
                    + "\u000b\u0000\u00b7\u00b8\u0005\'\u0000\u0000\u00b8\u00bb\u0005\'\u0000"
                    + "\u0000\u00b9\u00bb\u0001\u0000\u0000\u0000\u00ba\u00b6\u0001\u0000\u0000"
                    + "\u0000\u00ba\u00b7\u0001\u0000\u0000\u0000\u00ba\u00b9\u0001\u0000\u0000"
                    + "\u0000\u00bb\u00bc\u0001\u0000\u0000\u0000\u00bc\u00d4\u0005\'\u0000\u0000"
                    + "\u00bd\u00be\u0005\'\u0000\u0000\u00be\u00bf\u0005\b\u0000\u0000\u00bf"
                    + "\u00c0\u0005\'\u0000\u0000\u00c0\u00c5\u0005!\u0000\u0000\u00c1\u00c6"
                    + "\u0003J%\u0000\u00c2\u00c3\u0005\'\u0000\u0000\u00c3\u00c6\u0005\'\u0000"
                    + "\u0000\u00c4\u00c6\u0001\u0000\u0000\u0000\u00c5\u00c1\u0001\u0000\u0000"
                    + "\u0000\u00c5\u00c2\u0001\u0000\u0000\u0000\u00c5\u00c4\u0001\u0000\u0000"
                    + "\u0000\u00c6\u00d4\u0001\u0000\u0000\u0000\u00c7\u00c8\u0005\'\u0000\u0000"
                    + "\u00c8\u00c9\u0005\t\u0000\u0000\u00c9\u00ca\u0005\'\u0000\u0000\u00ca"
                    + "\u00cb\u0005!\u0000\u0000\u00cb\u00d0\u0005\'\u0000\u0000\u00cc\u00d1"
                    + "\u0003&\u0013\u0000\u00cd\u00ce\u0005\'\u0000\u0000\u00ce\u00d1\u0005"
                    + "\'\u0000\u0000\u00cf\u00d1\u0001\u0000\u0000\u0000\u00d0\u00cc\u0001\u0000"
                    + "\u0000\u0000\u00d0\u00cd\u0001\u0000\u0000\u0000\u00d0\u00cf\u0001\u0000"
                    + "\u0000\u0000\u00d1\u00d2\u0001\u0000\u0000\u0000\u00d2\u00d4\u0005\'\u0000"
                    + "\u0000\u00d3\u0090\u0001\u0000\u0000\u0000\u00d3\u0099\u0001\u0000\u0000"
                    + "\u0000\u00d3\u00a5\u0001\u0000\u0000\u0000\u00d3\u00b1\u0001\u0000\u0000"
                    + "\u0000\u00d3\u00bd\u0001\u0000\u0000\u0000\u00d3\u00c7\u0001\u0000\u0000"
                    + "\u0000\u00d4\t\u0001\u0000\u0000\u0000\u00d5\u00d7\u0003\f\u0006\u0000"
                    + "\u00d6\u00d8\u0003\u000e\u0007\u0000\u00d7\u00d6\u0001\u0000\u0000\u0000"
                    + "\u00d8\u00d9\u0001\u0000\u0000\u0000\u00d9\u00d7\u0001\u0000\u0000\u0000"
                    + "\u00d9\u00da\u0001\u0000\u0000\u0000\u00da\u00db\u0001\u0000\u0000\u0000"
                    + "\u00db\u00dc\u0005\u0000\u0000\u0001\u00dc\u000b\u0001\u0000\u0000\u0000"
                    + "\u00dd\u00de\u0003\u000e\u0007\u0000\u00de\r\u0001\u0000\u0000\u0000\u00df"
                    + "\u00e4\u0003\u0010\b\u0000\u00e0\u00e1\u0005&\u0000\u0000\u00e1\u00e3"
                    + "\u0003\u0010\b\u0000\u00e2\u00e0\u0001\u0000\u0000\u0000\u00e3\u00e6\u0001"
                    + "\u0000\u0000\u0000\u00e4\u00e2\u0001\u0000\u0000\u0000\u00e4\u00e5\u0001"
                    + "\u0000\u0000\u0000\u00e5\u00e8\u0001\u0000\u0000\u0000\u00e6\u00e4\u0001"
                    + "\u0000\u0000\u0000\u00e7\u00e9\u0005\u0001\u0000\u0000\u00e8\u00e7\u0001"
                    + "\u0000\u0000\u0000\u00e8\u00e9\u0001\u0000\u0000\u0000\u00e9\u00ea\u0001"
                    + "\u0000\u0000\u0000\u00ea\u00eb\u0005\u0002\u0000\u0000\u00eb\u000f\u0001"
                    + "\u0000\u0000\u0000\u00ec\u00f7\u0003J%\u0000\u00ed\u00f7\u0003\u0014\n"
                    + "\u0000\u00ee\u00f7\u0003\u0018\f\u0000\u00ef\u00f7\u0003H$\u0000\u00f0"
                    + "\u00f7\u0003\u0016\u000b\u0000\u00f1\u00f7\u0003\u001a\r\u0000\u00f2\u00f7"
                    + "\u0003&\u0013\u0000\u00f3\u00f7\u0003\u0012\t\u0000\u00f4\u00f7\u0005"
                    + "4\u0000\u0000\u00f5\u00f7\u0001\u0000\u0000\u0000\u00f6\u00ec\u0001\u0000"
                    + "\u0000\u0000\u00f6\u00ed\u0001\u0000\u0000\u0000\u00f6\u00ee\u0001\u0000"
                    + "\u0000\u0000\u00f6\u00ef\u0001\u0000\u0000\u0000\u00f6\u00f0\u0001\u0000"
                    + "\u0000\u0000\u00f6\u00f1\u0001\u0000\u0000\u0000\u00f6\u00f2\u0001\u0000"
                    + "\u0000\u0000\u00f6\u00f3\u0001\u0000\u0000\u0000\u00f6\u00f4\u0001\u0000"
                    + "\u0000\u0000\u00f6\u00f5\u0001\u0000\u0000\u0000\u00f7\u0011\u0001\u0000"
                    + "\u0000\u0000\u00f8\u00f9\u0007\u0000\u0000\u0000\u00f9\u0013\u0001\u0000"
                    + "\u0000\u0000\u00fa\u00fb\u00054\u0000\u0000\u00fb\u0015\u0001\u0000\u0000"
                    + "\u0000\u00fc\u00fd\u0005+\u0000\u0000\u00fd\u00fe\u0005!\u0000\u0000\u00fe"
                    + "\u00ff\u0005+\u0000\u0000\u00ff\u0017\u0001\u0000\u0000\u0000\u0100\u0101"
                    + "\u0005\u0004\u0000\u0000\u0101\u0019\u0001\u0000\u0000\u0000\u0102\u0106"
                    + "\u0003\u001c\u000e\u0000\u0103\u0105\u0003\u001c\u000e\u0000\u0104\u0103"
                    + "\u0001\u0000\u0000\u0000\u0105\u0108\u0001\u0000\u0000\u0000\u0106\u0104"
                    + "\u0001\u0000\u0000\u0000\u0106\u0107\u0001\u0000\u0000\u0000\u0107\u0111"
                    + "\u0001\u0000\u0000\u0000\u0108\u0106\u0001\u0000\u0000\u0000\u0109\u010a"
                    + "\u0005\u0019\u0000\u0000\u010a\u010e\u0003\u001c\u000e\u0000\u010b\u010d"
                    + "\u0003\u001c\u000e\u0000\u010c\u010b\u0001\u0000\u0000\u0000\u010d\u0110"
                    + "\u0001\u0000\u0000\u0000\u010e\u010c\u0001\u0000\u0000\u0000\u010e\u010f"
                    + "\u0001\u0000\u0000\u0000\u010f\u0112\u0001\u0000\u0000\u0000\u0110\u010e"
                    + "\u0001\u0000\u0000\u0000\u0111\u0109\u0001\u0000\u0000\u0000\u0112\u0113"
                    + "\u0001\u0000\u0000\u0000\u0113\u0111\u0001\u0000\u0000\u0000\u0113\u0114"
                    + "\u0001\u0000\u0000\u0000\u0114\u0119\u0001\u0000\u0000\u0000\u0115\u0116"
                    + "\u0005+\u0000\u0000\u0116\u0117\u0005\u001c\u0000\u0000\u0117\u0119\u0005"
                    + "+\u0000\u0000\u0118\u0102\u0001\u0000\u0000\u0000\u0118\u0115\u0001\u0000"
                    + "\u0000\u0000\u0119\u001b\u0001\u0000\u0000\u0000\u011a\u0122\u00054\u0000"
                    + "\u0000\u011b\u0122\u0003\u001e\u000f\u0000\u011c\u0122\u0003\"\u0011\u0000"
                    + "\u011d\u0122\u0003 \u0010\u0000\u011e\u0122\u0003\u0016\u000b\u0000\u011f"
                    + "\u0122\u0005\u0004\u0000\u0000\u0120\u0122\u0003\u0012\t\u0000\u0121\u011a"
                    + "\u0001\u0000\u0000\u0000\u0121\u011b\u0001\u0000\u0000\u0000\u0121\u011c"
                    + "\u0001\u0000\u0000\u0000\u0121\u011d\u0001\u0000\u0000\u0000\u0121\u011e"
                    + "\u0001\u0000\u0000\u0000\u0121\u011f\u0001\u0000\u0000\u0000\u0121\u0120"
                    + "\u0001\u0000\u0000\u0000\u0122\u001d\u0001\u0000\u0000\u0000\u0123\u0124"
                    + "\u0007\u0001\u0000\u0000\u0124\u001f\u0001\u0000\u0000\u0000\u0125\u012a"
                    + "\u0003\"\u0011\u0000\u0126\u0127\u0007\u0002\u0000\u0000\u0127\u0129\u0003"
                    + "\"\u0011\u0000\u0128\u0126\u0001\u0000\u0000\u0000\u0129\u012c\u0001\u0000"
                    + "\u0000\u0000\u012a\u0128\u0001\u0000\u0000\u0000\u012a\u012b\u0001\u0000"
                    + "\u0000\u0000\u012b!\u0001\u0000\u0000\u0000\u012c\u012a\u0001\u0000\u0000"
                    + "\u0000\u012d\u0133\u0003$\u0012\u0000\u012e\u0133\u0003>\u001f\u0000\u012f"
                    + "\u0133\u0003(\u0014\u0000\u0130\u0133\u0003*\u0015\u0000\u0131\u0133\u0003"
                    + ",\u0016\u0000\u0132\u012d\u0001\u0000\u0000\u0000\u0132\u012e\u0001\u0000"
                    + "\u0000\u0000\u0132\u012f\u0001\u0000\u0000\u0000\u0132\u0130\u0001\u0000"
                    + "\u0000\u0000\u0132\u0131\u0001\u0000\u0000\u0000\u0133#\u0001\u0000\u0000"
                    + "\u0000\u0134\u0135\u0005\u001b\u0000\u0000\u0135\u0139\u0003\"\u0011\u0000"
                    + "\u0136\u0137\u0005\u001b\u0000\u0000\u0137\u0139\u0005,\u0000\u0000\u0138"
                    + "\u0134\u0001\u0000\u0000\u0000\u0138\u0136\u0001\u0000\u0000\u0000\u0139"
                    + "%\u0001\u0000\u0000\u0000\u013a\u013f\u0003\"\u0011\u0000\u013b\u013c"
                    + "\u0007\u0003\u0000\u0000\u013c\u013e\u0003\"\u0011\u0000\u013d\u013b\u0001"
                    + "\u0000\u0000\u0000\u013e\u0141\u0001\u0000\u0000\u0000\u013f\u013d\u0001"
                    + "\u0000\u0000\u0000\u013f\u0140\u0001\u0000\u0000\u0000\u0140\u0144\u0001"
                    + "\u0000\u0000\u0000\u0141\u013f\u0001\u0000\u0000\u0000\u0142\u0144\u0003"
                    + "\"\u0011\u0000\u0143\u013a\u0001\u0000\u0000\u0000\u0143\u0142\u0001\u0000"
                    + "\u0000\u0000\u0144\'\u0001\u0000\u0000\u0000\u0145\u0146\u0005\u000f\u0000"
                    + "\u0000\u0146\u0147\u0005\u001d\u0000\u0000\u0147\u0148\u00054\u0000\u0000"
                    + "\u0148\u0149\u0005\u001e\u0000\u0000\u0149)\u0001\u0000\u0000\u0000\u014a"
                    + "\u014b\u0005\u0010\u0000\u0000\u014b\u014c\u0005\u001d\u0000\u0000\u014c"
                    + "\u014d\u00054\u0000\u0000\u014d\u0150\u0005\u001e\u0000\u0000\u014e\u0150"
                    + "\u00054\u0000\u0000\u014f\u014a\u0001\u0000\u0000\u0000\u014f\u014e\u0001"
                    + "\u0000\u0000\u0000\u0150+\u0001\u0000\u0000\u0000\u0151\u015a\u0003.\u0017"
                    + "\u0000\u0152\u015a\u00030\u0018\u0000\u0153\u015a\u00032\u0019\u0000\u0154"
                    + "\u015a\u00034\u001a\u0000\u0155\u015a\u00036\u001b\u0000\u0156\u015a\u0003"
                    + "8\u001c\u0000\u0157\u015a\u0003:\u001d\u0000\u0158\u015a\u0003<\u001e"
                    + "\u0000\u0159\u0151\u0001\u0000\u0000\u0000\u0159\u0152\u0001\u0000\u0000"
                    + "\u0000\u0159\u0153\u0001\u0000\u0000\u0000\u0159\u0154\u0001\u0000\u0000"
                    + "\u0000\u0159\u0155\u0001\u0000\u0000\u0000\u0159\u0156\u0001\u0000\u0000"
                    + "\u0000\u0159\u0157\u0001\u0000\u0000\u0000\u0159\u0158\u0001\u0000\u0000"
                    + "\u0000\u015a-\u0001\u0000\u0000\u0000\u015b\u015c\u0005\u001d\u0000\u0000"
                    + "\u015c\u015d\u0003\"\u0011\u0000\u015d\u015e\u0005\u0017\u0000\u0000\u015e"
                    + "\u015f\u0003\"\u0011\u0000\u015f\u0160\u0005\u001e\u0000\u0000\u0160/"
                    + "\u0001\u0000\u0000\u0000\u0161\u0162\u0005\u001d\u0000\u0000\u0162\u0163"
                    + "\u0003\"\u0011\u0000\u0163\u0164\u0005\u0016\u0000\u0000\u0164\u0165\u0003"
                    + "\"\u0011\u0000\u0165\u0166\u0005\u001e\u0000\u0000\u01661\u0001\u0000"
                    + "\u0000\u0000\u0167\u0168\u0005\u001d\u0000\u0000\u0168\u0169\u0003\"\u0011"
                    + "\u0000\u0169\u016a\u0005\u0018\u0000\u0000\u016a\u016b\u0003\"\u0011\u0000"
                    + "\u016b\u016c\u0005\u001e\u0000\u0000\u016c3\u0001\u0000\u0000\u0000\u016d"
                    + "\u016e\u0005\u001d\u0000\u0000\u016e\u016f\u0003\"\u0011\u0000\u016f\u0170"
                    + "\u0007\u0004\u0000\u0000\u0170\u0171\u0003\"\u0011\u0000\u0171\u0172\u0005"
                    + "\u001e\u0000\u0000\u01725\u0001\u0000\u0000\u0000\u0173\u0174\u0005\u001d"
                    + "\u0000\u0000\u0174\u0175\u0003\"\u0011\u0000\u0175\u0176\u0005\u0012\u0000"
                    + "\u0000\u0176\u0177\u0003\"\u0011\u0000\u0177\u0178\u0005\u001e\u0000\u0000"
                    + "\u01787\u0001\u0000\u0000\u0000\u0179\u017a\u0005\u001d\u0000\u0000\u017a"
                    + "\u017b\u0003\"\u0011\u0000\u017b\u017c\u0005\u0013\u0000\u0000\u017c\u017d"
                    + "\u0003\"\u0011\u0000\u017d\u017e\u0005\u001e\u0000\u0000\u017e9\u0001"
                    + "\u0000\u0000\u0000\u017f\u0180\u0005\u001d\u0000\u0000\u0180\u0181\u0003"
                    + "\"\u0011\u0000\u0181\u0182\u0005\u0015\u0000\u0000\u0182\u0183\u0003\""
                    + "\u0011\u0000\u0183\u0184\u0005\u001e\u0000\u0000\u0184;\u0001\u0000\u0000"
                    + "\u0000\u0185\u0186\u0005\u001d\u0000\u0000\u0186\u0187\u0003\"\u0011\u0000"
                    + "\u0187\u0188\u0005\u0014\u0000\u0000\u0188\u0189\u0003\"\u0011\u0000\u0189"
                    + "\u018a\u0005\u001e\u0000\u0000\u018a=\u0001\u0000\u0000\u0000\u018b\u0190"
                    + "\u0003@ \u0000\u018c\u0190\u0003B!\u0000\u018d\u0190\u0003D\"\u0000\u018e"
                    + "\u0190\u0003F#\u0000\u018f\u018b\u0001\u0000\u0000\u0000\u018f\u018c\u0001"
                    + "\u0000\u0000\u0000\u018f\u018d\u0001\u0000\u0000\u0000\u018f\u018e\u0001"
                    + "\u0000\u0000\u0000\u0190?\u0001\u0000\u0000\u0000\u0191\u0192\u0005,\u0000"
                    + "\u0000\u0192A\u0001\u0000\u0000\u0000\u0193\u0194\u0005(\u0000\u0000\u0194"
                    + "C\u0001\u0000\u0000\u0000\u0195\u0196\u0005-\u0000\u0000\u0196E\u0001"
                    + "\u0000\u0000\u0000\u0197\u0198\u0005+\u0000\u0000\u0198G\u0001\u0000\u0000"
                    + "\u0000\u0199\u019a\u0007\u0005\u0000\u0000\u019aI\u0001\u0000\u0000\u0000"
                    + "\u019b\u019c\u0006%\uffff\uffff\u0000\u019c\u019d\u0005\'\u0000\u0000"
                    + "\u019d\u019e\u0003J%\u0000\u019e\u019f\u0005\'\u0000\u0000\u019f\u01b4"
                    + "\u0001\u0000\u0000\u0000\u01a0\u01a2\u0005\"\u0000\u0000\u01a1\u01a3\u0005"
                    + "\u001d\u0000\u0000\u01a2\u01a1\u0001\u0000\u0000\u0000\u01a2\u01a3\u0001"
                    + "\u0000\u0000\u0000\u01a3\u01a4\u0001\u0000\u0000\u0000\u01a4\u01a6\u0003"
                    + "\"\u0011\u0000\u01a5\u01a7\u0005\u001e\u0000\u0000\u01a6\u01a5\u0001\u0000"
                    + "\u0000\u0000\u01a6\u01a7\u0001\u0000\u0000\u0000\u01a7\u01a8\u0001\u0000"
                    + "\u0000\u0000\u01a8\u01ad\u0005\u001f\u0000\u0000\u01a9\u01ab\u0003L&\u0000"
                    + "\u01aa\u01ac\u0005&\u0000\u0000\u01ab\u01aa\u0001\u0000\u0000\u0000\u01ab"
                    + "\u01ac\u0001\u0000\u0000\u0000\u01ac\u01ae\u0001\u0000\u0000\u0000\u01ad"
                    + "\u01a9\u0001\u0000\u0000\u0000\u01ae\u01af\u0001\u0000\u0000\u0000\u01af"
                    + "\u01ad\u0001\u0000\u0000\u0000\u01af\u01b0\u0001\u0000\u0000\u0000\u01b0"
                    + "\u01b1\u0001\u0000\u0000\u0000\u01b1\u01b2\u0005 \u0000\u0000\u01b2\u01b4"
                    + "\u0001\u0000\u0000\u0000\u01b3\u019b\u0001\u0000\u0000\u0000\u01b3\u01a0"
                    + "\u0001\u0000\u0000\u0000\u01b4\u01b9\u0001\u0000\u0000\u0000\u01b5\u01b6"
                    + "\n\u0002\u0000\u0000\u01b6\u01b8\u0003J%\u0003\u01b7\u01b5\u0001\u0000"
                    + "\u0000\u0000\u01b8\u01bb\u0001\u0000\u0000\u0000\u01b9\u01b7\u0001\u0000"
                    + "\u0000\u0000\u01b9\u01ba\u0001\u0000\u0000\u0000\u01baK\u0001\u0000\u0000"
                    + "\u0000\u01bb\u01b9\u0001\u0000\u0000\u0000\u01bc\u01bd\u0003N\'\u0000"
                    + "\u01bdM\u0001\u0000\u0000\u0000\u01be\u01c2\u0003P(\u0000\u01bf\u01c2"
                    + "\u0003R)\u0000\u01c0\u01c2\u0003T*\u0000\u01c1\u01be\u0001\u0000\u0000"
                    + "\u0000\u01c1\u01bf\u0001\u0000\u0000\u0000\u01c1\u01c0\u0001\u0000\u0000"
                    + "\u0000\u01c2O\u0001\u0000\u0000\u0000\u01c3\u01c4\u0005\r\u0000\u0000"
                    + "\u01c4\u01c5\u0005\u001d\u0000\u0000\u01c5\u01c6\u00054\u0000\u0000\u01c6"
                    + "\u01cc\u0005\u001e\u0000\u0000\u01c7\u01c8\u0005\r\u0000\u0000\u01c8\u01c9"
                    + "\u0005\u001d\u0000\u0000\u01c9\u01ca\u0005,\u0000\u0000\u01ca\u01cc\u0005"
                    + "\u001e\u0000\u0000\u01cb\u01c3\u0001\u0000\u0000\u0000\u01cb\u01c7\u0001"
                    + "\u0000\u0000\u0000\u01ccQ\u0001\u0000\u0000\u0000\u01cd\u01ce\u0005\u000e"
                    + "\u0000\u0000\u01ce\u01cf\u0005\u001d\u0000\u0000\u01cf\u01d0\u00054\u0000"
                    + "\u0000\u01d0\u01d6\u0005\u001e\u0000\u0000\u01d1\u01d2\u0005\u000e\u0000"
                    + "\u0000\u01d2\u01d3\u0005\u001d\u0000\u0000\u01d3\u01d4\u0005,\u0000\u0000"
                    + "\u01d4\u01d6\u0005\u001e\u0000\u0000\u01d5\u01cd\u0001\u0000\u0000\u0000"
                    + "\u01d5\u01d1\u0001\u0000\u0000\u0000\u01d6S\u0001\u0000\u0000\u0000\u01d7"
                    + "\u01dc\u0003Z-\u0000\u01d8\u01dc\u0003\\.\u0000\u01d9\u01dc\u0003X,\u0000"
                    + "\u01da\u01dc\u0003V+\u0000\u01db\u01d7\u0001\u0000\u0000\u0000\u01db\u01d8"
                    + "\u0001\u0000\u0000\u0000\u01db\u01d9\u0001\u0000\u0000\u0000\u01db\u01da"
                    + "\u0001\u0000\u0000\u0000\u01dcU\u0001\u0000\u0000\u0000\u01dd\u01de\u0005"
                    + "4\u0000\u0000\u01de\u01e2\u0005\u001a\u0000\u0000\u01df\u01e1\u0003\u001c"
                    + "\u000e\u0000\u01e0\u01df\u0001\u0000\u0000\u0000\u01e1\u01e4\u0001\u0000"
                    + "\u0000\u0000\u01e2\u01e0\u0001\u0000\u0000\u0000\u01e2\u01e3\u0001\u0000"
                    + "\u0000\u0000\u01e3W\u0001\u0000\u0000\u0000\u01e4\u01e2\u0001\u0000\u0000"
                    + "\u0000\u01e5\u01e6\u00054\u0000\u0000\u01e6\u01e7\u0005\u001a\u0000\u0000"
                    + "\u01e7\u01e8\u0005-\u0000\u0000\u01e8Y\u0001\u0000\u0000\u0000\u01e9\u01ea"
                    + "\u00054\u0000\u0000\u01ea\u01eb\u0005\u001a\u0000\u0000\u01eb\u01ec\u0005"
                    + "(\u0000\u0000\u01ec[\u0001\u0000\u0000\u0000\u01ed\u01ee\u00054\u0000"
                    + "\u0000\u01ee\u01ef\u0005\u001a\u0000\u0000\u01ef\u01f0\u0005+\u0000\u0000"
                    + "\u01f0]\u0001\u0000\u0000\u0000+`dptz\u0083\u0087\u008e\u0096\u00a2\u00ae"
                    + "\u00ba\u00c5\u00d0\u00d3\u00d9\u00e4\u00e8\u00f6\u0106\u010e\u0113\u0118"
                    + "\u0121\u012a\u0132\u0138\u013f\u0143\u014f\u0159\u018f\u01a2\u01a6\u01ab"
                    + "\u01af\u01b3\u01b9\u01c1\u01cb\u01d5\u01db\u01e2";
    public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
    private static final String[] _LITERAL_NAMES = makeLiteralNames();
    private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
    public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

    static {
        RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION);
    }

    static {
        tokenNames = new String[_SYMBOLIC_NAMES.length];
        for (int i = 0; i < tokenNames.length; i++) {
            tokenNames[i] = VOCABULARY.getLiteralName(i);
            if (tokenNames[i] == null) {
                tokenNames[i] = VOCABULARY.getSymbolicName(i);
            }

            if (tokenNames[i] == null) {
                tokenNames[i] = "<INVALID>";
            }
        }
    }

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }

    public DoplerParser(TokenStream input) {
        super(input);
        _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    private static String[] makeRuleNames() {
        return new String[] {
            "document",
            "jsonDocument",
            "jsonValue",
            "jsonObject",
            "jsonPair",
            "csvFile",
            "hdr",
            "row",
            "field",
            "header",
            "id",
            "cardinality",
            "question",
            "range",
            "rangeItem",
            "specialCharacter",
            "subrange",
            "expression",
            "unaryExpression",
            "decisionVisibilityCallExpression",
            "isTaken",
            "decisionValueCallExpression",
            "binaryExpression",
            "andExpression",
            "orExpression",
            "xorExpression",
            "equalityExpression",
            "greaterThanExpression",
            "lessThanExpression",
            "greaterEqualsExpression",
            "lessEqualsExpression",
            "literalExpression",
            "enumerationLiteralExpression",
            "booleanLiteralExpression",
            "stringLiteralExpression",
            "doubleLiteralExpression",
            "decisionType",
            "drule",
            "action",
            "valueRestrictionAction",
            "allows",
            "disallows",
            "enForce",
            "enumEnForce",
            "stringEnForce",
            "booleanEnForce",
            "doubleEnForce"
        };
    }

    private static String[] makeLiteralNames() {
        return new String[] {
            null,
            null,
            null,
            "'\"Question\":'",
            null,
            "'Type'",
            "'Range'",
            "'Cardinality'",
            "'Constraint/Rule'",
            "'Visible/relevant if'",
            "'DOPLER'",
            null,
            null,
            null,
            null,
            null,
            null,
            "'=='",
            "'>'",
            "'<'",
            "'<='",
            "'>='",
            "'||'",
            "'&&'",
            "'^'",
            "'|'",
            "'='",
            "'!'",
            "'-'",
            "'('",
            "')'",
            "'{'",
            "'}'",
            "':'",
            "'if'",
            "'&'",
            "'%'",
            "','",
            "';'",
            "'\"'",
            null,
            null,
            null,
            null,
            null,
            null,
            "'String'",
            "'Double'",
            null,
            "'Enumeration'",
            "'Boolean'"
        };
    }

    private static String[] makeSymbolicNames() {
        return new String[] {
            null,
            "CR",
            "LF",
            "QUESTION_KEY",
            "QUESTION",
            "TYPE_KEY",
            "RANGE_KEY",
            "CARDINALITY_KEY",
            "CONSTRAINT_RULE_KEY",
            "VISIBLE_RELEVANT_KEY",
            "DOPLER_KEY",
            "WS_DEFAULT",
            "WS_JSON",
            "ALLOW",
            "DISALLOW",
            "ISTAKEN",
            "GETVALUE",
            "EQUALS",
            "GREATER_THAN",
            "LESS_THAN",
            "LESS_EQUALS",
            "GREATER_EQUALS",
            "OR",
            "AND",
            "XOR",
            "PIPE",
            "SET",
            "NOT",
            "MINUS",
            "LPAREN",
            "RPAREN",
            "LBRACE",
            "RBRACE",
            "COLON",
            "IF",
            "ANPERSAND",
            "PERCENT",
            "COMMA",
            "SEMICOLON",
            "DQ",
            "BooleanLiteralExpression",
            "TRUE",
            "FALSE",
            "DoubleLiteralExpression",
            "EnumerationLiteralExpression",
            "StringLiteralExpression",
            "StringDecision",
            "NumberDecision",
            "EnumerationDecision",
            "ENUMERATION",
            "BooleanDecision",
            "SPECIAL_CHAR",
            "IDENTIFIER"
        };
    }

    @Override
    @Deprecated
    public String[] getTokenNames() {
        return tokenNames;
    }

    @Override
    public Vocabulary getVocabulary() {
        return VOCABULARY;
    }

    @Override
    public String getGrammarFileName() {
        return "DoplerParser.g4";
    }

    @Override
    public String[] getRuleNames() {
        return ruleNames;
    }

    @Override
    public String getSerializedATN() {
        return _serializedATN;
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }

    public final DocumentContext document() throws RecognitionException {
        DocumentContext _localctx = new DocumentContext(_ctx, getState());
        enterRule(_localctx, 0, RULE_document);
        try {
            setState(96);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 0, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(94);
                        jsonDocument();
                    }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(95);
                        csvFile();
                    }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final JsonDocumentContext jsonDocument() throws RecognitionException {
        JsonDocumentContext _localctx = new JsonDocumentContext(_ctx, getState());
        enterRule(_localctx, 2, RULE_jsonDocument);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(98);
                match(LBRACE);
                setState(100);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == LBRACE || _la == DQ) {
                    {
                        setState(99);
                        jsonValue();
                    }
                }

                setState(102);
                match(RBRACE);
                setState(103);
                match(EOF);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final JsonValueContext jsonValue() throws RecognitionException {
        JsonValueContext _localctx = new JsonValueContext(_ctx, getState());
        enterRule(_localctx, 4, RULE_jsonValue);
        int _la;
        try {
            setState(122);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case DQ:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(105);
                        match(DQ);
                        setState(106);
                        match(DOPLER_KEY);
                        setState(107);
                        match(DQ);
                        setState(108);
                        match(COLON);
                        setState(109);
                        match(LBRACE);
                        setState(116);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while (_la == LBRACE || _la == DQ) {
                            {
                                {
                                    setState(110);
                                    jsonObject();
                                    setState(112);
                                    _errHandler.sync(this);
                                    _la = _input.LA(1);
                                    if (_la == COMMA) {
                                        {
                                            setState(111);
                                            match(COMMA);
                                        }
                                    }
                                }
                            }
                            setState(118);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                        }
                        setState(119);
                        match(RBRACE);
                    }
                    break;
                case LBRACE:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(120);
                        match(LBRACE);
                        setState(121);
                        match(RBRACE);
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final JsonObjectContext jsonObject() throws RecognitionException {
        JsonObjectContext _localctx = new JsonObjectContext(_ctx, getState());
        enterRule(_localctx, 6, RULE_jsonObject);
        int _la;
        try {
            setState(142);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case DQ:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(124);
                        match(DQ);
                        setState(125);
                        id();
                        setState(126);
                        match(DQ);
                        setState(127);
                        match(COLON);
                        setState(128);
                        match(LBRACE);
                        setState(135);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while (_la == QUESTION_KEY || _la == DQ) {
                            {
                                {
                                    setState(129);
                                    jsonPair();
                                    setState(131);
                                    _errHandler.sync(this);
                                    _la = _input.LA(1);
                                    if (_la == COMMA) {
                                        {
                                            setState(130);
                                            match(COMMA);
                                        }
                                    }
                                }
                            }
                            setState(137);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                        }
                        setState(138);
                        match(RBRACE);
                    }
                    break;
                case LBRACE:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(140);
                        match(LBRACE);
                        setState(141);
                        match(RBRACE);
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final JsonPairContext jsonPair() throws RecognitionException {
        JsonPairContext _localctx = new JsonPairContext(_ctx, getState());
        enterRule(_localctx, 8, RULE_jsonPair);
        try {
            setState(211);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 14, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(144);
                        match(QUESTION_KEY);
                        setState(145);
                        match(DQ);
                        setState(150);
                        _errHandler.sync(this);
                        switch (getInterpreter().adaptivePredict(_input, 8, _ctx)) {
                            case 1:
                                {
                                    setState(146);
                                    question();
                                }
                                break;
                            case 2:
                                {
                                    setState(147);
                                    match(DQ);
                                    setState(148);
                                    match(DQ);
                                }
                                break;
                            case 3:
                                {
                                }
                                break;
                        }
                        setState(152);
                        match(DQ);
                    }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(153);
                        match(DQ);
                        setState(154);
                        match(TYPE_KEY);
                        setState(155);
                        match(DQ);
                        setState(156);
                        match(COLON);
                        setState(157);
                        match(DQ);
                        setState(162);
                        _errHandler.sync(this);
                        switch (getInterpreter().adaptivePredict(_input, 9, _ctx)) {
                            case 1:
                                {
                                    setState(158);
                                    decisionType();
                                }
                                break;
                            case 2:
                                {
                                    setState(159);
                                    match(DQ);
                                    setState(160);
                                    match(DQ);
                                }
                                break;
                            case 3:
                                {
                                }
                                break;
                        }
                        setState(164);
                        match(DQ);
                    }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                    {
                        setState(165);
                        match(DQ);
                        setState(166);
                        match(RANGE_KEY);
                        setState(167);
                        match(DQ);
                        setState(168);
                        match(COLON);
                        setState(169);
                        match(DQ);
                        setState(174);
                        _errHandler.sync(this);
                        switch (getInterpreter().adaptivePredict(_input, 10, _ctx)) {
                            case 1:
                                {
                                    setState(170);
                                    range();
                                }
                                break;
                            case 2:
                                {
                                    setState(171);
                                    match(DQ);
                                    setState(172);
                                    match(DQ);
                                }
                                break;
                            case 3:
                                {
                                }
                                break;
                        }
                        setState(176);
                        match(DQ);
                    }
                    break;
                case 4:
                    enterOuterAlt(_localctx, 4);
                    {
                        setState(177);
                        match(DQ);
                        setState(178);
                        match(CARDINALITY_KEY);
                        setState(179);
                        match(DQ);
                        setState(180);
                        match(COLON);
                        setState(181);
                        match(DQ);
                        setState(186);
                        _errHandler.sync(this);
                        switch (getInterpreter().adaptivePredict(_input, 11, _ctx)) {
                            case 1:
                                {
                                    setState(182);
                                    cardinality();
                                }
                                break;
                            case 2:
                                {
                                    setState(183);
                                    match(DQ);
                                    setState(184);
                                    match(DQ);
                                }
                                break;
                            case 3:
                                {
                                }
                                break;
                        }
                        setState(188);
                        match(DQ);
                    }
                    break;
                case 5:
                    enterOuterAlt(_localctx, 5);
                    {
                        setState(189);
                        match(DQ);
                        setState(190);
                        match(CONSTRAINT_RULE_KEY);
                        setState(191);
                        match(DQ);
                        setState(192);
                        match(COLON);
                        setState(197);
                        _errHandler.sync(this);
                        switch (getInterpreter().adaptivePredict(_input, 12, _ctx)) {
                            case 1:
                                {
                                    setState(193);
                                    drule(0);
                                }
                                break;
                            case 2:
                                {
                                    setState(194);
                                    match(DQ);
                                    setState(195);
                                    match(DQ);
                                }
                                break;
                            case 3:
                                {
                                }
                                break;
                        }
                    }
                    break;
                case 6:
                    enterOuterAlt(_localctx, 6);
                    {
                        setState(199);
                        match(DQ);
                        setState(200);
                        match(VISIBLE_RELEVANT_KEY);
                        setState(201);
                        match(DQ);
                        setState(202);
                        match(COLON);
                        setState(203);
                        match(DQ);
                        setState(208);
                        _errHandler.sync(this);
                        switch (getInterpreter().adaptivePredict(_input, 13, _ctx)) {
                            case 1:
                                {
                                    setState(204);
                                    decisionVisibilityCallExpression();
                                }
                                break;
                            case 2:
                                {
                                    setState(205);
                                    match(DQ);
                                    setState(206);
                                    match(DQ);
                                }
                                break;
                            case 3:
                                {
                                }
                                break;
                        }
                        setState(210);
                        match(DQ);
                    }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final CsvFileContext csvFile() throws RecognitionException {
        CsvFileContext _localctx = new CsvFileContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_csvFile);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(213);
                hdr();
                setState(215);
                _errHandler.sync(this);
                _la = _input.LA(1);
                do {
                    {
                        {
                            setState(214);
                            row();
                        }
                    }
                    setState(217);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 8437651828999166L) != 0));
                setState(219);
                match(EOF);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final HdrContext hdr() throws RecognitionException {
        HdrContext _localctx = new HdrContext(_ctx, getState());
        enterRule(_localctx, 12, RULE_hdr);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(221);
                row();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final RowContext row() throws RecognitionException {
        RowContext _localctx = new RowContext(_ctx, getState());
        enterRule(_localctx, 14, RULE_row);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(223);
                field();
                setState(228);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == SEMICOLON) {
                    {
                        {
                            setState(224);
                            match(SEMICOLON);
                            setState(225);
                            field();
                        }
                    }
                    setState(230);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(232);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == CR) {
                    {
                        setState(231);
                        match(CR);
                    }
                }

                setState(234);
                match(LF);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final FieldContext field() throws RecognitionException {
        FieldContext _localctx = new FieldContext(_ctx, getState());
        enterRule(_localctx, 16, RULE_field);
        try {
            setState(246);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 18, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(236);
                        drule(0);
                    }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(237);
                        id();
                    }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                    {
                        setState(238);
                        question();
                    }
                    break;
                case 4:
                    enterOuterAlt(_localctx, 4);
                    {
                        setState(239);
                        decisionType();
                    }
                    break;
                case 5:
                    enterOuterAlt(_localctx, 5);
                    {
                        setState(240);
                        cardinality();
                    }
                    break;
                case 6:
                    enterOuterAlt(_localctx, 6);
                    {
                        setState(241);
                        range();
                    }
                    break;
                case 7:
                    enterOuterAlt(_localctx, 7);
                    {
                        setState(242);
                        decisionVisibilityCallExpression();
                    }
                    break;
                case 8:
                    enterOuterAlt(_localctx, 8);
                    {
                        setState(243);
                        header();
                    }
                    break;
                case 9:
                    enterOuterAlt(_localctx, 9);
                    {
                        setState(244);
                        match(IDENTIFIER);
                    }
                    break;
                case 10:
                    enterOuterAlt(_localctx, 10);
                    {
                    }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final HeaderContext header() throws RecognitionException {
        HeaderContext _localctx = new HeaderContext(_ctx, getState());
        enterRule(_localctx, 18, RULE_header);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(248);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 1000L) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final IdContext id() throws RecognitionException {
        IdContext _localctx = new IdContext(_ctx, getState());
        enterRule(_localctx, 20, RULE_id);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(250);
                match(IDENTIFIER);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final CardinalityContext cardinality() throws RecognitionException {
        CardinalityContext _localctx = new CardinalityContext(_ctx, getState());
        enterRule(_localctx, 22, RULE_cardinality);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(252);
                match(DoubleLiteralExpression);
                setState(253);
                match(COLON);
                setState(254);
                match(DoubleLiteralExpression);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final QuestionContext question() throws RecognitionException {
        QuestionContext _localctx = new QuestionContext(_ctx, getState());
        enterRule(_localctx, 24, RULE_question);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(256);
                match(QUESTION);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final RangeContext range() throws RecognitionException {
        RangeContext _localctx = new RangeContext(_ctx, getState());
        enterRule(_localctx, 26, RULE_range);
        int _la;
        try {
            setState(280);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 22, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(258);
                        rangeItem();
                        setState(262);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 6818328899322872L) != 0)) {
                            {
                                {
                                    setState(259);
                                    rangeItem();
                                }
                            }
                            setState(264);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                        }
                        setState(273);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        do {
                            {
                                {
                                    setState(265);
                                    match(PIPE);
                                    setState(266);
                                    rangeItem();
                                    setState(270);
                                    _errHandler.sync(this);
                                    _la = _input.LA(1);
                                    while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 6818328899322872L) != 0)) {
                                        {
                                            {
                                                setState(267);
                                                rangeItem();
                                            }
                                        }
                                        setState(272);
                                        _errHandler.sync(this);
                                        _la = _input.LA(1);
                                    }
                                }
                            }
                            setState(275);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                        } while (_la == PIPE);
                    }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(277);
                        match(DoubleLiteralExpression);
                        setState(278);
                        match(MINUS);
                        setState(279);
                        match(DoubleLiteralExpression);
                    }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final RangeItemContext rangeItem() throws RecognitionException {
        RangeItemContext _localctx = new RangeItemContext(_ctx, getState());
        enterRule(_localctx, 28, RULE_rangeItem);
        try {
            setState(289);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 23, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(282);
                        match(IDENTIFIER);
                    }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(283);
                        specialCharacter();
                    }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                    {
                        setState(284);
                        expression();
                    }
                    break;
                case 4:
                    enterOuterAlt(_localctx, 4);
                    {
                        setState(285);
                        subrange();
                    }
                    break;
                case 5:
                    enterOuterAlt(_localctx, 5);
                    {
                        setState(286);
                        cardinality();
                    }
                    break;
                case 6:
                    enterOuterAlt(_localctx, 6);
                    {
                        setState(287);
                        match(QUESTION);
                    }
                    break;
                case 7:
                    enterOuterAlt(_localctx, 7);
                    {
                        setState(288);
                        header();
                    }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final SpecialCharacterContext specialCharacter() throws RecognitionException {
        SpecialCharacterContext _localctx = new SpecialCharacterContext(_ctx, getState());
        enterRule(_localctx, 30, RULE_specialCharacter);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(291);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 2252056974852096L) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final SubrangeContext subrange() throws RecognitionException {
        SubrangeContext _localctx = new SubrangeContext(_ctx, getState());
        enterRule(_localctx, 32, RULE_subrange);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(293);
                expression();
                setState(298);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 16646144L) != 0)) {
                    {
                        {
                            setState(294);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 16646144L) != 0))) {
                                _errHandler.recoverInline(this);
                            } else {
                                if (_input.LA(1) == Token.EOF) matchedEOF = true;
                                _errHandler.reportMatch(this);
                                consume();
                            }
                            setState(295);
                            expression();
                        }
                    }
                    setState(300);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final ExpressionContext expression() throws RecognitionException {
        ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
        enterRule(_localctx, 34, RULE_expression);
        try {
            setState(306);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case NOT:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(301);
                        unaryExpression();
                    }
                    break;
                case BooleanLiteralExpression:
                case DoubleLiteralExpression:
                case EnumerationLiteralExpression:
                case StringLiteralExpression:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(302);
                        literalExpression();
                    }
                    break;
                case ISTAKEN:
                    enterOuterAlt(_localctx, 3);
                    {
                        setState(303);
                        isTaken();
                    }
                    break;
                case GETVALUE:
                case IDENTIFIER:
                    enterOuterAlt(_localctx, 4);
                    {
                        setState(304);
                        decisionValueCallExpression();
                    }
                    break;
                case LPAREN:
                    enterOuterAlt(_localctx, 5);
                    {
                        setState(305);
                        binaryExpression();
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final UnaryExpressionContext unaryExpression() throws RecognitionException {
        UnaryExpressionContext _localctx = new UnaryExpressionContext(_ctx, getState());
        enterRule(_localctx, 36, RULE_unaryExpression);
        try {
            setState(312);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 26, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(308);
                        match(NOT);
                        setState(309);
                        expression();
                    }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(310);
                        match(NOT);
                        setState(311);
                        match(EnumerationLiteralExpression);
                    }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final DecisionVisibilityCallExpressionContext decisionVisibilityCallExpression()
            throws RecognitionException {
        DecisionVisibilityCallExpressionContext _localctx =
                new DecisionVisibilityCallExpressionContext(_ctx, getState());
        enterRule(_localctx, 38, RULE_decisionVisibilityCallExpression);
        int _la;
        try {
            setState(323);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 28, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(314);
                        expression();
                        setState(319);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 13500416L) != 0)) {
                            {
                                {
                                    setState(315);
                                    _la = _input.LA(1);
                                    if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 13500416L) != 0))) {
                                        _errHandler.recoverInline(this);
                                    } else {
                                        if (_input.LA(1) == Token.EOF) matchedEOF = true;
                                        _errHandler.reportMatch(this);
                                        consume();
                                    }
                                    setState(316);
                                    expression();
                                }
                            }
                            setState(321);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                        }
                    }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(322);
                        expression();
                    }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final IsTakenContext isTaken() throws RecognitionException {
        IsTakenContext _localctx = new IsTakenContext(_ctx, getState());
        enterRule(_localctx, 40, RULE_isTaken);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(325);
                match(ISTAKEN);
                setState(326);
                match(LPAREN);
                setState(327);
                match(IDENTIFIER);
                setState(328);
                match(RPAREN);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final DecisionValueCallExpressionContext decisionValueCallExpression() throws RecognitionException {
        DecisionValueCallExpressionContext _localctx = new DecisionValueCallExpressionContext(_ctx, getState());
        enterRule(_localctx, 42, RULE_decisionValueCallExpression);
        try {
            setState(335);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case GETVALUE:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(330);
                        match(GETVALUE);
                        setState(331);
                        match(LPAREN);
                        setState(332);
                        match(IDENTIFIER);
                        setState(333);
                        match(RPAREN);
                    }
                    break;
                case IDENTIFIER:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(334);
                        match(IDENTIFIER);
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final BinaryExpressionContext binaryExpression() throws RecognitionException {
        BinaryExpressionContext _localctx = new BinaryExpressionContext(_ctx, getState());
        enterRule(_localctx, 44, RULE_binaryExpression);
        try {
            setState(345);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 30, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(337);
                        andExpression();
                    }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(338);
                        orExpression();
                    }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                    {
                        setState(339);
                        xorExpression();
                    }
                    break;
                case 4:
                    enterOuterAlt(_localctx, 4);
                    {
                        setState(340);
                        equalityExpression();
                    }
                    break;
                case 5:
                    enterOuterAlt(_localctx, 5);
                    {
                        setState(341);
                        greaterThanExpression();
                    }
                    break;
                case 6:
                    enterOuterAlt(_localctx, 6);
                    {
                        setState(342);
                        lessThanExpression();
                    }
                    break;
                case 7:
                    enterOuterAlt(_localctx, 7);
                    {
                        setState(343);
                        greaterEqualsExpression();
                    }
                    break;
                case 8:
                    enterOuterAlt(_localctx, 8);
                    {
                        setState(344);
                        lessEqualsExpression();
                    }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final AndExpressionContext andExpression() throws RecognitionException {
        AndExpressionContext _localctx = new AndExpressionContext(_ctx, getState());
        enterRule(_localctx, 46, RULE_andExpression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(347);
                match(LPAREN);
                setState(348);
                expression();
                setState(349);
                match(AND);
                setState(350);
                expression();
                setState(351);
                match(RPAREN);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final OrExpressionContext orExpression() throws RecognitionException {
        OrExpressionContext _localctx = new OrExpressionContext(_ctx, getState());
        enterRule(_localctx, 48, RULE_orExpression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(353);
                match(LPAREN);
                setState(354);
                expression();
                setState(355);
                match(OR);
                setState(356);
                expression();
                setState(357);
                match(RPAREN);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final XorExpressionContext xorExpression() throws RecognitionException {
        XorExpressionContext _localctx = new XorExpressionContext(_ctx, getState());
        enterRule(_localctx, 50, RULE_xorExpression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(359);
                match(LPAREN);
                setState(360);
                expression();
                setState(361);
                match(XOR);
                setState(362);
                expression();
                setState(363);
                match(RPAREN);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final EqualityExpressionContext equalityExpression() throws RecognitionException {
        EqualityExpressionContext _localctx = new EqualityExpressionContext(_ctx, getState());
        enterRule(_localctx, 52, RULE_equalityExpression);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(365);
                match(LPAREN);
                setState(366);
                expression();
                setState(367);
                _la = _input.LA(1);
                if (!(_la == EQUALS || _la == SET)) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
                setState(368);
                expression();
                setState(369);
                match(RPAREN);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final GreaterThanExpressionContext greaterThanExpression() throws RecognitionException {
        GreaterThanExpressionContext _localctx = new GreaterThanExpressionContext(_ctx, getState());
        enterRule(_localctx, 54, RULE_greaterThanExpression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(371);
                match(LPAREN);
                setState(372);
                expression();
                setState(373);
                match(GREATER_THAN);
                setState(374);
                expression();
                setState(375);
                match(RPAREN);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final LessThanExpressionContext lessThanExpression() throws RecognitionException {
        LessThanExpressionContext _localctx = new LessThanExpressionContext(_ctx, getState());
        enterRule(_localctx, 56, RULE_lessThanExpression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(377);
                match(LPAREN);
                setState(378);
                expression();
                setState(379);
                match(LESS_THAN);
                setState(380);
                expression();
                setState(381);
                match(RPAREN);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final GreaterEqualsExpressionContext greaterEqualsExpression() throws RecognitionException {
        GreaterEqualsExpressionContext _localctx = new GreaterEqualsExpressionContext(_ctx, getState());
        enterRule(_localctx, 58, RULE_greaterEqualsExpression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(383);
                match(LPAREN);
                setState(384);
                expression();
                setState(385);
                match(GREATER_EQUALS);
                setState(386);
                expression();
                setState(387);
                match(RPAREN);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final LessEqualsExpressionContext lessEqualsExpression() throws RecognitionException {
        LessEqualsExpressionContext _localctx = new LessEqualsExpressionContext(_ctx, getState());
        enterRule(_localctx, 60, RULE_lessEqualsExpression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(389);
                match(LPAREN);
                setState(390);
                expression();
                setState(391);
                match(LESS_EQUALS);
                setState(392);
                expression();
                setState(393);
                match(RPAREN);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final LiteralExpressionContext literalExpression() throws RecognitionException {
        LiteralExpressionContext _localctx = new LiteralExpressionContext(_ctx, getState());
        enterRule(_localctx, 62, RULE_literalExpression);
        try {
            setState(399);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case EnumerationLiteralExpression:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(395);
                        enumerationLiteralExpression();
                    }
                    break;
                case BooleanLiteralExpression:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(396);
                        booleanLiteralExpression();
                    }
                    break;
                case StringLiteralExpression:
                    enterOuterAlt(_localctx, 3);
                    {
                        setState(397);
                        stringLiteralExpression();
                    }
                    break;
                case DoubleLiteralExpression:
                    enterOuterAlt(_localctx, 4);
                    {
                        setState(398);
                        doubleLiteralExpression();
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final EnumerationLiteralExpressionContext enumerationLiteralExpression() throws RecognitionException {
        EnumerationLiteralExpressionContext _localctx = new EnumerationLiteralExpressionContext(_ctx, getState());
        enterRule(_localctx, 64, RULE_enumerationLiteralExpression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(401);
                match(EnumerationLiteralExpression);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final BooleanLiteralExpressionContext booleanLiteralExpression() throws RecognitionException {
        BooleanLiteralExpressionContext _localctx = new BooleanLiteralExpressionContext(_ctx, getState());
        enterRule(_localctx, 66, RULE_booleanLiteralExpression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(403);
                match(BooleanLiteralExpression);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final StringLiteralExpressionContext stringLiteralExpression() throws RecognitionException {
        StringLiteralExpressionContext _localctx = new StringLiteralExpressionContext(_ctx, getState());
        enterRule(_localctx, 68, RULE_stringLiteralExpression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(405);
                match(StringLiteralExpression);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final DoubleLiteralExpressionContext doubleLiteralExpression() throws RecognitionException {
        DoubleLiteralExpressionContext _localctx = new DoubleLiteralExpressionContext(_ctx, getState());
        enterRule(_localctx, 70, RULE_doubleLiteralExpression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(407);
                match(DoubleLiteralExpression);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final DecisionTypeContext decisionType() throws RecognitionException {
        DecisionTypeContext _localctx = new DecisionTypeContext(_ctx, getState());
        enterRule(_localctx, 72, RULE_decisionType);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(409);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 1618481116086272L) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final DruleContext drule() throws RecognitionException {
        return drule(0);
    }

    private DruleContext drule(int _p) throws RecognitionException {
        ParserRuleContext _parentctx = _ctx;
        int _parentState = getState();
        DruleContext _localctx = new DruleContext(_ctx, _parentState);
        DruleContext _prevctx = _localctx;
        int _startState = 74;
        enterRecursionRule(_localctx, 74, RULE_drule, _p);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(435);
                _errHandler.sync(this);
                switch (_input.LA(1)) {
                    case DQ:
                        {
                            setState(412);
                            match(DQ);
                            setState(413);
                            drule(0);
                            setState(414);
                            match(DQ);
                        }
                        break;
                    case IF:
                        {
                            setState(416);
                            match(IF);
                            setState(418);
                            _errHandler.sync(this);
                            switch (getInterpreter().adaptivePredict(_input, 32, _ctx)) {
                                case 1:
                                    {
                                        setState(417);
                                        match(LPAREN);
                                    }
                                    break;
                            }
                            setState(420);
                            expression();
                            setState(422);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                            if (_la == RPAREN) {
                                {
                                    setState(421);
                                    match(RPAREN);
                                }
                            }

                            setState(424);
                            match(LBRACE);
                            setState(429);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                            do {
                                {
                                    {
                                        setState(425);
                                        action();
                                        setState(427);
                                        _errHandler.sync(this);
                                        _la = _input.LA(1);
                                        if (_la == SEMICOLON) {
                                            {
                                                setState(426);
                                                match(SEMICOLON);
                                            }
                                        }
                                    }
                                }
                                setState(431);
                                _errHandler.sync(this);
                                _la = _input.LA(1);
                            } while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4503599627395072L) != 0));
                            setState(433);
                            match(RBRACE);
                        }
                        break;
                    default:
                        throw new NoViableAltException(this);
                }
                _ctx.stop = _input.LT(-1);
                setState(441);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 37, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        if (_parseListeners != null) triggerExitRuleEvent();
                        _prevctx = _localctx;
                        {
                            {
                                _localctx = new DruleContext(_parentctx, _parentState);
                                pushNewRecursionContext(_localctx, _startState, RULE_drule);
                                setState(437);
                                if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
                                setState(438);
                                drule(3);
                            }
                        }
                    }
                    setState(443);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 37, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            unrollRecursionContexts(_parentctx);
        }
        return _localctx;
    }

    public final ActionContext action() throws RecognitionException {
        ActionContext _localctx = new ActionContext(_ctx, getState());
        enterRule(_localctx, 76, RULE_action);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(444);
                valueRestrictionAction();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final ValueRestrictionActionContext valueRestrictionAction() throws RecognitionException {
        ValueRestrictionActionContext _localctx = new ValueRestrictionActionContext(_ctx, getState());
        enterRule(_localctx, 78, RULE_valueRestrictionAction);
        try {
            setState(449);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case ALLOW:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(446);
                        allows();
                    }
                    break;
                case DISALLOW:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(447);
                        disallows();
                    }
                    break;
                case IDENTIFIER:
                    enterOuterAlt(_localctx, 3);
                    {
                        setState(448);
                        enForce();
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final AllowsContext allows() throws RecognitionException {
        AllowsContext _localctx = new AllowsContext(_ctx, getState());
        enterRule(_localctx, 80, RULE_allows);
        try {
            setState(459);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 39, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(451);
                        match(ALLOW);
                        setState(452);
                        match(LPAREN);
                        setState(453);
                        match(IDENTIFIER);
                        setState(454);
                        match(RPAREN);
                    }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(455);
                        match(ALLOW);
                        setState(456);
                        match(LPAREN);
                        setState(457);
                        match(EnumerationLiteralExpression);
                        setState(458);
                        match(RPAREN);
                    }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final DisallowsContext disallows() throws RecognitionException {
        DisallowsContext _localctx = new DisallowsContext(_ctx, getState());
        enterRule(_localctx, 82, RULE_disallows);
        try {
            setState(469);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 40, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(461);
                        match(DISALLOW);
                        setState(462);
                        match(LPAREN);
                        setState(463);
                        match(IDENTIFIER);
                        setState(464);
                        match(RPAREN);
                    }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(465);
                        match(DISALLOW);
                        setState(466);
                        match(LPAREN);
                        setState(467);
                        match(EnumerationLiteralExpression);
                        setState(468);
                        match(RPAREN);
                    }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final EnForceContext enForce() throws RecognitionException {
        EnForceContext _localctx = new EnForceContext(_ctx, getState());
        enterRule(_localctx, 84, RULE_enForce);
        try {
            setState(475);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 41, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(471);
                        booleanEnForce();
                    }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(472);
                        doubleEnForce();
                    }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                    {
                        setState(473);
                        stringEnForce();
                    }
                    break;
                case 4:
                    enterOuterAlt(_localctx, 4);
                    {
                        setState(474);
                        enumEnForce();
                    }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final EnumEnForceContext enumEnForce() throws RecognitionException {
        EnumEnForceContext _localctx = new EnumEnForceContext(_ctx, getState());
        enterRule(_localctx, 86, RULE_enumEnForce);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(477);
                match(IDENTIFIER);
                setState(478);
                match(SET);
                setState(482);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 42, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(479);
                                rangeItem();
                            }
                        }
                    }
                    setState(484);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 42, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final StringEnForceContext stringEnForce() throws RecognitionException {
        StringEnForceContext _localctx = new StringEnForceContext(_ctx, getState());
        enterRule(_localctx, 88, RULE_stringEnForce);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(485);
                match(IDENTIFIER);
                setState(486);
                match(SET);
                setState(487);
                match(StringLiteralExpression);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final BooleanEnForceContext booleanEnForce() throws RecognitionException {
        BooleanEnForceContext _localctx = new BooleanEnForceContext(_ctx, getState());
        enterRule(_localctx, 90, RULE_booleanEnForce);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(489);
                match(IDENTIFIER);
                setState(490);
                match(SET);
                setState(491);
                match(BooleanLiteralExpression);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public final DoubleEnForceContext doubleEnForce() throws RecognitionException {
        DoubleEnForceContext _localctx = new DoubleEnForceContext(_ctx, getState());
        enterRule(_localctx, 92, RULE_doubleEnForce);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(493);
                match(IDENTIFIER);
                setState(494);
                match(SET);
                setState(495);
                match(DoubleLiteralExpression);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
        switch (ruleIndex) {
            case 37:
                return drule_sempred((DruleContext) _localctx, predIndex);
        }
        return true;
    }

    private boolean drule_sempred(DruleContext _localctx, int predIndex) {
        switch (predIndex) {
            case 0:
                return precpred(_ctx, 2);
        }
        return true;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class DocumentContext extends ParserRuleContext {
        public DocumentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public JsonDocumentContext jsonDocument() {
            return getRuleContext(JsonDocumentContext.class, 0);
        }

        public CsvFileContext csvFile() {
            return getRuleContext(CsvFileContext.class, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_document;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterDocument(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitDocument(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class JsonDocumentContext extends ParserRuleContext {
        public JsonDocumentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode LBRACE() {
            return getToken(DoplerParser.LBRACE, 0);
        }

        public TerminalNode RBRACE() {
            return getToken(DoplerParser.RBRACE, 0);
        }

        public TerminalNode EOF() {
            return getToken(DoplerParser.EOF, 0);
        }

        public JsonValueContext jsonValue() {
            return getRuleContext(JsonValueContext.class, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_jsonDocument;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterJsonDocument(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitJsonDocument(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class JsonValueContext extends ParserRuleContext {
        public JsonValueContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public List<TerminalNode> DQ() {
            return getTokens(DoplerParser.DQ);
        }

        public TerminalNode DQ(int i) {
            return getToken(DoplerParser.DQ, i);
        }

        public TerminalNode DOPLER_KEY() {
            return getToken(DoplerParser.DOPLER_KEY, 0);
        }

        public TerminalNode COLON() {
            return getToken(DoplerParser.COLON, 0);
        }

        public TerminalNode LBRACE() {
            return getToken(DoplerParser.LBRACE, 0);
        }

        public TerminalNode RBRACE() {
            return getToken(DoplerParser.RBRACE, 0);
        }

        public List<JsonObjectContext> jsonObject() {
            return getRuleContexts(JsonObjectContext.class);
        }

        public JsonObjectContext jsonObject(int i) {
            return getRuleContext(JsonObjectContext.class, i);
        }

        public List<TerminalNode> COMMA() {
            return getTokens(DoplerParser.COMMA);
        }

        public TerminalNode COMMA(int i) {
            return getToken(DoplerParser.COMMA, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_jsonValue;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterJsonValue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitJsonValue(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class JsonObjectContext extends ParserRuleContext {
        public JsonObjectContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public List<TerminalNode> DQ() {
            return getTokens(DoplerParser.DQ);
        }

        public TerminalNode DQ(int i) {
            return getToken(DoplerParser.DQ, i);
        }

        public IdContext id() {
            return getRuleContext(IdContext.class, 0);
        }

        public TerminalNode COLON() {
            return getToken(DoplerParser.COLON, 0);
        }

        public TerminalNode LBRACE() {
            return getToken(DoplerParser.LBRACE, 0);
        }

        public TerminalNode RBRACE() {
            return getToken(DoplerParser.RBRACE, 0);
        }

        public List<JsonPairContext> jsonPair() {
            return getRuleContexts(JsonPairContext.class);
        }

        public JsonPairContext jsonPair(int i) {
            return getRuleContext(JsonPairContext.class, i);
        }

        public List<TerminalNode> COMMA() {
            return getTokens(DoplerParser.COMMA);
        }

        public TerminalNode COMMA(int i) {
            return getToken(DoplerParser.COMMA, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_jsonObject;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterJsonObject(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitJsonObject(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class JsonPairContext extends ParserRuleContext {
        public JsonPairContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode QUESTION_KEY() {
            return getToken(DoplerParser.QUESTION_KEY, 0);
        }

        public List<TerminalNode> DQ() {
            return getTokens(DoplerParser.DQ);
        }

        public TerminalNode DQ(int i) {
            return getToken(DoplerParser.DQ, i);
        }

        public QuestionContext question() {
            return getRuleContext(QuestionContext.class, 0);
        }

        public TerminalNode TYPE_KEY() {
            return getToken(DoplerParser.TYPE_KEY, 0);
        }

        public TerminalNode COLON() {
            return getToken(DoplerParser.COLON, 0);
        }

        public DecisionTypeContext decisionType() {
            return getRuleContext(DecisionTypeContext.class, 0);
        }

        public TerminalNode RANGE_KEY() {
            return getToken(DoplerParser.RANGE_KEY, 0);
        }

        public RangeContext range() {
            return getRuleContext(RangeContext.class, 0);
        }

        public TerminalNode CARDINALITY_KEY() {
            return getToken(DoplerParser.CARDINALITY_KEY, 0);
        }

        public CardinalityContext cardinality() {
            return getRuleContext(CardinalityContext.class, 0);
        }

        public TerminalNode CONSTRAINT_RULE_KEY() {
            return getToken(DoplerParser.CONSTRAINT_RULE_KEY, 0);
        }

        public DruleContext drule() {
            return getRuleContext(DruleContext.class, 0);
        }

        public TerminalNode VISIBLE_RELEVANT_KEY() {
            return getToken(DoplerParser.VISIBLE_RELEVANT_KEY, 0);
        }

        public DecisionVisibilityCallExpressionContext decisionVisibilityCallExpression() {
            return getRuleContext(DecisionVisibilityCallExpressionContext.class, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_jsonPair;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterJsonPair(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitJsonPair(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class CsvFileContext extends ParserRuleContext {
        public CsvFileContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public HdrContext hdr() {
            return getRuleContext(HdrContext.class, 0);
        }

        public TerminalNode EOF() {
            return getToken(DoplerParser.EOF, 0);
        }

        public List<RowContext> row() {
            return getRuleContexts(RowContext.class);
        }

        public RowContext row(int i) {
            return getRuleContext(RowContext.class, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_csvFile;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterCsvFile(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitCsvFile(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class HdrContext extends ParserRuleContext {
        public HdrContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public RowContext row() {
            return getRuleContext(RowContext.class, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_hdr;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterHdr(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitHdr(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class RowContext extends ParserRuleContext {
        public RowContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public List<FieldContext> field() {
            return getRuleContexts(FieldContext.class);
        }

        public FieldContext field(int i) {
            return getRuleContext(FieldContext.class, i);
        }

        public TerminalNode LF() {
            return getToken(DoplerParser.LF, 0);
        }

        public List<TerminalNode> SEMICOLON() {
            return getTokens(DoplerParser.SEMICOLON);
        }

        public TerminalNode SEMICOLON(int i) {
            return getToken(DoplerParser.SEMICOLON, i);
        }

        public TerminalNode CR() {
            return getToken(DoplerParser.CR, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_row;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterRow(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitRow(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class FieldContext extends ParserRuleContext {
        public FieldContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public DruleContext drule() {
            return getRuleContext(DruleContext.class, 0);
        }

        public IdContext id() {
            return getRuleContext(IdContext.class, 0);
        }

        public QuestionContext question() {
            return getRuleContext(QuestionContext.class, 0);
        }

        public DecisionTypeContext decisionType() {
            return getRuleContext(DecisionTypeContext.class, 0);
        }

        public CardinalityContext cardinality() {
            return getRuleContext(CardinalityContext.class, 0);
        }

        public RangeContext range() {
            return getRuleContext(RangeContext.class, 0);
        }

        public DecisionVisibilityCallExpressionContext decisionVisibilityCallExpression() {
            return getRuleContext(DecisionVisibilityCallExpressionContext.class, 0);
        }

        public HeaderContext header() {
            return getRuleContext(HeaderContext.class, 0);
        }

        public TerminalNode IDENTIFIER() {
            return getToken(DoplerParser.IDENTIFIER, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_field;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterField(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitField(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class HeaderContext extends ParserRuleContext {
        public HeaderContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode QUESTION_KEY() {
            return getToken(DoplerParser.QUESTION_KEY, 0);
        }

        public TerminalNode TYPE_KEY() {
            return getToken(DoplerParser.TYPE_KEY, 0);
        }

        public TerminalNode RANGE_KEY() {
            return getToken(DoplerParser.RANGE_KEY, 0);
        }

        public TerminalNode CARDINALITY_KEY() {
            return getToken(DoplerParser.CARDINALITY_KEY, 0);
        }

        public TerminalNode CONSTRAINT_RULE_KEY() {
            return getToken(DoplerParser.CONSTRAINT_RULE_KEY, 0);
        }

        public TerminalNode VISIBLE_RELEVANT_KEY() {
            return getToken(DoplerParser.VISIBLE_RELEVANT_KEY, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_header;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterHeader(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitHeader(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IdContext extends ParserRuleContext {
        public IdContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode IDENTIFIER() {
            return getToken(DoplerParser.IDENTIFIER, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_id;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterId(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitId(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class CardinalityContext extends ParserRuleContext {
        public CardinalityContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public List<TerminalNode> DoubleLiteralExpression() {
            return getTokens(DoplerParser.DoubleLiteralExpression);
        }

        public TerminalNode DoubleLiteralExpression(int i) {
            return getToken(DoplerParser.DoubleLiteralExpression, i);
        }

        public TerminalNode COLON() {
            return getToken(DoplerParser.COLON, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_cardinality;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterCardinality(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitCardinality(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class QuestionContext extends ParserRuleContext {
        public QuestionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode QUESTION() {
            return getToken(DoplerParser.QUESTION, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_question;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterQuestion(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitQuestion(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class RangeContext extends ParserRuleContext {
        public RangeContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public List<RangeItemContext> rangeItem() {
            return getRuleContexts(RangeItemContext.class);
        }

        public RangeItemContext rangeItem(int i) {
            return getRuleContext(RangeItemContext.class, i);
        }

        public List<TerminalNode> PIPE() {
            return getTokens(DoplerParser.PIPE);
        }

        public TerminalNode PIPE(int i) {
            return getToken(DoplerParser.PIPE, i);
        }

        public List<TerminalNode> DoubleLiteralExpression() {
            return getTokens(DoplerParser.DoubleLiteralExpression);
        }

        public TerminalNode DoubleLiteralExpression(int i) {
            return getToken(DoplerParser.DoubleLiteralExpression, i);
        }

        public TerminalNode MINUS() {
            return getToken(DoplerParser.MINUS, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_range;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterRange(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitRange(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class RangeItemContext extends ParserRuleContext {
        public RangeItemContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode IDENTIFIER() {
            return getToken(DoplerParser.IDENTIFIER, 0);
        }

        public SpecialCharacterContext specialCharacter() {
            return getRuleContext(SpecialCharacterContext.class, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public SubrangeContext subrange() {
            return getRuleContext(SubrangeContext.class, 0);
        }

        public CardinalityContext cardinality() {
            return getRuleContext(CardinalityContext.class, 0);
        }

        public TerminalNode QUESTION() {
            return getToken(DoplerParser.QUESTION, 0);
        }

        public HeaderContext header() {
            return getRuleContext(HeaderContext.class, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_rangeItem;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterRangeItem(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitRangeItem(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class SpecialCharacterContext extends ParserRuleContext {
        public SpecialCharacterContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode RPAREN() {
            return getToken(DoplerParser.RPAREN, 0);
        }

        public TerminalNode LPAREN() {
            return getToken(DoplerParser.LPAREN, 0);
        }

        public TerminalNode LBRACE() {
            return getToken(DoplerParser.LBRACE, 0);
        }

        public TerminalNode RBRACE() {
            return getToken(DoplerParser.RBRACE, 0);
        }

        public TerminalNode COLON() {
            return getToken(DoplerParser.COLON, 0);
        }

        public TerminalNode ANPERSAND() {
            return getToken(DoplerParser.ANPERSAND, 0);
        }

        public TerminalNode PERCENT() {
            return getToken(DoplerParser.PERCENT, 0);
        }

        public TerminalNode COMMA() {
            return getToken(DoplerParser.COMMA, 0);
        }

        public TerminalNode SPECIAL_CHAR() {
            return getToken(DoplerParser.SPECIAL_CHAR, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_specialCharacter;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterSpecialCharacter(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitSpecialCharacter(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class SubrangeContext extends ParserRuleContext {
        public SubrangeContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public List<TerminalNode> AND() {
            return getTokens(DoplerParser.AND);
        }

        public TerminalNode AND(int i) {
            return getToken(DoplerParser.AND, i);
        }

        public List<TerminalNode> OR() {
            return getTokens(DoplerParser.OR);
        }

        public TerminalNode OR(int i) {
            return getToken(DoplerParser.OR, i);
        }

        public List<TerminalNode> EQUALS() {
            return getTokens(DoplerParser.EQUALS);
        }

        public TerminalNode EQUALS(int i) {
            return getToken(DoplerParser.EQUALS, i);
        }

        public List<TerminalNode> GREATER_THAN() {
            return getTokens(DoplerParser.GREATER_THAN);
        }

        public TerminalNode GREATER_THAN(int i) {
            return getToken(DoplerParser.GREATER_THAN, i);
        }

        public List<TerminalNode> LESS_THAN() {
            return getTokens(DoplerParser.LESS_THAN);
        }

        public TerminalNode LESS_THAN(int i) {
            return getToken(DoplerParser.LESS_THAN, i);
        }

        public List<TerminalNode> LESS_EQUALS() {
            return getTokens(DoplerParser.LESS_EQUALS);
        }

        public TerminalNode LESS_EQUALS(int i) {
            return getToken(DoplerParser.LESS_EQUALS, i);
        }

        public List<TerminalNode> GREATER_EQUALS() {
            return getTokens(DoplerParser.GREATER_EQUALS);
        }

        public TerminalNode GREATER_EQUALS(int i) {
            return getToken(DoplerParser.GREATER_EQUALS, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_subrange;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterSubrange(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitSubrange(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class ExpressionContext extends ParserRuleContext {
        public ExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public UnaryExpressionContext unaryExpression() {
            return getRuleContext(UnaryExpressionContext.class, 0);
        }

        public LiteralExpressionContext literalExpression() {
            return getRuleContext(LiteralExpressionContext.class, 0);
        }

        public IsTakenContext isTaken() {
            return getRuleContext(IsTakenContext.class, 0);
        }

        public DecisionValueCallExpressionContext decisionValueCallExpression() {
            return getRuleContext(DecisionValueCallExpressionContext.class, 0);
        }

        public BinaryExpressionContext binaryExpression() {
            return getRuleContext(BinaryExpressionContext.class, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_expression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitExpression(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class UnaryExpressionContext extends ParserRuleContext {
        public UnaryExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode NOT() {
            return getToken(DoplerParser.NOT, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public TerminalNode EnumerationLiteralExpression() {
            return getToken(DoplerParser.EnumerationLiteralExpression, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_unaryExpression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterUnaryExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitUnaryExpression(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class DecisionVisibilityCallExpressionContext extends ParserRuleContext {
        public DecisionVisibilityCallExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public List<TerminalNode> AND() {
            return getTokens(DoplerParser.AND);
        }

        public TerminalNode AND(int i) {
            return getToken(DoplerParser.AND, i);
        }

        public List<TerminalNode> OR() {
            return getTokens(DoplerParser.OR);
        }

        public TerminalNode OR(int i) {
            return getToken(DoplerParser.OR, i);
        }

        public List<TerminalNode> EQUALS() {
            return getTokens(DoplerParser.EQUALS);
        }

        public TerminalNode EQUALS(int i) {
            return getToken(DoplerParser.EQUALS, i);
        }

        public List<TerminalNode> GREATER_THAN() {
            return getTokens(DoplerParser.GREATER_THAN);
        }

        public TerminalNode GREATER_THAN(int i) {
            return getToken(DoplerParser.GREATER_THAN, i);
        }

        public List<TerminalNode> LESS_THAN() {
            return getTokens(DoplerParser.LESS_THAN);
        }

        public TerminalNode LESS_THAN(int i) {
            return getToken(DoplerParser.LESS_THAN, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_decisionVisibilityCallExpression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener)
                ((DoplerParserListener) listener).enterDecisionVisibilityCallExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener)
                ((DoplerParserListener) listener).exitDecisionVisibilityCallExpression(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class IsTakenContext extends ParserRuleContext {
        public IsTakenContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode ISTAKEN() {
            return getToken(DoplerParser.ISTAKEN, 0);
        }

        public TerminalNode LPAREN() {
            return getToken(DoplerParser.LPAREN, 0);
        }

        public TerminalNode IDENTIFIER() {
            return getToken(DoplerParser.IDENTIFIER, 0);
        }

        public TerminalNode RPAREN() {
            return getToken(DoplerParser.RPAREN, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_isTaken;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterIsTaken(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitIsTaken(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class DecisionValueCallExpressionContext extends ParserRuleContext {
        public DecisionValueCallExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode GETVALUE() {
            return getToken(DoplerParser.GETVALUE, 0);
        }

        public TerminalNode LPAREN() {
            return getToken(DoplerParser.LPAREN, 0);
        }

        public TerminalNode IDENTIFIER() {
            return getToken(DoplerParser.IDENTIFIER, 0);
        }

        public TerminalNode RPAREN() {
            return getToken(DoplerParser.RPAREN, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_decisionValueCallExpression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener)
                ((DoplerParserListener) listener).enterDecisionValueCallExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener)
                ((DoplerParserListener) listener).exitDecisionValueCallExpression(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class BinaryExpressionContext extends ParserRuleContext {
        public BinaryExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public AndExpressionContext andExpression() {
            return getRuleContext(AndExpressionContext.class, 0);
        }

        public OrExpressionContext orExpression() {
            return getRuleContext(OrExpressionContext.class, 0);
        }

        public XorExpressionContext xorExpression() {
            return getRuleContext(XorExpressionContext.class, 0);
        }

        public EqualityExpressionContext equalityExpression() {
            return getRuleContext(EqualityExpressionContext.class, 0);
        }

        public GreaterThanExpressionContext greaterThanExpression() {
            return getRuleContext(GreaterThanExpressionContext.class, 0);
        }

        public LessThanExpressionContext lessThanExpression() {
            return getRuleContext(LessThanExpressionContext.class, 0);
        }

        public GreaterEqualsExpressionContext greaterEqualsExpression() {
            return getRuleContext(GreaterEqualsExpressionContext.class, 0);
        }

        public LessEqualsExpressionContext lessEqualsExpression() {
            return getRuleContext(LessEqualsExpressionContext.class, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_binaryExpression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterBinaryExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitBinaryExpression(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class AndExpressionContext extends ParserRuleContext {
        public AndExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode LPAREN() {
            return getToken(DoplerParser.LPAREN, 0);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public TerminalNode AND() {
            return getToken(DoplerParser.AND, 0);
        }

        public TerminalNode RPAREN() {
            return getToken(DoplerParser.RPAREN, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_andExpression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterAndExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitAndExpression(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class OrExpressionContext extends ParserRuleContext {
        public OrExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode LPAREN() {
            return getToken(DoplerParser.LPAREN, 0);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public TerminalNode OR() {
            return getToken(DoplerParser.OR, 0);
        }

        public TerminalNode RPAREN() {
            return getToken(DoplerParser.RPAREN, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_orExpression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterOrExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitOrExpression(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class XorExpressionContext extends ParserRuleContext {
        public XorExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode LPAREN() {
            return getToken(DoplerParser.LPAREN, 0);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public TerminalNode XOR() {
            return getToken(DoplerParser.XOR, 0);
        }

        public TerminalNode RPAREN() {
            return getToken(DoplerParser.RPAREN, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_xorExpression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterXorExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitXorExpression(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class EqualityExpressionContext extends ParserRuleContext {
        public EqualityExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode LPAREN() {
            return getToken(DoplerParser.LPAREN, 0);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public TerminalNode RPAREN() {
            return getToken(DoplerParser.RPAREN, 0);
        }

        public TerminalNode EQUALS() {
            return getToken(DoplerParser.EQUALS, 0);
        }

        public TerminalNode SET() {
            return getToken(DoplerParser.SET, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_equalityExpression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener)
                ((DoplerParserListener) listener).enterEqualityExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener)
                ((DoplerParserListener) listener).exitEqualityExpression(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class GreaterThanExpressionContext extends ParserRuleContext {
        public GreaterThanExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode LPAREN() {
            return getToken(DoplerParser.LPAREN, 0);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public TerminalNode GREATER_THAN() {
            return getToken(DoplerParser.GREATER_THAN, 0);
        }

        public TerminalNode RPAREN() {
            return getToken(DoplerParser.RPAREN, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_greaterThanExpression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener)
                ((DoplerParserListener) listener).enterGreaterThanExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener)
                ((DoplerParserListener) listener).exitGreaterThanExpression(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class LessThanExpressionContext extends ParserRuleContext {
        public LessThanExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode LPAREN() {
            return getToken(DoplerParser.LPAREN, 0);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public TerminalNode LESS_THAN() {
            return getToken(DoplerParser.LESS_THAN, 0);
        }

        public TerminalNode RPAREN() {
            return getToken(DoplerParser.RPAREN, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_lessThanExpression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener)
                ((DoplerParserListener) listener).enterLessThanExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener)
                ((DoplerParserListener) listener).exitLessThanExpression(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class GreaterEqualsExpressionContext extends ParserRuleContext {
        public GreaterEqualsExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode LPAREN() {
            return getToken(DoplerParser.LPAREN, 0);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public TerminalNode GREATER_EQUALS() {
            return getToken(DoplerParser.GREATER_EQUALS, 0);
        }

        public TerminalNode RPAREN() {
            return getToken(DoplerParser.RPAREN, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_greaterEqualsExpression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener)
                ((DoplerParserListener) listener).enterGreaterEqualsExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener)
                ((DoplerParserListener) listener).exitGreaterEqualsExpression(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class LessEqualsExpressionContext extends ParserRuleContext {
        public LessEqualsExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode LPAREN() {
            return getToken(DoplerParser.LPAREN, 0);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public TerminalNode LESS_EQUALS() {
            return getToken(DoplerParser.LESS_EQUALS, 0);
        }

        public TerminalNode RPAREN() {
            return getToken(DoplerParser.RPAREN, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_lessEqualsExpression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener)
                ((DoplerParserListener) listener).enterLessEqualsExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener)
                ((DoplerParserListener) listener).exitLessEqualsExpression(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class LiteralExpressionContext extends ParserRuleContext {
        public LiteralExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public EnumerationLiteralExpressionContext enumerationLiteralExpression() {
            return getRuleContext(EnumerationLiteralExpressionContext.class, 0);
        }

        public BooleanLiteralExpressionContext booleanLiteralExpression() {
            return getRuleContext(BooleanLiteralExpressionContext.class, 0);
        }

        public StringLiteralExpressionContext stringLiteralExpression() {
            return getRuleContext(StringLiteralExpressionContext.class, 0);
        }

        public DoubleLiteralExpressionContext doubleLiteralExpression() {
            return getRuleContext(DoubleLiteralExpressionContext.class, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_literalExpression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener)
                ((DoplerParserListener) listener).enterLiteralExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitLiteralExpression(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class EnumerationLiteralExpressionContext extends ParserRuleContext {
        public EnumerationLiteralExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode EnumerationLiteralExpression() {
            return getToken(DoplerParser.EnumerationLiteralExpression, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_enumerationLiteralExpression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener)
                ((DoplerParserListener) listener).enterEnumerationLiteralExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener)
                ((DoplerParserListener) listener).exitEnumerationLiteralExpression(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class BooleanLiteralExpressionContext extends ParserRuleContext {
        public BooleanLiteralExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode BooleanLiteralExpression() {
            return getToken(DoplerParser.BooleanLiteralExpression, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_booleanLiteralExpression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener)
                ((DoplerParserListener) listener).enterBooleanLiteralExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener)
                ((DoplerParserListener) listener).exitBooleanLiteralExpression(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class StringLiteralExpressionContext extends ParserRuleContext {
        public StringLiteralExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode StringLiteralExpression() {
            return getToken(DoplerParser.StringLiteralExpression, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_stringLiteralExpression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener)
                ((DoplerParserListener) listener).enterStringLiteralExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener)
                ((DoplerParserListener) listener).exitStringLiteralExpression(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class DoubleLiteralExpressionContext extends ParserRuleContext {
        public DoubleLiteralExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode DoubleLiteralExpression() {
            return getToken(DoplerParser.DoubleLiteralExpression, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_doubleLiteralExpression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener)
                ((DoplerParserListener) listener).enterDoubleLiteralExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener)
                ((DoplerParserListener) listener).exitDoubleLiteralExpression(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class DecisionTypeContext extends ParserRuleContext {
        public DecisionTypeContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode NumberDecision() {
            return getToken(DoplerParser.NumberDecision, 0);
        }

        public TerminalNode EnumerationDecision() {
            return getToken(DoplerParser.EnumerationDecision, 0);
        }

        public TerminalNode BooleanDecision() {
            return getToken(DoplerParser.BooleanDecision, 0);
        }

        public TerminalNode StringDecision() {
            return getToken(DoplerParser.StringDecision, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_decisionType;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterDecisionType(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitDecisionType(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class DruleContext extends ParserRuleContext {
        public DruleContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public List<TerminalNode> DQ() {
            return getTokens(DoplerParser.DQ);
        }

        public TerminalNode DQ(int i) {
            return getToken(DoplerParser.DQ, i);
        }

        public List<DruleContext> drule() {
            return getRuleContexts(DruleContext.class);
        }

        public DruleContext drule(int i) {
            return getRuleContext(DruleContext.class, i);
        }

        public TerminalNode IF() {
            return getToken(DoplerParser.IF, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public TerminalNode LBRACE() {
            return getToken(DoplerParser.LBRACE, 0);
        }

        public TerminalNode RBRACE() {
            return getToken(DoplerParser.RBRACE, 0);
        }

        public TerminalNode LPAREN() {
            return getToken(DoplerParser.LPAREN, 0);
        }

        public TerminalNode RPAREN() {
            return getToken(DoplerParser.RPAREN, 0);
        }

        public List<ActionContext> action() {
            return getRuleContexts(ActionContext.class);
        }

        public ActionContext action(int i) {
            return getRuleContext(ActionContext.class, i);
        }

        public List<TerminalNode> SEMICOLON() {
            return getTokens(DoplerParser.SEMICOLON);
        }

        public TerminalNode SEMICOLON(int i) {
            return getToken(DoplerParser.SEMICOLON, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_drule;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterDrule(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitDrule(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class ActionContext extends ParserRuleContext {
        public ActionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public ValueRestrictionActionContext valueRestrictionAction() {
            return getRuleContext(ValueRestrictionActionContext.class, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_action;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterAction(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitAction(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class ValueRestrictionActionContext extends ParserRuleContext {
        public ValueRestrictionActionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public AllowsContext allows() {
            return getRuleContext(AllowsContext.class, 0);
        }

        public DisallowsContext disallows() {
            return getRuleContext(DisallowsContext.class, 0);
        }

        public EnForceContext enForce() {
            return getRuleContext(EnForceContext.class, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_valueRestrictionAction;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener)
                ((DoplerParserListener) listener).enterValueRestrictionAction(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener)
                ((DoplerParserListener) listener).exitValueRestrictionAction(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class AllowsContext extends ParserRuleContext {
        public AllowsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode ALLOW() {
            return getToken(DoplerParser.ALLOW, 0);
        }

        public TerminalNode LPAREN() {
            return getToken(DoplerParser.LPAREN, 0);
        }

        public TerminalNode IDENTIFIER() {
            return getToken(DoplerParser.IDENTIFIER, 0);
        }

        public TerminalNode RPAREN() {
            return getToken(DoplerParser.RPAREN, 0);
        }

        public TerminalNode EnumerationLiteralExpression() {
            return getToken(DoplerParser.EnumerationLiteralExpression, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_allows;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterAllows(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitAllows(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class DisallowsContext extends ParserRuleContext {
        public DisallowsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode DISALLOW() {
            return getToken(DoplerParser.DISALLOW, 0);
        }

        public TerminalNode LPAREN() {
            return getToken(DoplerParser.LPAREN, 0);
        }

        public TerminalNode IDENTIFIER() {
            return getToken(DoplerParser.IDENTIFIER, 0);
        }

        public TerminalNode RPAREN() {
            return getToken(DoplerParser.RPAREN, 0);
        }

        public TerminalNode EnumerationLiteralExpression() {
            return getToken(DoplerParser.EnumerationLiteralExpression, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_disallows;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterDisallows(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitDisallows(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class EnForceContext extends ParserRuleContext {
        public EnForceContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public BooleanEnForceContext booleanEnForce() {
            return getRuleContext(BooleanEnForceContext.class, 0);
        }

        public DoubleEnForceContext doubleEnForce() {
            return getRuleContext(DoubleEnForceContext.class, 0);
        }

        public StringEnForceContext stringEnForce() {
            return getRuleContext(StringEnForceContext.class, 0);
        }

        public EnumEnForceContext enumEnForce() {
            return getRuleContext(EnumEnForceContext.class, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_enForce;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterEnForce(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitEnForce(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class EnumEnForceContext extends ParserRuleContext {
        public EnumEnForceContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode IDENTIFIER() {
            return getToken(DoplerParser.IDENTIFIER, 0);
        }

        public TerminalNode SET() {
            return getToken(DoplerParser.SET, 0);
        }

        public List<RangeItemContext> rangeItem() {
            return getRuleContexts(RangeItemContext.class);
        }

        public RangeItemContext rangeItem(int i) {
            return getRuleContext(RangeItemContext.class, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_enumEnForce;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterEnumEnForce(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitEnumEnForce(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class StringEnForceContext extends ParserRuleContext {
        public StringEnForceContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode IDENTIFIER() {
            return getToken(DoplerParser.IDENTIFIER, 0);
        }

        public TerminalNode SET() {
            return getToken(DoplerParser.SET, 0);
        }

        public TerminalNode StringLiteralExpression() {
            return getToken(DoplerParser.StringLiteralExpression, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_stringEnForce;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterStringEnForce(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitStringEnForce(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class BooleanEnForceContext extends ParserRuleContext {
        public BooleanEnForceContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode IDENTIFIER() {
            return getToken(DoplerParser.IDENTIFIER, 0);
        }

        public TerminalNode SET() {
            return getToken(DoplerParser.SET, 0);
        }

        public TerminalNode BooleanLiteralExpression() {
            return getToken(DoplerParser.BooleanLiteralExpression, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_booleanEnForce;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterBooleanEnForce(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitBooleanEnForce(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class DoubleEnForceContext extends ParserRuleContext {
        public DoubleEnForceContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode IDENTIFIER() {
            return getToken(DoplerParser.IDENTIFIER, 0);
        }

        public TerminalNode SET() {
            return getToken(DoplerParser.SET, 0);
        }

        public TerminalNode DoubleLiteralExpression() {
            return getToken(DoplerParser.DoubleLiteralExpression, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_doubleEnForce;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).enterDoubleEnForce(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DoplerParserListener) ((DoplerParserListener) listener).exitDoubleEnForce(this);
        }
    }
}
