package org.intellij.lang.jflex.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.intellij.lang.jflex.JFlexTokenTypes;

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
    {SectionSeparator}        { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.SECTION_SIGN; }
    {AnySpace}+               { yybegin(YYINITIAL); return JFlexTokenTypes.JAVA_CODE; }
    .                         { yybegin(YYINITIAL); return JFlexTokenTypes.JAVA_CODE; }
}

<OPTIONS_AND_DECLARATIONS> {
    {AnySpace}+               { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.WHITE_SPACE; }
    {Comment}                 { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.COMMENT; }

    {SectionSeparator}        { yybegin(LEXICAL_RULES); return JFlexTokenTypes.SECTION_SIGN; }
    "%"                       { yybegin(OPTION); return JFlexTokenTypes.OPTION_SIGN;}
    {Identifier}              { yybegin(IDENTIFIER); return JFlexTokenTypes.MACROS; }
    .                         { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.BAD_CHARACTER; }
}

<LEXICAL_RULES> {
    {AnySpace}+               { yybegin(LEXICAL_RULES); return JFlexTokenTypes.WHITE_SPACE; }
    {Comment}                 { yybegin(LEXICAL_RULES); return JFlexTokenTypes.COMMENT; }
    "<"                       { yybegin(STATE); return JFlexTokenTypes.STATE_LEFT_ANGLE_BRACKET; }
    .                         { yybegin(ACTION_REGEXP); yypushback(yylength()); }
}

<IDENTIFIER> {
    {WhiteSpace}+              { yybegin(IDENTIFIER); return JFlexTokenTypes.WHITE_SPACE; }
    "="                        { yybegin(IDENTIFIER_REGEXP_LITERAL); return JFlexTokenTypes.EQ; }
    .                          { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.BAD_CHARACTER; }
}
<IDENTIFIER_REGEXP_LITERAL> {
    {WhiteSpace}+              { yybegin(IDENTIFIER_REGEXP_LITERAL); return JFlexTokenTypes.WHITE_SPACE; }
    .                          { yybegin(IDENTIFIER_REGEXP); yypushback(yylength()); }
}
<IDENTIFIER_REGEXP> {
    {LineTerminator}+          { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.WHITE_SPACE; }
    {WhiteSpace}+              { yybegin(IDENTIFIER_REGEXP); return JFlexTokenTypes.REGEXP_WHITE_SPACE; }
    {EscapedCharacter}         { yybegin(IDENTIFIER_REGEXP); return JFlexTokenTypes.REGEXP_SYMBOL; }
    "["                        { yybegin(IDENTIFIER_REGEXP_CLASS); return JFlexTokenTypes.REGEXP_LEFT_BRACKET;}
    "|"                        { yybegin(IDENTIFIER_REGEXP); return JFlexTokenTypes.REGEXP_OR; }
    "("                        { yybegin(IDENTIFIER_REGEXP); return JFlexTokenTypes.REGEXP_LEFT_PARENTHESIS; }
    ")"                        { yybegin(IDENTIFIER_REGEXP); return JFlexTokenTypes.REGEXP_RIGHT_PARENTHESIS; }
    "{"                        { yybegin(IDENTIFIER_REGEXP_IDENTIFIER); return JFlexTokenTypes.REGEXP_LEFT_BRACE; }
    {RegExpPrefix}             { yybegin(IDENTIFIER_REGEXP); return JFlexTokenTypes.REGEXP_PREFIX; }
    {RegExpPostfix}            { yybegin(IDENTIFIER_REGEXP); return JFlexTokenTypes.REGEXP_POSTFIX; }
    {StringLiteral}            { yybegin(IDENTIFIER_REGEXP); return JFlexTokenTypes.REGEXP_STRING_LITERAL; }
    .                          { yybegin(IDENTIFIER_REGEXP); return JFlexTokenTypes.REGEXP_SYMBOL; }
}
<IDENTIFIER_REGEXP_IDENTIFIER> {
    {LineTerminator}+          { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.WHITE_SPACE; }
    {Identifier}               { yybegin(IDENTIFIER_REGEXP_IDENTIFIER); return JFlexTokenTypes.REGEXP_MACROS_REF; }
    "}"                        { yybegin(IDENTIFIER_REGEXP); return JFlexTokenTypes.REGEXP_RIGHT_BRACE; }
    .                          { yybegin(IDENTIFIER_REGEXP); return JFlexTokenTypes.BAD_CHARACTER; }
}
<IDENTIFIER_REGEXP_CLASS> {
    {LineTerminator}+          { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.WHITE_SPACE; }
    {EscapedCharacter}         { yybegin(IDENTIFIER_REGEXP_CLASS); return JFlexTokenTypes.REGEXP_CLASS_SYMBOL; }
    "]"                        { yybegin(IDENTIFIER_REGEXP); return JFlexTokenTypes.REGEXP_RIGHT_BRACKET;}
    {StringLiteral}            { yybegin(IDENTIFIER_REGEXP_CLASS); return JFlexTokenTypes.REGEXP_STRING_LITERAL; }
    .                          { yybegin(IDENTIFIER_REGEXP_CLASS); return JFlexTokenTypes.REGEXP_CLASS_SYMBOL; }
}

