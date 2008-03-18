package org.intellij.lang.jflex;

import com.intellij.lang.Language;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;

public interface JFlexElementTypes {
    IFileElementType FILE = new IFileElementType(Language.findInstance(JFlexLanguage.class));

    IElementType BAD_CHARACTER = TokenType.BAD_CHARACTER;
    IElementType WHITE_SPACE = TokenType.WHITE_SPACE;

    IElementType STRING_LITERAL = new JFlexElementType("STRING_LITERAL");
    IElementType IDENTIFIER = new JFlexElementType("IDENTIFIER");

    IElementType JAVA_CODE = new JFlexElementType("JAVA_CODE");
    IElementType COMMENT = new JFlexElementType("COMMENT");

    IElementType MACROS = new JFlexElementType("MACROS");
    IElementType MACROS_REF = new JFlexElementType("MACROS_REF");

    IElementType EQ = new JFlexElementType("EQ");
    IElementType COMMA = new JFlexElementType("COMMA");
    IElementType OR = new JFlexElementType("OR");

    IElementType LEFT_BRACE = new JFlexElementType("LEFT_BRACE");
    IElementType RIGHT_BRACE = new JFlexElementType("RIGHT_BRACE");
    IElementType RIGHT_BRACKET = new JFlexElementType("RIGHT_BRACKET");
    IElementType LEFT_BRACKET = new JFlexElementType("LEFT_BRACKET");
    IElementType RIGHT_ANGLE_BRACKET = new JFlexElementType("RIGHT_ANGLE_BRACKET");
    IElementType LEFT_ANGLE_BRACKET = new JFlexElementType("LEFT_ANGLE_BRACKET");
    IElementType RIGHT_PARENTHESIS = new JFlexElementType("RIGHT_PARENTHESIS");
    IElementType LEFT_PARENTHESIS = new JFlexElementType("LEFT_PARENTHESIS");

    IElementType SECTION_SIGN = new JFlexElementType("SECTION_SIGN");

    IElementType OPTION_SIGN = new JFlexElementType("OPTION_SIGN");
    IElementType OPTION_PARAMETER = new JFlexElementType("OPTION_PARAMETER", IDENTIFIER);
    IElementType OPTION_WHITE_SPACE = new JFlexElementType("OPTION_WHITE_SPACE", WHITE_SPACE);
    IElementType OPTION_LEFT_BRACE = new JFlexElementType("OPTION_LEFT_BRACE", LEFT_BRACE);
    IElementType OPTION_RIGHT_BRACE = new JFlexElementType("OPTION_RIGHT_BRACE", RIGHT_BRACE);
    IElementType OPTION_COMMA = new JFlexElementType("OPTION_COMMA", COMMA);

    IElementType STATE_REF = new JFlexElementType("STATE_REF", IDENTIFIER);
    IElementType STATE_COMMA = new JFlexElementType("STATE_COMMA", COMMA);
    IElementType STATE_LEFT_ANGLE_BRACKET = new JFlexElementType("STATE_LEFT_ANGLE_BRACKET", LEFT_ANGLE_BRACKET);
    IElementType STATE_RIGHT_ANGLE_BRACKET = new JFlexElementType("STATE_LEFT_ANGLE_BRACKET", RIGHT_ANGLE_BRACKET);

    IElementType REGEXP_WHITE_SPACE = new JFlexElementType("REGEXP_WHITE_SPACE", WHITE_SPACE);
    IElementType REGEXP_PREFIX = new JFlexElementType("REGEXP_PREFIX");
    IElementType REGEXP_POSTFIX = new JFlexElementType("REGEXP_PREFIX");
    IElementType REGEXP_SYMBOL = new JFlexElementType("REGEXP_SYMBOL");
    IElementType REGEXP_CLASS_SYMBOL = new JFlexElementType("REGEXP_CLASS_SYMBOL", REGEXP_SYMBOL);
    IElementType REGEXP_LEFT_BRACKET = new JFlexElementType("REGEXP_LEFT_BRACKET", LEFT_BRACKET);
    IElementType REGEXP_RIGHT_BRACKET = new JFlexElementType("REGEXP_RIGHT_BRACKET", RIGHT_BRACKET);
    IElementType REGEXP_LEFT_BRACE = new JFlexElementType("REGEXP_LEFT_BRACE", LEFT_BRACE);
    IElementType REGEXP_RIGHT_BRACE = new JFlexElementType("REGEXP_RIGHT_BRACE", RIGHT_BRACE);
    IElementType REGEXP_LEFT_PARENTHESIS = new JFlexElementType("REGEXP_LEFT_PARENTHESIS", LEFT_PARENTHESIS);
    IElementType REGEXP_RIGHT_PARENTHESIS = new JFlexElementType("REGEXP_RIGHT_PARENTHESIS", RIGHT_PARENTHESIS);
    IElementType REGEXP_OR = new JFlexElementType("REGEXP_OR", OR);
    IElementType REGEXP_STRING_LITERAL = new JFlexElementType("REGEXP_STRING_LITERAL", STRING_LITERAL);
    IElementType REGEXP_MACROS_REF = new JFlexElementType("REGEXP_MACROS_REF", MACROS_REF);

