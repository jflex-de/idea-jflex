package de.jflex.ide.idea;

import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.SingleLazyInstanceSyntaxHighlighterFactory;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import de.jflex.ide.idea.fileTypes.JFlexSyntaxHighlighter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * JFlex language.
 *
 * @author Alexey Efimov
 */
public class JFlexLanguage extends Language {
    @NonNls
    public static final String ID = "JFlex";

    public static final JFlexLanguage LANGUAGE = new JFlexLanguage();

    public JFlexLanguage() {
        super(ID);

        //somehow lang.syntaxHighlighterFactory extension won't work for me
        SyntaxHighlighterFactory.LANGUAGE_FACTORY.addExplicitExtension(this, new SingleLazyInstanceSyntaxHighlighterFactory() {
            @NotNull
            protected SyntaxHighlighter createHighlighter() {
                return new JFlexSyntaxHighlighter();
            }
        });
    }

}
