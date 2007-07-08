package org.intellij.lang.jflex;

import com.intellij.lang.Language;
import com.intellij.lang.ParserDefinition;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.intellij.lang.jflex.fileTypes.JFlexSyntaxHighlighter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * JFlex language.
 *
 * @author Alexey Efimov
 */
public class JFlexLanguage extends Language {
    @NonNls
    private static final String ID = "JFlex";

    public JFlexLanguage() {
        super(ID);
    }

    @NotNull
    public SyntaxHighlighter getSyntaxHighlighter(Project project, VirtualFile virtualFile) {
        return new JFlexSyntaxHighlighter(project, virtualFile);
    }

    @Nullable
    public ParserDefinition getParserDefinition() {
        return new JFlexParserDefinition();
    }
}