    IElementType CLASS_KEYWORD = new JFlexElementType("CLASS_KEYWORD");
    IElementType IMPLEMENTS_KEYWORD = new JFlexElementType("IMPLEMENTS_KEYWORD");
    IElementType EXTENDS_KEYWORD = new JFlexElementType("EXTENDS_KEYWORD");
    IElementType PUBLIC_KEYWORD = new JFlexElementType("PUBLIC_KEYWORD");
    IElementType FINAL_KEYWORD = new JFlexElementType("FINAL_KEYWORD");
    IElementType ABSTRACT_KEYWORD = new JFlexElementType("ABSTRACT_KEYWORD");
    IElementType APIPRIVATE_KEYWORD = new JFlexElementType("APIPRIVATE_KEYWORD");
    IElementType INIT_KEYWORD = new JFlexElementType("INIT_KEYWORD");
    IElementType INITTHROW_KEYWORD = new JFlexElementType("INITTHROW_KEYWORD");
    IElementType SCANERROR_KEYWORD = new JFlexElementType("SCANERROR_KEYWORD");
    IElementType BUFFERSIZE_KEYWORD = new JFlexElementType("BUFFERSIZE_KEYWORD");
    IElementType INCLUDE_KEYWORD = new JFlexElementType("INCLUDE_KEYWORD");
    IElementType FUNCTION_KEYWORD = new JFlexElementType("FUNCTION_KEYWORD");
    IElementType INT_KEYWORD = new JFlexElementType("INT_KEYWORD");
    IElementType INTEGER_KEYWORD = new JFlexElementType("INTEGER_KEYWORD");
    IElementType INTWRAP_KEYWORD = new JFlexElementType("INTWRAP_KEYWORD");
    IElementType TYPE_KEYWORD = new JFlexElementType("TYPE_KEYWORD");
    IElementType YYLEXTHROW_KEYWORD = new JFlexElementType("YYLEXTHROW_KEYWORD");
    IElementType EOF_KEYWORD = new JFlexElementType("EOF_KEYWORD");
    IElementType EOFVAL_KEYWORD = new JFlexElementType("EOFVAL_KEYWORD");
    IElementType EOFTHROW_KEYWORD = new JFlexElementType("EOFTHROW_KEYWORD");
    IElementType EOFCLOSE_KEYWORD = new JFlexElementType("EOFCLOSE_KEYWORD");
    IElementType DEBUG_KEYWORD = new JFlexElementType("DEBUG_KEYWORD");
    IElementType STANDALONE_KEYWORD = new JFlexElementType("STANDALONE_KEYWORD");
    IElementType CUP_KEYWORD = new JFlexElementType("CUP_KEYWORD");
    IElementType CUPSYM_KEYWORD = new JFlexElementType("CUPSYM_KEYWORD");
    IElementType CUPDEBUG_KEYWORD = new JFlexElementType("CUPDEBUG_KEYWORD");
    IElementType BYACC_KEYWORD = new JFlexElementType("BYACC_KEYWORD");
    IElementType SWITCH_KEYWORD = new JFlexElementType("SWITCH_KEYWORD");
    IElementType TABLE_KEYWORD = new JFlexElementType("TABLE_KEYWORD");
    IElementType PACK_KEYWORD = new JFlexElementType("PACK_KEYWORD");
    IElementType _7BIT_KEYWORD = new JFlexElementType("7BIT_KEYWORD");
    IElementType _8BIT_KEYWORD = new JFlexElementType("8BIT_KEYWORD");
    IElementType _16BIT_KEYWORD = new JFlexElementType("16BIT_KEYWORD");
    IElementType FULL_KEYWORD = new JFlexElementType("FULL_KEYWORD");
    IElementType UNICODE_KEYWORD = new JFlexElementType("UNICODE_KEYWORD");
    IElementType CASELESS_KEYWORD = new JFlexElementType("CASELESS_KEYWORD");
    IElementType IGNORECASE_KEYWORD = new JFlexElementType("IGNORECASE_KEYWORD");
    IElementType CHAR_KEYWORD = new JFlexElementType("CHAR_KEYWORD");
    IElementType LINE_KEYWORD = new JFlexElementType("LINE_KEYWORD");
    IElementType COLUMN_KEYWORD = new JFlexElementType("COLUMN_KEYWORD");
    IElementType NOTUNIX_KEYWORD = new JFlexElementType("NOTUNIX_KEYWORD");
    IElementType YYEOF_KEYWORD = new JFlexElementType("YYEOF_KEYWORD");
    IElementType STATE_KEYWORD = new JFlexElementType("STATE_KEYWORD");
    IElementType S_KEYWORD = new JFlexElementType("S_KEYWORD");
    IElementType XSTATE_KEYWORD = new JFlexElementType("XSTATE_KEYWORD");
    IElementType X_KEYWORD = new JFlexElementType("X_KEYWORD");
    IElementType FALSE_KEYWORD = new JFlexElementType("FALSE_KEYWORD");
    IElementType TRUE_KEYWORD = new JFlexElementType("TRUE_KEYWORD");