<OPTION> {
    "class"                    { yybegin(OPT_PARAM); return JFlexTokenTypes.CLASS_KEYWORD; }
    "implements"               { yybegin(OPT_PARAMS); return JFlexTokenTypes.IMPLEMENTS_KEYWORD; }
    "extends"                  { yybegin(OPT_PARAM); return JFlexTokenTypes.EXTENDS_KEYWORD; }
    "public"                   { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.PUBLIC_KEYWORD; }
    "final"                    { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.FINAL_KEYWORD; }
    "abstract"                 { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.ABSTRACT_KEYWORD; }
    "apiprivate"               { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.APIPRIVATE_KEYWORD; }
    "init"                     { yybegin(OPT_CODE); return JFlexTokenTypes.INIT_KEYWORD; }
    "initthrow"                { yybegin(OPT_EXCEPTION); return JFlexTokenTypes.INITTHROW_KEYWORD; }
    "scanerror"                { yybegin(OPT_PARAM); return JFlexTokenTypes.SCANERROR_KEYWORD; }
    "buffersize"               { yybegin(OPT_PARAM); return JFlexTokenTypes.BUFFERSIZE_KEYWORD; }
    "include"                  { yybegin(OPT_PARAM); return JFlexTokenTypes.INCLUDE_KEYWORD; }
    "function"                 { yybegin(OPT_PARAM); return JFlexTokenTypes.FUNCTION_KEYWORD; }
    "int"                      { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.INT_KEYWORD; }
    "integer"                  { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.INTEGER_KEYWORD; }
    "intwrap"                  { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.INTWRAP_KEYWORD; }
    "type"                     { yybegin(OPT_PARAM); return JFlexTokenTypes.TYPE_KEYWORD; }
    "yylexthrow"               { yybegin(OPT_EXCEPTION); return JFlexTokenTypes.YYLEXTHROW_KEYWORD; }
    "eof"                      { yybegin(OPT_CODE); return JFlexTokenTypes.EOF_KEYWORD; }
    "eofval"                   { yybegin(OPT_CODE); return JFlexTokenTypes.EOFVAL_KEYWORD; }
    "eofthrow"                 { yybegin(OPT_EXCEPTION); return JFlexTokenTypes.EOFTHROW_KEYWORD; }
    "eofclose"                 { yybegin(OPT_BOOLEAN); return JFlexTokenTypes.EOFCLOSE_KEYWORD; }
    "debug"                    { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.DEBUG_KEYWORD; }
    "standalone"               { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.STANDALONE_KEYWORD; }
    "cup"                      { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.CUP_KEYWORD; }
    "cupsym"                   { yybegin(OPT_PARAM); return JFlexTokenTypes.CUPSYM_KEYWORD; }
    "cupdebug"                 { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.CUPDEBUG_KEYWORD; }
    "byacc"                    { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.BYACC_KEYWORD; }
    "switch"                   { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.SWITCH_KEYWORD; }
    "table"                    { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.TABLE_KEYWORD; }
    "pack"                     { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.PACK_KEYWORD; }
    "7bit"                     { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes._7BIT_KEYWORD; }
    "full"                     { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.FULL_KEYWORD; }
    "8bit"                     { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes._8BIT_KEYWORD; }
    "unicode"                  { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.UNICODE_KEYWORD; }
    "16bit"                    { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes._16BIT_KEYWORD; }
    "caseless"                 { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.CASELESS_KEYWORD; }
    "ignorecase"               { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.IGNORECASE_KEYWORD; }
    "char"                     { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.CHAR_KEYWORD; }
    "line"                     { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.LINE_KEYWORD; }
    "column"                   { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.COLUMN_KEYWORD; }
    "notunix"                  { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.NOTUNIX_KEYWORD; }
    "yyeof"                    { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.YYEOF_KEYWORD; }
    "state"                    { yybegin(OPT_PARAMS); return JFlexTokenTypes.STATE_KEYWORD; }
    "s"                        { yybegin(OPT_PARAMS); return JFlexTokenTypes.S_KEYWORD; }
    "xstate"                   { yybegin(OPT_PARAMS); return JFlexTokenTypes.XSTATE_KEYWORD; }
    "x"                        { yybegin(OPT_PARAMS); return JFlexTokenTypes.X_KEYWORD; }
    "{"                        { yybegin(OPT_JAVA_CODE); return JFlexTokenTypes.OPTION_LEFT_BRACE; }
    "}"                        { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.OPTION_RIGHT_BRACE; }
    {LineTerminator}+          { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.WHITE_SPACE; }
    {WhiteSpace}+              { yybegin(OPT_ERROR); return JFlexTokenTypes.OPTION_WHITE_SPACE; }
    .                          { yybegin(OPT_ERROR); return JFlexTokenTypes.BAD_CHARACTER; }
}
<OPT_ERROR> {
    {LineTerminator}+          { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.WHITE_SPACE; }
    {WhiteSpace}+              { yybegin(OPT_ERROR); return JFlexTokenTypes.OPTION_WHITE_SPACE; }
    .                          { yybegin(OPT_ERROR); return JFlexTokenTypes.BAD_CHARACTER; }
}
<OPT_PARAM> {
    {LineTerminator}+          { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.WHITE_SPACE; }
    {WhiteSpace}+              { yybegin(OPT_PARAM); return JFlexTokenTypes.OPTION_WHITE_SPACE; }
    {Identifier}               { yybegin(OPT_PARAM); return JFlexTokenTypes.OPTION_PARAMETER; }
    .                          { yybegin(OPT_PARAM); return JFlexTokenTypes.BAD_CHARACTER; }
}
<OPT_PARAMS> {
    {LineTerminator}+          { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.WHITE_SPACE; }
    {WhiteSpace}+              { yybegin(OPT_PARAMS); return JFlexTokenTypes.OPTION_WHITE_SPACE; }
    {Identifier}               { yybegin(OPT_PARAMS); return JFlexTokenTypes.OPTION_PARAMETER; }
    ","                        { yybegin(OPT_PARAMS); return JFlexTokenTypes.OPTION_COMMA; }
    .                          { yybegin(OPT_PARAMS); return JFlexTokenTypes.BAD_CHARACTER; }
}
<OPT_CODE> {
    {LineTerminator}+          { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.WHITE_SPACE; }
    "{"                        { yybegin(OPT_JAVA_CODE); return JFlexTokenTypes.OPTION_LEFT_BRACE; }
    "}"                        { yybegin(OPT_CODE); return JFlexTokenTypes.OPTION_RIGHT_BRACE; }
    .                          { yybegin(OPT_CODE); return JFlexTokenTypes.BAD_CHARACTER; }
}
<OPT_JAVA_CODE> {
    {JavaCodeEndOption}        { yybegin(OPTIONS_AND_DECLARATIONS); yypushback(yylength()); }
    {AnySpace}+                { yybegin(OPT_JAVA_CODE); return JFlexTokenTypes.JAVA_CODE; }
    .                          { yybegin(OPT_JAVA_CODE); return JFlexTokenTypes.JAVA_CODE; }
}
<OPT_EXCEPTION> {
    {LineTerminator}+          { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.WHITE_SPACE; }
    "{"                        { yybegin(OPT_EXCEPTION); return JFlexTokenTypes.OPTION_LEFT_BRACE; }
    "}"                        { yybegin(OPT_EXCEPTION); return JFlexTokenTypes.OPTION_RIGHT_BRACE; }
    {WhiteSpace}+              { yybegin(OPT_EXCEPTION); return JFlexTokenTypes.OPTION_WHITE_SPACE; }
    {Identifier}               { yybegin(OPT_EXCEPTION); return JFlexTokenTypes.OPTION_PARAMETER; }
    ","                        { yybegin(OPT_EXCEPTION); return JFlexTokenTypes.OPTION_COMMA; }
    .                          { yybegin(OPT_EXCEPTION); return JFlexTokenTypes.BAD_CHARACTER; }
}
<OPT_BOOLEAN> {
    {LineTerminator}+          { yybegin(OPTIONS_AND_DECLARATIONS); return JFlexTokenTypes.WHITE_SPACE; }
    {WhiteSpace}+              { yybegin(OPT_BOOLEAN); return JFlexTokenTypes.OPTION_WHITE_SPACE; }
    "false"                    { yybegin(OPT_BOOLEAN); return JFlexTokenTypes.FALSE_KEYWORD; }
    "true"                     { yybegin(OPT_BOOLEAN); return JFlexTokenTypes.TRUE_KEYWORD; }
    .                          { yybegin(OPT_BOOLEAN); return JFlexTokenTypes.BAD_CHARACTER; }
}

