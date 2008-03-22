package org.intellij.lang.jflex.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.intellij.lang.jflex.JFlexElementTypes;

%%

%class _JFlexLexer
%implements FlexLexer
%final
%unicode
%function advance
%type IElementType
%eof{ return;
%eof}

%{
    private int braceCounter = 0;
%}

SectionSeparator = "%%"

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace = [ \t]
AnySpace = {LineTerminator} | {WhiteSpace} | [\f]

EscapedCharacter = \\{InputCharacter}

Comment = {TraditionalComment} | {DocumentationComment} | {LineComment}

TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
DocumentationComment = "/**" {CommentContent} "*"+ "/"
CommentContent = ( [^*] | \*+ [^/*] )*
LineComment = {WhiteSpace}* "//" .*

JavaCodeEndOption = "%}" | "%init}" | "%eof}" | "%eofval}"

Identifier = [:jletter:] [:jletterdigit:]*
IdentifierReference = \{ {Identifier} \}

StringLiteral = \" ( \\\" | [^\"\n\r] )* \"

RegExpPrefix = [\!\~]
RegExpPostfix = [\*\+\?]

%state OPTIONS_AND_DECLARATIONS, LEXICAL_RULES

%state IDENTIFIER, IDENTIFIER_REGEXP_LITERAL, IDENTIFIER_REGEXP, IDENTIFIER_REGEXP_IDENTIFIER, IDENTIFIER_REGEXP_CLASS
%state OPTION, OPT_ERROR, OPT_PARAM, OPT_PARAMS, OPT_CODE, OPT_JAVA_CODE, OPT_EXCEPTION, OPT_BOOLEAN
%state STATE, RULE, ACTION_REGEXP, ACTION_REGEXP_IDENTIFIER, ACTION_REGEXP_CLASS, ACTION, ACTION_JAVA_CODE

%%

<YYINITIAL> {
    {SectionSeparator}        { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.SECTION_SIGN; }
    {AnySpace}+               { yybegin(YYINITIAL); return JFlexElementTypes.JAVA_CODE; }
    .                         { yybegin(YYINITIAL); return JFlexElementTypes.JAVA_CODE; }
}

<OPTIONS_AND_DECLARATIONS> {
    {AnySpace}+               { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.WHITE_SPACE; }
    {Comment}                 { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.COMMENT; }

    {SectionSeparator}        { yybegin(LEXICAL_RULES); return JFlexElementTypes.SECTION_SIGN; }
    "%"                       { yybegin(OPTION); return JFlexElementTypes.OPTION_SIGN;}
    {Identifier}              { yybegin(IDENTIFIER); return JFlexElementTypes.MACROS; }
    .                         { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.BAD_CHARACTER; }
}

<LEXICAL_RULES> {
    {AnySpace}+               { yybegin(LEXICAL_RULES); return JFlexElementTypes.WHITE_SPACE; }
    {Comment}                 { yybegin(LEXICAL_RULES); return JFlexElementTypes.COMMENT; }
    "<"                       { yybegin(STATE); return JFlexElementTypes.STATE_LEFT_ANGLE_BRACKET; }
    .                         { yybegin(ACTION_REGEXP); yypushback(yylength()); }
}

<IDENTIFIER> {
    {WhiteSpace}+              { yybegin(IDENTIFIER); return JFlexElementTypes.WHITE_SPACE; }
    "="                        { yybegin(IDENTIFIER_REGEXP_LITERAL); return JFlexElementTypes.EQ; }
    .                          { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.BAD_CHARACTER; }
}
<IDENTIFIER_REGEXP_LITERAL> {
    {WhiteSpace}+              { yybegin(IDENTIFIER_REGEXP_LITERAL); return JFlexElementTypes.WHITE_SPACE; }
    .                          { yybegin(IDENTIFIER_REGEXP); yypushback(yylength()); }
}
<IDENTIFIER_REGEXP> {
    {LineTerminator}+          { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.WHITE_SPACE; }
    {WhiteSpace}+              { yybegin(IDENTIFIER_REGEXP); return JFlexElementTypes.REGEXP_WHITE_SPACE; }
    {EscapedCharacter}         { yybegin(IDENTIFIER_REGEXP); return JFlexElementTypes.REGEXP_SYMBOL; }
    "["                        { yybegin(IDENTIFIER_REGEXP_CLASS); return JFlexElementTypes.REGEXP_LEFT_BRACKET;}
    "|"                        { yybegin(IDENTIFIER_REGEXP); return JFlexElementTypes.REGEXP_OR; }
    "("                        { yybegin(IDENTIFIER_REGEXP); return JFlexElementTypes.REGEXP_LEFT_PARENTHESIS; }
    ")"                        { yybegin(IDENTIFIER_REGEXP); return JFlexElementTypes.REGEXP_RIGHT_PARENTHESIS; }
    "{"                        { yybegin(IDENTIFIER_REGEXP_IDENTIFIER); return JFlexElementTypes.REGEXP_LEFT_BRACE; }
    {RegExpPrefix}             { yybegin(IDENTIFIER_REGEXP); return JFlexElementTypes.REGEXP_PREFIX; }
    {RegExpPostfix}            { yybegin(IDENTIFIER_REGEXP); return JFlexElementTypes.REGEXP_POSTFIX; }
    {StringLiteral}            { yybegin(IDENTIFIER_REGEXP); return JFlexElementTypes.REGEXP_STRING_LITERAL; }
    .                          { yybegin(IDENTIFIER_REGEXP); return JFlexElementTypes.REGEXP_SYMBOL; }
}
<IDENTIFIER_REGEXP_IDENTIFIER> {
    {LineTerminator}+          { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.WHITE_SPACE; }
    {Identifier}               { yybegin(IDENTIFIER_REGEXP_IDENTIFIER); return JFlexElementTypes.REGEXP_MACROS_REF; }
    "}"                        { yybegin(IDENTIFIER_REGEXP); return JFlexElementTypes.REGEXP_RIGHT_BRACE; }
    .                          { yybegin(IDENTIFIER_REGEXP); return JFlexElementTypes.BAD_CHARACTER; }
}
<IDENTIFIER_REGEXP_CLASS> {
    {LineTerminator}+          { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.WHITE_SPACE; }
    {EscapedCharacter}         { yybegin(IDENTIFIER_REGEXP_CLASS); return JFlexElementTypes.REGEXP_CLASS_SYMBOL; }
    "]"                        { yybegin(IDENTIFIER_REGEXP); return JFlexElementTypes.REGEXP_RIGHT_BRACKET;}
    {StringLiteral}            { yybegin(IDENTIFIER_REGEXP_CLASS); return JFlexElementTypes.REGEXP_STRING_LITERAL; }
    .                          { yybegin(IDENTIFIER_REGEXP_CLASS); return JFlexElementTypes.REGEXP_CLASS_SYMBOL; }
}

<OPTION> {
    "class"                    { yybegin(OPT_PARAM); return JFlexElementTypes.CLASS_KEYWORD; }
    "implements"               { yybegin(OPT_PARAMS); return JFlexElementTypes.IMPLEMENTS_KEYWORD; }
    "extends"                  { yybegin(OPT_PARAM); return JFlexElementTypes.EXTENDS_KEYWORD; }
    "public"                   { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.PUBLIC_KEYWORD; }
    "final"                    { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.FINAL_KEYWORD; }
    "abstract"                 { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.ABSTRACT_KEYWORD; }
    "apiprivate"               { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.APIPRIVATE_KEYWORD; }
    "init"                     { yybegin(OPT_CODE); return JFlexElementTypes.INIT_KEYWORD; }
    "initthrow"                { yybegin(OPT_EXCEPTION); return JFlexElementTypes.INITTHROW_KEYWORD; }
    "scanerror"                { yybegin(OPT_PARAM); return JFlexElementTypes.SCANERROR_KEYWORD; }
    "buffersize"               { yybegin(OPT_PARAM); return JFlexElementTypes.BUFFERSIZE_KEYWORD; }
    "include"                  { yybegin(OPT_PARAM); return JFlexElementTypes.INCLUDE_KEYWORD; }
    "function"                 { yybegin(OPT_PARAM); return JFlexElementTypes.FUNCTION_KEYWORD; }
    "int"                      { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.INT_KEYWORD; }
    "integer"                  { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.INTEGER_KEYWORD; }
    "intwrap"                  { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.INTWRAP_KEYWORD; }
    "type"                     { yybegin(OPT_PARAM); return JFlexElementTypes.TYPE_KEYWORD; }
    "yylexthrow"               { yybegin(OPT_EXCEPTION); return JFlexElementTypes.YYLEXTHROW_KEYWORD; }
    "eof"                      { yybegin(OPT_CODE); return JFlexElementTypes.EOF_KEYWORD; }
    "eofval"                   { yybegin(OPT_CODE); return JFlexElementTypes.EOFVAL_KEYWORD; }
    "eofthrow"                 { yybegin(OPT_EXCEPTION); return JFlexElementTypes.EOFTHROW_KEYWORD; }
    "eofclose"                 { yybegin(OPT_BOOLEAN); return JFlexElementTypes.EOFCLOSE_KEYWORD; }
    "debug"                    { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.DEBUG_KEYWORD; }
    "standalone"               { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.STANDALONE_KEYWORD; }
    "cup"                      { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.CUP_KEYWORD; }
    "cupsym"                   { yybegin(OPT_PARAM); return JFlexElementTypes.CUPSYM_KEYWORD; }
    "cupdebug"                 { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.CUPDEBUG_KEYWORD; }
    "byacc"                    { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.BYACC_KEYWORD; }
    "switch"                   { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.SWITCH_KEYWORD; }
    "table"                    { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.TABLE_KEYWORD; }
    "pack"                     { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.PACK_KEYWORD; }
    "7bit"                     { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes._7BIT_KEYWORD; }
    "full"                     { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.FULL_KEYWORD; }
    "8bit"                     { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes._8BIT_KEYWORD; }
    "unicode"                  { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.UNICODE_KEYWORD; }
    "16bit"                    { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes._16BIT_KEYWORD; }
    "caseless"                 { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.CASELESS_KEYWORD; }
    "ignorecase"               { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.IGNORECASE_KEYWORD; }
    "char"                     { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.CHAR_KEYWORD; }
    "line"                     { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.LINE_KEYWORD; }
    "column"                   { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.COLUMN_KEYWORD; }
    "notunix"                  { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.NOTUNIX_KEYWORD; }
    "yyeof"                    { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.YYEOF_KEYWORD; }
    "state"                    { yybegin(OPT_PARAMS); return JFlexElementTypes.STATE_KEYWORD; }
    "s"                        { yybegin(OPT_PARAMS); return JFlexElementTypes.S_KEYWORD; }
    "xstate"                   { yybegin(OPT_PARAMS); return JFlexElementTypes.XSTATE_KEYWORD; }
    "x"                        { yybegin(OPT_PARAMS); return JFlexElementTypes.X_KEYWORD; }
    "{"                        { yybegin(OPT_JAVA_CODE); return JFlexElementTypes.OPTION_LEFT_BRACE; }
    "}"                        { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.OPTION_RIGHT_BRACE; }
    {LineTerminator}+          { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.WHITE_SPACE; }
    {WhiteSpace}+              { yybegin(OPT_ERROR); return JFlexElementTypes.OPTION_WHITE_SPACE; }
    .                          { yybegin(OPT_ERROR); return JFlexElementTypes.BAD_CHARACTER; }
}
<OPT_ERROR> {
    {LineTerminator}+          { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.WHITE_SPACE; }
    {WhiteSpace}+              { yybegin(OPT_ERROR); return JFlexElementTypes.OPTION_WHITE_SPACE; }
    .                          { yybegin(OPT_ERROR); return JFlexElementTypes.BAD_CHARACTER; }
}
<OPT_PARAM> {
    {LineTerminator}+          { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.WHITE_SPACE; }
    {WhiteSpace}+              { yybegin(OPT_PARAM); return JFlexElementTypes.OPTION_WHITE_SPACE; }
    {Identifier}               { yybegin(OPT_PARAM); return JFlexElementTypes.OPTION_PARAMETER; }
    .                          { yybegin(OPT_PARAM); return JFlexElementTypes.BAD_CHARACTER; }
}
<OPT_PARAMS> {
    {LineTerminator}+          { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.WHITE_SPACE; }
    {WhiteSpace}+              { yybegin(OPT_PARAMS); return JFlexElementTypes.OPTION_WHITE_SPACE; }
    {Identifier}               { yybegin(OPT_PARAMS); return JFlexElementTypes.OPTION_PARAMETER; }
    ","                        { yybegin(OPT_PARAMS); return JFlexElementTypes.OPTION_COMMA; }
    .                          { yybegin(OPT_PARAMS); return JFlexElementTypes.BAD_CHARACTER; }
}
<OPT_CODE> {
    {LineTerminator}+          { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.WHITE_SPACE; }
    "{"                        { yybegin(OPT_JAVA_CODE); return JFlexElementTypes.OPTION_LEFT_BRACE; }
    "}"                        { yybegin(OPT_CODE); return JFlexElementTypes.OPTION_RIGHT_BRACE; }
    .                          { yybegin(OPT_CODE); return JFlexElementTypes.BAD_CHARACTER; }
}
<OPT_JAVA_CODE> {
    {JavaCodeEndOption}        { yybegin(OPTIONS_AND_DECLARATIONS); yypushback(yylength()); }
    {AnySpace}+                { yybegin(OPT_JAVA_CODE); return JFlexElementTypes.JAVA_CODE; }
    .                          { yybegin(OPT_JAVA_CODE); return JFlexElementTypes.JAVA_CODE; }
}
<OPT_EXCEPTION> {
    {LineTerminator}+          { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.WHITE_SPACE; }
    "{"                        { yybegin(OPT_EXCEPTION); return JFlexElementTypes.OPTION_LEFT_BRACE; }
    "}"                        { yybegin(OPT_EXCEPTION); return JFlexElementTypes.OPTION_RIGHT_BRACE; }
    {WhiteSpace}+              { yybegin(OPT_EXCEPTION); return JFlexElementTypes.OPTION_WHITE_SPACE; }
    {Identifier}               { yybegin(OPT_EXCEPTION); return JFlexElementTypes.OPTION_PARAMETER; }
    ","                        { yybegin(OPT_EXCEPTION); return JFlexElementTypes.OPTION_COMMA; }
    .                          { yybegin(OPT_EXCEPTION); return JFlexElementTypes.BAD_CHARACTER; }
}
<OPT_BOOLEAN> {
    {LineTerminator}+          { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexElementTypes.WHITE_SPACE; }
    {WhiteSpace}+              { yybegin(OPT_BOOLEAN); return JFlexElementTypes.OPTION_WHITE_SPACE; }
    "false"                    { yybegin(OPT_BOOLEAN); return JFlexElementTypes.FALSE_KEYWORD; }
    "true"                     { yybegin(OPT_BOOLEAN); return JFlexElementTypes.TRUE_KEYWORD; }
    .                          { yybegin(OPT_BOOLEAN); return JFlexElementTypes.BAD_CHARACTER; }
}

<STATE> {
    {LineTerminator}+          { yybegin(LEXICAL_RULES); return JFlexElementTypes.WHITE_SPACE; }
    {WhiteSpace}+              { yybegin(STATE); return JFlexElementTypes.WHITE_SPACE; }
    {Identifier}               { yybegin(STATE); return JFlexElementTypes.STATE_REF; }
    ","                        { yybegin(STATE); return JFlexElementTypes.STATE_COMMA; }
    ">"                        { yybegin(RULE); return JFlexElementTypes.STATE_LEFT_ANGLE_BRACKET; }
    .                          { yybegin(STATE); return JFlexElementTypes.BAD_CHARACTER; }
}

<RULE> {
    {AnySpace}+                { yybegin(RULE); return JFlexElementTypes.WHITE_SPACE; }
    {Comment}                  { yybegin(RULE); return JFlexElementTypes.COMMENT; }
    {IdentifierReference}      { yybegin(ACTION_REGEXP); yypushback(yylength()); }
    "{"                        { yybegin(RULE); return JFlexElementTypes.LEFT_BRACE; }
    "<"                        { yybegin(STATE); return JFlexElementTypes.STATE_RIGHT_ANGLE_BRACKET; }
    "}"                        { yybegin(LEXICAL_RULES); return JFlexElementTypes.RIGHT_BRACE; }
    .                          { yybegin(ACTION_REGEXP); yypushback(yylength()); }
}
<ACTION_REGEXP> {
    {LineTerminator}+          { yybegin(LEXICAL_RULES); return JFlexElementTypes.WHITE_SPACE; }
    {WhiteSpace}+              { yybegin(ACTION_REGEXP); return JFlexElementTypes.WHITE_SPACE; }
    {EscapedCharacter}         { yybegin(ACTION_REGEXP); return JFlexElementTypes.REGEXP_SYMBOL; }
    {IdentifierReference}      { yybegin(ACTION_REGEXP_IDENTIFIER); yypushback(yylength());}
    "{"                        { braceCounter = 0; yybegin(ACTION_JAVA_CODE); return JFlexElementTypes.LEFT_BRACE; }
    "["                        { yybegin(ACTION_REGEXP_CLASS); return JFlexElementTypes.REGEXP_LEFT_BRACKET; }
    "|"                        { yybegin(ACTION_REGEXP); return JFlexElementTypes.REGEXP_OR; }
    "("                        { yybegin(ACTION_REGEXP); return JFlexElementTypes.REGEXP_LEFT_PARENTHESIS; }
    ")"                        { yybegin(ACTION_REGEXP); return JFlexElementTypes.REGEXP_RIGHT_PARENTHESIS; }
    {RegExpPrefix}             { yybegin(ACTION_REGEXP); return JFlexElementTypes.REGEXP_PREFIX; }
    {RegExpPostfix}            { yybegin(ACTION_REGEXP); return JFlexElementTypes.REGEXP_POSTFIX; }
    {StringLiteral}            { yybegin(ACTION_REGEXP); return JFlexElementTypes.REGEXP_STRING_LITERAL; }
    .                          { yybegin(ACTION_REGEXP); return JFlexElementTypes.REGEXP_SYMBOL; }
}
<ACTION_REGEXP_IDENTIFIER> {
    {Identifier}               { yybegin(ACTION_REGEXP_IDENTIFIER); return JFlexElementTypes.REGEXP_MACROS_REF; }
    "{"                        { yybegin(ACTION_REGEXP_IDENTIFIER); return JFlexElementTypes.REGEXP_LEFT_BRACE; }
    "}"                        { yybegin(ACTION_REGEXP); return JFlexElementTypes.REGEXP_RIGHT_BRACE; }
    .                          { yybegin(ACTION_REGEXP); return JFlexElementTypes.BAD_CHARACTER; }
}
<ACTION_REGEXP_CLASS> {
    {LineTerminator}+          { yybegin(LEXICAL_RULES); return JFlexElementTypes.WHITE_SPACE; }
    {EscapedCharacter}         { yybegin(ACTION_REGEXP_CLASS); return JFlexElementTypes.REGEXP_CLASS_SYMBOL; }
    "]"                        { yybegin(ACTION_REGEXP); return JFlexElementTypes.REGEXP_RIGHT_BRACKET;}
    {StringLiteral}            { yybegin(ACTION_REGEXP_CLASS); return JFlexElementTypes.REGEXP_STRING_LITERAL; }
    .                          { yybegin(ACTION_REGEXP_CLASS); return JFlexElementTypes.REGEXP_CLASS_SYMBOL; }
}
<ACTION> {
    {AnySpace}+                { yybegin(ACTION); return JFlexElementTypes.WHITE_SPACE; }
    "{"                        { braceCounter = 0; yybegin(ACTION_JAVA_CODE); return JFlexElementTypes.LEFT_BRACE; }
    .                          { yybegin(RULE); return JFlexElementTypes.BAD_CHARACTER; }
}
<ACTION_JAVA_CODE> {
    {AnySpace}+                { yybegin(ACTION_JAVA_CODE); return JFlexElementTypes.JAVA_CODE; }
    "{"                        { braceCounter++; yybegin(ACTION_JAVA_CODE); return JFlexElementTypes.JAVA_CODE; }
    "}"                        { braceCounter--; if (braceCounter < 0) {yybegin(RULE); return JFlexElementTypes.RIGHT_BRACE; } else { yybegin(ACTION_JAVA_CODE); return JFlexElementTypes.JAVA_CODE; }}
    .                          { yybegin(ACTION_JAVA_CODE); return JFlexElementTypes.JAVA_CODE; }
}

//todo: remove from here
{LineTerminator}+ { return JFlexElementTypes.WHITE_SPACE; }