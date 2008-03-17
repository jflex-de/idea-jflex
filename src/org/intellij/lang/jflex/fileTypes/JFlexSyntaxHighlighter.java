package org.intellij.lang.jflex.fileTypes;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.tree.IElementType;
import org.intellij.lang.jflex.JFlexElementType;
import org.intellij.lang.jflex.JFlexElementTypes;
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

        lexer = new JFlexHighlighterLexer();

        fillMap(colors, JFlexElementTypes.BRACES, JFlexHighlighterColors.BRACES);
        fillMap(colors, JFlexElementTypes.BRACKETS, JFlexHighlighterColors.BRACKETS);
        fillMap(colors, JFlexElementTypes.PARENTHESES, JFlexHighlighterColors.PARENTHS);
        fillMap(colors, JFlexElementTypes.ANGLE_BRACKETS, JFlexHighlighterColors.ANGLE_BRACKETS);

        fillMap(colors, JFlexElementTypes.OPERATORS, JFlexHighlighterColors.OPERATION_SIGN);

        colors.put(JFlexElementTypes.BAD_CHARACTER, JFlexHighlighterColors.BAD_CHARACTER);
        colors.put(JFlexElementTypes.COMMENT, JFlexHighlighterColors.COMMENT);
        colors.put(JFlexElementTypes.STRING_LITERAL, JFlexHighlighterColors.STRING);
        colors.put(JFlexElementTypes.COMMA, JFlexHighlighterColors.COMMA);

        backgrounds.put(JFlexElementTypes.JAVA_CODE, JFlexHighlighterColors.JAVA_CODE);

        colors.put(JFlexElementTypes.MACROS, JFlexHighlighterColors.MACROS);
        colors.put(JFlexElementTypes.MACROS_REF, JFlexHighlighterColors.MACROS_REF);


        colors.put(JFlexElementTypes.STATE_REF, JFlexHighlighterColors.STATE_REF);

        fillMap(backgrounds, JFlexElementTypes.REGEXP_SCOPE, JFlexHighlighterColors.REGEXP_BACKGROUND);
        colors.put(JFlexElementTypes.REGEXP_SYMBOL, JFlexHighlighterColors.REGEXP_SYMBOL);
        colors.put(JFlexElementTypes.REGEXP_CLASS_SYMBOL, JFlexHighlighterColors.REGEXP_CLASS_SYMBOL);

        fillMap(backgrounds, JFlexElementTypes.OPTION_SCOPE, JFlexHighlighterColors.OPTION_BACKGROUND);
        fillMap(colors, JFlexElementTypes.OPTION_KEYWORDS, JFlexHighlighterColors.OPTION_KEYWORD);
        colors.put(JFlexElementTypes.OPTION_PARAMETER, JFlexHighlighterColors.OPTION_PARAMETER);
        colors.put(JFlexElementTypes.OPTION_SIGN, JFlexHighlighterColors.OPTION_SIGN);

        colors.put(JFlexElementTypes.SECTION_SIGN, JFlexHighlighterColors.SECTION_SIGN);
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
            return map.get(((JFlexElementType) tokenType).getParsedType());
        }
        return map.get(tokenType);
    }
}