    TokenSet OPTION_KEYWORDS = TokenSet.create(
            CLASS_KEYWORD,
            IMPLEMENTS_KEYWORD,
            EXTENDS_KEYWORD,
            PUBLIC_KEYWORD,
            FINAL_KEYWORD,
            ABSTRACT_KEYWORD,
            APIPRIVATE_KEYWORD,
            INIT_KEYWORD,
            INITTHROW_KEYWORD,
            SCANERROR_KEYWORD,
            BUFFERSIZE_KEYWORD,
            INCLUDE_KEYWORD,
            FUNCTION_KEYWORD,
            INT_KEYWORD,
            INTEGER_KEYWORD,
            INTWRAP_KEYWORD,
            TYPE_KEYWORD,
            YYLEXTHROW_KEYWORD,
            EOF_KEYWORD,
            EOFVAL_KEYWORD,
            EOFTHROW_KEYWORD,
            EOFCLOSE_KEYWORD,
            DEBUG_KEYWORD,
            STANDALONE_KEYWORD,
            CUP_KEYWORD,
            CUPSYM_KEYWORD,
            CUPDEBUG_KEYWORD,
            BYACC_KEYWORD,
            SWITCH_KEYWORD,
            TABLE_KEYWORD,
            PACK_KEYWORD,
            _7BIT_KEYWORD,
            _8BIT_KEYWORD,
            _16BIT_KEYWORD,
            FULL_KEYWORD,
            UNICODE_KEYWORD,
            CASELESS_KEYWORD,
            IGNORECASE_KEYWORD,
            CHAR_KEYWORD,
            LINE_KEYWORD,
            COLUMN_KEYWORD,
            NOTUNIX_KEYWORD,
            YYEOF_KEYWORD,
            STATE_KEYWORD,
            S_KEYWORD,
            XSTATE_KEYWORD,
            X_KEYWORD,
            FALSE_KEYWORD,
            TRUE_KEYWORD
    );

    TokenSet OPERATORS = TokenSet.create(
            EQ,
            OR
    );

    TokenSet BRACES = TokenSet.create(
            LEFT_BRACE,
            RIGHT_BRACE
    );

    TokenSet BRACKETS = TokenSet.create(
            LEFT_BRACKET,
            RIGHT_BRACKET
    );

    TokenSet PARENTHESES = TokenSet.create(
            LEFT_PARENTHESIS,
            RIGHT_PARENTHESIS
    );

    TokenSet ANGLE_BRACKETS = TokenSet.create(
            LEFT_ANGLE_BRACKET,
            RIGHT_ANGLE_BRACKET
    );

    TokenSet OPTION_SCOPE = TokenSet.orSet(OPTION_KEYWORDS, TokenSet.create(
            OPTION_COMMA,
            OPTION_LEFT_BRACE,
            OPTION_RIGHT_BRACE,
            OPTION_SIGN,
            OPTION_WHITE_SPACE,
            OPTION_PARAMETER
    ));

    TokenSet REGEXP_SCOPE = TokenSet.create(
            REGEXP_STRING_LITERAL,
            REGEXP_CLASS_SYMBOL,
            REGEXP_LEFT_BRACKET,
            REGEXP_LEFT_PARENTHESIS,
            REGEXP_LEFT_BRACE,
            REGEXP_RIGHT_BRACKET,
            REGEXP_RIGHT_PARENTHESIS,
            REGEXP_RIGHT_BRACE,
            REGEXP_OR,
            REGEXP_POSTFIX,
            REGEXP_PREFIX,
            REGEXP_SYMBOL,
            REGEXP_MACROS_REF,
            REGEXP_WHITE_SPACE
    );

    TokenSet STATE_SCOPE = TokenSet.create(
            STATE_COMMA,
            STATE_REF,
            STATE_LEFT_ANGLE_BRACKET,
            STATE_RIGHT_ANGLE_BRACKET
    );

    TokenSet WHITE_SPACES = TokenSet.create(
            WHITE_SPACE,
            OPTION_WHITE_SPACE,
            REGEXP_WHITE_SPACE
    );

    TokenSet COMMENTS = TokenSet.create(COMMENT);

    TokenSet EXPRESSIONS = TokenSet.create(
            OPTION_PARAMETER
    );
    IElementType CLASS_STATEMENT = new JFlexElementType("CLASS_STATEMENT");
    IElementType TYPE_STATEMENT = new JFlexElementType("TYPE_STATEMENT");
    IElementType IMPLEMENTS_STATEMENT = new JFlexElementType("IMPLEMENTS_STATEMENT");
}
