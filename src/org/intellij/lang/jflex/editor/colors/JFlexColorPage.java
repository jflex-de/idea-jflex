package org.intellij.lang.jflex.editor.colors;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.lang.Language;
import org.intellij.lang.jflex.editor.JFlexHighlighterColors;
import org.intellij.lang.jflex.fileTypes.JFlexFileTypeManager;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

final class JFlexColorPage implements ColorSettingsPage {
    private static final AttributesDescriptor[] EMPTY_ATTRIBUTES_DESCRIPTOR_ARRAY = new AttributesDescriptor[]{};
    private static final ColorDescriptor[] EMPTY_COLOR_DESCRIPTOR_ARRAY = new ColorDescriptor[]{};

    private final Set<AttributesDescriptor> attributeDescriptors = new HashSet<AttributesDescriptor>();

    public JFlexColorPage() {
        attributeDescriptors.add(new AttributesDescriptor("Angle brackets", JFlexHighlighterColors.ANGLE_BRACKETS));
        attributeDescriptors.add(new AttributesDescriptor("Brackets", JFlexHighlighterColors.BRACKETS));
        attributeDescriptors.add(new AttributesDescriptor("Comma", JFlexHighlighterColors.COMMA));
        attributeDescriptors.add(new AttributesDescriptor("Braces", JFlexHighlighterColors.BRACES));
        attributeDescriptors.add(new AttributesDescriptor("Parenths", JFlexHighlighterColors.PARENTHS));
        attributeDescriptors.add(new AttributesDescriptor("Comment", JFlexHighlighterColors.COMMENT));
        attributeDescriptors.add(new AttributesDescriptor("Java code", JFlexHighlighterColors.JAVA_CODE));
        attributeDescriptors.add(new AttributesDescriptor("Macros", JFlexHighlighterColors.MACROS));
        attributeDescriptors.add(new AttributesDescriptor("Macros reference", JFlexHighlighterColors.MACROS_REF));
        attributeDescriptors.add(new AttributesDescriptor("Operator", JFlexHighlighterColors.OPERATION_SIGN));
        attributeDescriptors.add(new AttributesDescriptor("Option", JFlexHighlighterColors.OPTION_BACKGROUND));
        attributeDescriptors.add(new AttributesDescriptor("Option keyword", JFlexHighlighterColors.OPTION_KEYWORD));
        attributeDescriptors.add(new AttributesDescriptor("Option parameter", JFlexHighlighterColors.OPTION_PARAMETER));
        attributeDescriptors.add(new AttributesDescriptor("Option sign", JFlexHighlighterColors.OPTION_SIGN));
        attributeDescriptors.add(new AttributesDescriptor("RegExp", JFlexHighlighterColors.REGEXP_BACKGROUND));
        attributeDescriptors.add(new AttributesDescriptor("RegExp class symbol", JFlexHighlighterColors.REGEXP_CLASS_SYMBOL));
        attributeDescriptors.add(new AttributesDescriptor("RegExp symbol", JFlexHighlighterColors.REGEXP_SYMBOL));
        attributeDescriptors.add(new AttributesDescriptor("Section sign", JFlexHighlighterColors.SECTION_SIGN));
        attributeDescriptors.add(new AttributesDescriptor("State reference", JFlexHighlighterColors.STATE_REF));
        attributeDescriptors.add(new AttributesDescriptor("String", JFlexHighlighterColors.STRING));
    }

    @Nullable
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @NotNull
    public AttributesDescriptor[] getAttributeDescriptors() {
        return attributeDescriptors.toArray(EMPTY_ATTRIBUTES_DESCRIPTOR_ARRAY);
    }

    @NotNull
    public ColorDescriptor[] getColorDescriptors() {
        return EMPTY_COLOR_DESCRIPTOR_ARRAY;
    }

    @NonNls
    @NotNull
    public String getDemoText() {
        return "package org.intellij.lang.jflex;\n" +
                "%%\n" +
                "%class Lexer\n" +
                "/* Macros */\n" +
                "LineTerminator = \\r|\\n|\\r\\n\n" +
                "WhiteSpace = ({LineTerminator} | [ \\t\\f])+\n" +
                "// States\n" +
                "%state SAMPLE\n" +
                "%%\n" +
                "<SAMPLE, YYINITIAL> {\n" +
                "  // Rule\n" +
                "  \"foo\" { foo(); }\n" +
                "}";
    }

    @NotNull
    public String getDisplayName() {
        return "JFlex";
    }

    @NotNull
    public SyntaxHighlighter getHighlighter() {
        Language jflexLanguage = JFlexFileTypeManager.getInstance().getFileType().getLanguage();
        return SyntaxHighlighterFactory.getSyntaxHighlighter(jflexLanguage, null, null);
    }

    @Nullable
    public Icon getIcon() {
        return JFlexFileTypeManager.getInstance().getFileType().getIcon();
    }

}
