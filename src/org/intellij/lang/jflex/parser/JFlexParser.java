package org.intellij.lang.jflex.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;
import org.intellij.lang.jflex.JFlexElementTypes;
import org.intellij.lang.jflex.util.JFlexBundle;
import org.jetbrains.annotations.NotNull;

/**
 * Parser.
 *
 * @author Alexey Efimov, Max Ishchenko
 */
public class JFlexParser implements PsiParser {

    private static final Logger LOG = Logger.getInstance("#JFlexParser");

    private final Project project;

    public JFlexParser(Project project) {
        this.project = project;
    }

    @NotNull
    public ASTNode parse(IElementType root, PsiBuilder builder) {

        final PsiBuilder.Marker rootMarker = builder.mark();

        while (!builder.eof()) {
            parse(builder);
        }
        rootMarker.done(root);

        return builder.getTreeBuilt();

    }

    private void parse(PsiBuilder builder) {
        IElementType first = builder.getTokenType();
        if (first == JFlexElementTypes.MACROS) {
            parseMacroDefinition(builder);
        } else if (first == JFlexElementTypes.CLASS_KEYWORD) {
            parseClassStatement(builder);
        } else if (first == JFlexElementTypes.STATE_KEYWORD) {
            parseStateStatement(builder);
        } else if (first == JFlexElementTypes.XSTATE_KEYWORD) {
            parseXstateStatement(builder);
        } else if (first == JFlexElementTypes.STATE_REF) {
            parseStateReference(builder);
        } else if (first == JFlexElementTypes.IMPLEMENTS_KEYWORD) {
            parseImplementsStatement(builder);
        } else if (first == JFlexElementTypes.TYPE_KEYWORD) {
            parseTypeStatement(builder);
        } else if (first == JFlexElementTypes.JAVA_CODE) {
            parseJavaCode(builder);
        } else if (first == JFlexElementTypes.REGEXP_MACROS_REF) {
            parseMacroReference(builder);
        } else {
            builder.advanceLexer();
        }
    }

    private void parseStateReference(PsiBuilder builder) {
        PsiBuilder.Marker mark = builder.mark();
        builder.advanceLexer();
        mark.done(JFlexElementTypes.STATE_REF);
    }

    private void parseCommaSeparatedOptionStatement(PsiBuilder builder, IElementType finishWith) {
        parseCommaSeparatedOptionStatement(builder, finishWith, JFlexElementTypes.OPTION_PARAMETER);
    }

    private void parseCommaSeparatedOptionStatement(PsiBuilder builder, IElementType finishWith, IElementType markWith) {

        PsiBuilder.Marker stateMarker = builder.mark();
        builder.advanceLexer();

        boolean first = true;

        while (builder.getTokenType() == JFlexElementTypes.OPTION_PARAMETER || builder.getTokenType() == JFlexElementTypes.OPTION_COMMA) {

            if (first) {
                first = false;
            } else {
                //parsing commas or go to next expr
                if (builder.getTokenType() == JFlexElementTypes.OPTION_COMMA) {
                    builder.advanceLexer();
                } else {
                    builder.error(JFlexBundle.message("parser.comma.expected"));
                }
            }

            PsiBuilder.Marker interfaceMarker = builder.mark();
            if (builder.getTokenType() == JFlexElementTypes.OPTION_PARAMETER) {
                builder.advanceLexer();
                interfaceMarker.done(markWith);
            } else {
                builder.error(JFlexBundle.message("parser.expression.expected"));
                interfaceMarker.drop();
                break;
            }
        }

        stateMarker.done(finishWith);

    }

    private void parseStateStatement(PsiBuilder builder) {
        parseCommaSeparatedOptionStatement(builder, JFlexElementTypes.STATE_STATEMENT, JFlexElementTypes.STATE_DEFINITION);

    }

    private void parseXstateStatement(PsiBuilder builder) {
        parseCommaSeparatedOptionStatement(builder, JFlexElementTypes.STATE_STATEMENT, JFlexElementTypes.STATE_DEFINITION);
    }

    private void parseMacroDefinition(PsiBuilder builder) {

        PsiBuilder.Marker macroDefinition = builder.mark();
        builder.advanceLexer();

        if (builder.getTokenType() != JFlexElementTypes.EQ) {
            builder.error(JFlexBundle.message("parser.eq.expected"));
        } else {
            builder.advanceLexer();
        }

        int found = 0;
        PsiBuilder.Marker macrovalue = builder.mark();

        while (JFlexElementTypes.REGEXP_SCOPE.contains(builder.getTokenType())) {
            found++;
            builder.advanceLexer();
        }

        if (found == 0) {
            macrovalue.drop();
            builder.error(JFlexBundle.message("parser.macrovalue.expected"));
        } else {
            macrovalue.done(JFlexElementTypes.REGEXP);
        }

        macroDefinition.done(JFlexElementTypes.MACRO_DEFINITION);

    }

    private void parseMacroReference(PsiBuilder builder) {
        PsiBuilder.Marker macroMarker = builder.mark();
        builder.advanceLexer();
        macroMarker.done(JFlexElementTypes.REGEXP_MACROS_REF);
    }

    private void parseImplementsStatement(PsiBuilder builder) {
        parseCommaSeparatedOptionStatement(builder, JFlexElementTypes.IMPLEMENTS_STATEMENT);
    }

    private void parseTypeStatement(PsiBuilder builder) {
        LOG.assertTrue(builder.getTokenType() == JFlexElementTypes.TYPE_KEYWORD);
        PsiBuilder.Marker marker = builder.mark();
        builder.advanceLexer();
        parseOptionParamExpression(builder);
        marker.done(JFlexElementTypes.TYPE_STATEMENT);
    }

    private void parseJavaCode(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        builder.advanceLexer();
        marker.done(JFlexElementTypes.JAVA_CODE);
    }

    private void parseClassStatement(PsiBuilder builder) {
        LOG.assertTrue(builder.getTokenType() == JFlexElementTypes.CLASS_KEYWORD);
        PsiBuilder.Marker marker = builder.mark();
        builder.advanceLexer();
        parseOptionParamExpression(builder);
        marker.done(JFlexElementTypes.CLASS_STATEMENT);
    }

    private void parseOptionParamExpression(PsiBuilder builder) {
        PsiBuilder.Marker expr = builder.mark();
        if (builder.getTokenType() != JFlexElementTypes.OPTION_PARAMETER) {
            builder.error(JFlexBundle.message("parser.expression.expected"));
            expr.drop();
        } else {
            builder.advanceLexer();
            expr.done(JFlexElementTypes.OPTION_PARAMETER);
        }
    }
}
