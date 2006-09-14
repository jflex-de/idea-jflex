package org.intellij.lang.jflex.fileTypes;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.tree.IElementType;
import org.intellij.lang.jflex.JFlexElementType;
import org.intellij.lang.jflex.JFlexTokenTypes;
import org.intellij.lang.jflex.editor.JFlexHighlighterColors;
import org.intellij.lang.jflex.lexer.JFlexHighlighterLexer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * JFlex syntax highlighter
 *
 * @author Alexey Efimov
 */
public final class JFlexSyntaxHighlighter extends SyntaxHighlighterBase {

    private final JFlexHighlighterLexer lexer;
    private final Map<IElementType, TextAttributesKey> colors = new HashMap<IElementType, TextAttributesKey>();
    private final Map<IElementType, TextAttributesKey> backgrounds = new HashMap<IElementType, TextAttributesKey>();

    public JFlexSyntaxHighlighter(Project project, VirtualFile virtualFile) {
        lexer = new JFlexHighlighterLexer(project, virtualFile);

        fillMap(colors, JFlexTokenTypes.BRACES, JFlexHighlighterColors.BRACES);
        fillMap(colors, JFlexTokenTypes.BRACKETS, JFlexHighlighterColors.BRACKETS);
        fillMap(colors, JFlexTokenTypes.PARENTHESES, JFlexHighlighterColors.PARENTHS);
        fillMap(colors, JFlexTokenTypes.ANGLE_BRACKETS, JFlexHighlighterColors.ANGLE_BRACKETS);

        fillMap(colors, JFlexTokenTypes.OPERATORS, JFlexHighlighterColors.OPERATION_SIGN);

        colors.put(JFlexTokenTypes.BAD_CHARACTER, JFlexHighlighterColors.BAD_CHARACTER);
        colors.put(JFlexTokenTypes.COMMENT, JFlexHighlighterColors.COMMENT);
        colors.put(JFlexTokenTypes.STRING_LITERAL, JFlexHighlighterColors.STRING);
        colors.put(JFlexTokenTypes.COMMA, JFlexHighlighterColors.COMMA);

        backgrounds.put(JFlexTokenTypes.JAVA_CODE, JFlexHighlighterColors.JAVA_CODE);

        colors.put(JFlexTokenTypes.MACROS, JFlexHighlighterColors.MACROS);
        colors.put(JFlexTokenTypes.MACROS_REF, JFlexHighlighterColors.MACROS_REF);



        colors.put(JFlexTokenTypes.STATE_REF, JFlexHighlighterColors.STATE_REF);

        fillMap(backgrounds, JFlexTokenTypes.REGEXP_SCOPE, JFlexHighlighterColors.REGEXP_BACKGROUND);
        colors.put(JFlexTokenTypes.REGEXP_SYMBOL, JFlexHighlighterColors.REGEXP_SYMBOL);
        colors.put(JFlexTokenTypes.REGEXP_CLASS_SYMBOL, JFlexHighlighterColors.REGEXP_CLASS_SYMBOL);

        fillMap(backgrounds, JFlexTokenTypes.OPTION_SCOPE, JFlexHighlighterColors.OPTION_BACKGROUND);
        fillMap(colors, JFlexTokenTypes.OPTION_KEYWORDS, JFlexHighlighterColors.OPTION_KEYWORD);
        colors.put(JFlexTokenTypes.OPTION_PARAMETER, JFlexHighlighterColors.OPTION_PARAMETER);
        colors.put(JFlexTokenTypes.OPTION_SIGN, JFlexHighlighterColors.OPTION_SIGN);

        colors.put(JFlexTokenTypes.SECTION_SIGN, JFlexHighlighterColors.SECTION_SIGN);
    }

    @NotNull
    public Lexer getHighlightingLexer() {
        return lexer;
    }

    @NotNull
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        return pack(getAttributeKeys(tokenType, backgrounds), getAttributeKeys(tokenType, colors));
    }

    private TextAttributesKey getAttributeKeys(IElementType tokenType, Map<IElementType, TextAttributesKey> map) {
        TextAttributesKey attributes = map.get(tokenType);
        if (attributes == null && tokenType instanceof JFlexElementType) {
            return map.get(((JFlexElementType)tokenType).getParsedType());
        }
        return map.get(tokenType);
    }
}
