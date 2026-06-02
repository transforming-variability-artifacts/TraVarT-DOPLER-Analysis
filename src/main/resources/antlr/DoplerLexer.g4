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

// Schaltet JSON-Mode zusätzlich ein
lexer grammar DoplerLexer;

@members {
    // Decide once at construction: if the first non-whitespace char is '{', treat input as JSON
    public boolean jsonFile = false;
    {
        int i = 1;
        int la = _input.LA(1);
        while (la != org.antlr.v4.runtime.IntStream.EOF &&
               (la == ' ' || la == '\t' || la == '\r' || la == '\n')) {
            la = _input.LA(++i);
        }
        jsonFile = (la == '{');
    }
}

CR        : { !jsonFile }? '\r';
LF        : { !jsonFile }? '\n';

QUESTION_KEY : '"Question":';

QUESTION
    : (~[?\r\n;"])+ '?'   // everything except ? or \r or \n or ; followed by a ?
    ;

TYPE_KEY : 'Type';
RANGE_KEY : 'Range';
CARDINALITY_KEY : 'Cardinality';
CONSTRAINT_RULE_KEY : 'Constraint/Rule';
VISIBLE_RELEVANT_KEY : 'Visible/relevant if';
DOPLER_KEY : 'DOPLER';


WS_DEFAULT
    : { !jsonFile }? [ ]+ -> skip
    ;

// In JSON files, skip all whitespace including newlines
WS_JSON
    : { jsonFile }? [ \t\r\n]+ -> skip
    ;

// Keywords
ALLOW        : 'allow' | 'Allow';
DISALLOW     : 'disAllow' | 'DisAllow';
ISTAKEN      : 'isTaken' | 'IsTaken';
GETVALUE     : 'getValue' | 'GetValue';

// Operators
EQUALS       : '==' ;
GREATER_THAN : '>'  ;
LESS_THAN    : '<'  ;
LESS_EQUALS  : '<=' ;
GREATER_EQUALS : '>=' ;
OR           : '||' ;
AND          : '&&' ;
XOR          : '^'  ;
PIPE         : '|'  ;
SET          : '='  ;
NOT          : '!'  ;
MINUS        : '-'  ;
// Delimiters
LPAREN       : '(' ;
RPAREN       : ')' ;
LBRACE       : '{' ;
RBRACE       : '}' ;
COLON        : ':' ;
IF           : 'if' ;
ANPERSAND   : '&' ;
PERCENT      : '%' ;
COMMA        : ',' ;
SEMICOLON   : ';' ;
DQ  : '"';


//Literal Expressions
BooleanLiteralExpression
    : TRUE
    | FALSE
    ;
TRUE
    : 'TRUE'
    | 'true'
    ;
FALSE
    : 'FALSE'
    | 'false'
    ;

DoubleLiteralExpression
    : [0-9]+
    | [0-9]+ '.' [0-9]+
    ;

EnumerationLiteralExpression
    : IDENTIFIER '.' IDENTIFIER
    ;

StringLiteralExpression
    : '\'' IDENTIFIER '\''
    ;

// Decision Types
StringDecision
    : 'String'
    ;

NumberDecision
    : 'Double'
    ;

EnumerationDecision
    : ENUMERATION
    ;
ENUMERATION
    : 'Enumeration'
    ;

BooleanDecision
    : 'Boolean'
    ;

SPECIAL_CHAR
    : ~[a-zA-Z0-9\r\n\t ;|]  // Everything except, Whitespace, Semikolon, Pipe
    ;

IDENTIFIER
    : [a-zA-Z0-9_*][a-zA-Z0-9_*]*
    | [a-zA-Z_*][a-zA-Z-*]*
    ;
