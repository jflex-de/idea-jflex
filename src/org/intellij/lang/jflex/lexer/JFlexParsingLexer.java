package org.intellij.lang.jflex.lexer;

import com.intellij.lexer.FlexAdapter;
import com.intellij.openapi.project.Project;

/**
 * Lexer adapter.
 *
 * @author Alexey Efimov
 */
public class JFlexParsingLexer extends FlexAdapter {
    public JFlexParsingLexer(Project project) {
        super(new _JFlexLexer((java.io.Reader) null));
    }
}