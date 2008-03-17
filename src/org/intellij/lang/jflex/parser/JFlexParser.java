package org.intellij.lang.jflex.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;
import org.intellij.lang.jflex.JFlexElementTypes;
import org.jetbrains.annotations.NotNull;

/**
 * Parser.
 *
 * @author Alexey Efimov
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
        if (first == JFlexElementTypes.CLASS_KEYWORD) {
            parseClassStatement(builder);
            return;
        } else if (first == JFlexElementTypes.TYPE_KEYWORD) {
            parseTypeStatement(builder);
            return;
        } else if (first == JFlexElementTypes.JAVA_CODE) {
            parseJavaCode(builder);
            return;
        }
        builder.advanceLexer();
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
            builder.error("Expected expression");
            expr.drop();
        } else {
            builder.advanceLexer();
            expr.done(JFlexElementTypes.OPTION_PARAMETER);
        }
    }
}
