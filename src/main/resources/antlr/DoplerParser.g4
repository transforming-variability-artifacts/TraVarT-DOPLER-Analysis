/*******************************************************************************
 * SPDX-License-Identifier: MPL-2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/.
 *
 * Contributors:
 * 	@author David Kowal
 * 	@author Kevin Feichtinger
 *
 * Copyright 2024 Karlsruhe Institute of Technology (KIT)
 * KASTEL - Dependability of Software-intensive Systems
 *******************************************************************************/

parser grammar DoplerParser;

options {tokenVocab = DoplerLexer;}

document
    : jsonDocument
    | csvFile
    ;


// JSON File Structure
jsonDocument
    : LBRACE jsonValue? RBRACE EOF
    ;

jsonValue
    : DQ DOPLER_KEY DQ COLON LBRACE (jsonObject COMMA?)* RBRACE
    | LBRACE RBRACE
    ;

jsonObject
    : DQ id DQ COLON LBRACE (jsonPair COMMA?)* RBRACE
    | LBRACE RBRACE
    ;

jsonPair
    : QUESTION_KEY DQ (question | DQ DQ | ) DQ
    | DQ TYPE_KEY DQ COLON DQ (decisionType | DQ DQ | ) DQ
    | DQ RANGE_KEY DQ COLON DQ (range | DQ DQ | ) DQ
    | DQ CARDINALITY_KEY DQ COLON DQ (cardinality | DQ DQ | ) DQ
    | DQ CONSTRAINT_RULE_KEY DQ COLON (drule | DQ DQ |  )
    | DQ VISIBLE_RELEVANT_KEY DQ COLON DQ (decisionVisibilityCallExpression | DQ DQ | ) DQ
    ;


// Parsing Rules

// CSV file structure
csvFile
    : hdr row+ EOF
    ;

hdr
    : row
    ;

row
    : field (SEMICOLON field)* CR? LF
    ;

field
    : drule
    | id
    | question
    | decisionType
    | cardinality
    | range
    | decisionVisibilityCallExpression
    | header
    | IDENTIFIER
    |
    ;

header
    : QUESTION_KEY
    | TYPE_KEY
    | RANGE_KEY
    | CARDINALITY_KEY
    | CONSTRAINT_RULE_KEY
    | VISIBLE_RELEVANT_KEY
    ;

// Parsing Rules for specific elements
id
    : IDENTIFIER
    ;

cardinality
    : DoubleLiteralExpression COLON DoubleLiteralExpression
    ;

// Question
question
    : QUESTION
    ;

// Range
range
    : rangeItem (rangeItem)*(PIPE rangeItem (rangeItem)*)+
    | DoubleLiteralExpression MINUS DoubleLiteralExpression
    ;

rangeItem
    : IDENTIFIER
    | specialCharacter
    | expression
    | subrange
    | cardinality
    | QUESTION
    | header
    ;

specialCharacter
    : RPAREN | LPAREN | LBRACE | RBRACE | COLON | ANPERSAND | PERCENT | COMMA | SPECIAL_CHAR
    ;

subrange
    : expression (( AND | OR | EQUALS | GREATER_THAN | LESS_THAN | LESS_EQUALS | GREATER_EQUALS) expression)*
    ;

// Expressions
expression
    : LPAREN expression RPAREN              # parenthesizedExpression
    | NOT expression                        # unaryExpression
    | ISTAKEN LPAREN IDENTIFIER RPAREN      # isTaken
    | GETVALUE LPAREN IDENTIFIER RPAREN     # decisionValueCallExpression
    | expression (EQUALS | SET) expression  # equalityExpression
    | expression GREATER_THAN expression    # greaterThanExpression
    | expression LESS_THAN expression       # lessThanExpression
    | expression GREATER_EQUALS expression  # greaterEqualsExpression
    | expression LESS_EQUALS expression     # lessEqualsExpression
    | expression AND expression             # andExpression
    | expression OR expression              # orExpression
    | expression XOR expression             # xorExpression
    | literalExpression                     # primaryLiteral
    | IDENTIFIER                            # decisionValueCallExpression
    ;

// Re-add this rule so DoplerExpressionParser.java still finds the class
decisionVisibilityCallExpression
    : expression
    ;

// Literal Expressions
literalExpression
    : enumerationLiteralExpression
    | booleanLiteralExpression
    | stringLiteralExpression
    | doubleLiteralExpression
    ;

enumerationLiteralExpression
    : EnumerationLiteralExpression
    ;

booleanLiteralExpression
    : BooleanLiteralExpression
    ;

stringLiteralExpression
    : StringLiteralExpression
    ;

doubleLiteralExpression
    : DoubleLiteralExpression
    ;


// Decision Types
decisionType
    : NumberDecision
    | EnumerationDecision
    | BooleanDecision
    | StringDecision
    ;

// Action and Rule Definitions
drule
    : DQ drule DQ
    | drule drule
    | IF LPAREN? expression RPAREN? LBRACE (action SEMICOLON?)+ RBRACE
    ;

action
    : valueRestrictionAction
    ;

valueRestrictionAction
    : allows
    | disallows
    | enForce
    ;

allows
    : ALLOW LPAREN IDENTIFIER RPAREN
    | ALLOW LPAREN EnumerationLiteralExpression RPAREN
    ;

disallows
    : DISALLOW LPAREN IDENTIFIER RPAREN
    | DISALLOW LPAREN EnumerationLiteralExpression RPAREN
    ;

enForce
    : booleanEnForce
    | doubleEnForce
    | stringEnForce
    | enumEnForce
    ;

enumEnForce
    : IDENTIFIER SET (rangeItem)*
    ;

stringEnForce
    : IDENTIFIER SET StringLiteralExpression
    ;

booleanEnForce
    : IDENTIFIER SET BooleanLiteralExpression
    ;

doubleEnForce
    : IDENTIFIER SET DoubleLiteralExpression
    ;