<STATE> {
    {LineTerminator}+          { yybegin(LEXICAL_RULES); return JFlexTokenTypes.WHITE_SPACE; }
    {WhiteSpace}+              { yybegin(STATE); return JFlexTokenTypes.WHITE_SPACE; }
    {Identifier}               { yybegin(STATE); return JFlexTokenTypes.STATE_REF; }
    ","                        { yybegin(STATE); return JFlexTokenTypes.STATE_COMMA; }
    ">"                        { yybegin(RULE); return JFlexTokenTypes.STATE_LEFT_ANGLE_BRACKET; }
    .                          { yybegin(STATE); return JFlexTokenTypes.BAD_CHARACTER; }
}

<RULE> {
    {AnySpace}+                { yybegin(RULE); return JFlexTokenTypes.WHITE_SPACE; }
    {Comment}                  { yybegin(RULE); return JFlexTokenTypes.COMMENT; }
    {IdentifierReference}      { yybegin(ACTION_REGEXP); yypushback(yylength()); }
    "{"                        { yybegin(RULE); return JFlexTokenTypes.LEFT_BRACE; }
    "<"                        { yybegin(STATE); return JFlexTokenTypes.STATE_RIGHT_ANGLE_BRACKET; }
    "}"                        { yybegin(LEXICAL_RULES); return JFlexTokenTypes.RIGHT_BRACE; }
    .                          { yybegin(ACTION_REGEXP); yypushback(yylength()); }
}
<ACTION_REGEXP> {
    {LineTerminator}+          { yybegin(LEXICAL_RULES); return JFlexTokenTypes.WHITE_SPACE; }
    {WhiteSpace}+              { yybegin(ACTION_REGEXP); return JFlexTokenTypes.WHITE_SPACE; }
    {EscapedCharacter}         { yybegin(ACTION_REGEXP); return JFlexTokenTypes.REGEXP_SYMBOL; }
    {IdentifierReference}      { yybegin(ACTION_REGEXP_IDENTIFIER); yypushback(yylength());}
    "{"                        { braceCounter = 0; yybegin(ACTION_JAVA_CODE); return JFlexTokenTypes.LEFT_BRACE; }
    "["                        { yybegin(ACTION_REGEXP_CLASS); return JFlexTokenTypes.REGEXP_LEFT_BRACKET; }
    "|"                        { yybegin(ACTION_REGEXP); return JFlexTokenTypes.REGEXP_OR; }
    "("                        { yybegin(ACTION_REGEXP); return JFlexTokenTypes.REGEXP_LEFT_PARENTHESIS; }
    ")"                        { yybegin(ACTION_REGEXP); return JFlexTokenTypes.REGEXP_RIGHT_PARENTHESIS; }
    {RegExpPrefix}             { yybegin(ACTION_REGEXP); return JFlexTokenTypes.REGEXP_PREFIX; }
    {RegExpPostfix}            { yybegin(ACTION_REGEXP); return JFlexTokenTypes.REGEXP_POSTFIX; }
    {StringLiteral}            { yybegin(ACTION_REGEXP); return JFlexTokenTypes.REGEXP_STRING_LITERAL; }
    .                          { yybegin(ACTION_REGEXP); return JFlexTokenTypes.REGEXP_SYMBOL; }
}
<ACTION_REGEXP_IDENTIFIER> {
    {Identifier}               { yybegin(ACTION_REGEXP_IDENTIFIER); return JFlexTokenTypes.REGEXP_MACROS_REF; }
    "{"                        { yybegin(ACTION_REGEXP_IDENTIFIER); return JFlexTokenTypes.REGEXP_LEFT_BRACE; }
    "}"                        { yybegin(ACTION_REGEXP); return JFlexTokenTypes.REGEXP_RIGHT_BRACE; }
    .                          { yybegin(ACTION_REGEXP); return JFlexTokenTypes.BAD_CHARACTER; }
}
<ACTION_REGEXP_CLASS> {
    {LineTerminator}+          { yybegin(LEXICAL_RULES); return JFlexTokenTypes.WHITE_SPACE; }
    {EscapedCharacter}         { yybegin(ACTION_REGEXP_CLASS); return JFlexTokenTypes.REGEXP_CLASS_SYMBOL; }
    "]"                        { yybegin(ACTION_REGEXP); return JFlexTokenTypes.REGEXP_RIGHT_BRACKET;}
    {StringLiteral}            { yybegin(ACTION_REGEXP_CLASS); return JFlexTokenTypes.REGEXP_STRING_LITERAL; }
    .                          { yybegin(ACTION_REGEXP_CLASS); return JFlexTokenTypes.REGEXP_CLASS_SYMBOL; }
}
<ACTION> {
    {AnySpace}+                { yybegin(ACTION); return JFlexTokenTypes.WHITE_SPACE; }
    "{"                        { braceCounter = 0; yybegin(ACTION_JAVA_CODE); return JFlexTokenTypes.LEFT_BRACE; }
    .                          { yybegin(RULE); return JFlexTokenTypes.BAD_CHARACTER; }
}
<ACTION_JAVA_CODE> {
    {AnySpace}+                { yybegin(ACTION_JAVA_CODE); return JFlexTokenTypes.JAVA_CODE; }
    "{"                        { braceCounter++; yybegin(ACTION_JAVA_CODE); return JFlexTokenTypes.JAVA_CODE; }
    "}"                        { braceCounter--; if (braceCounter < 0) {yybegin(RULE); return JFlexTokenTypes.RIGHT_BRACE; } else { yybegin(ACTION_JAVA_CODE); return JFlexTokenTypes.JAVA_CODE; }}
    .                          { yybegin(ACTION_JAVA_CODE); return JFlexTokenTypes.JAVA_CODE; }
}
