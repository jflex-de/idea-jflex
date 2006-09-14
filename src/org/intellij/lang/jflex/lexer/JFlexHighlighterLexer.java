package org.intellij.lang.jflex.lexer;

import com.intellij.lexer.FlexAdapter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * Lexer adapter.
 *
 * @author Alexey Efimov
 */
public class JFlexHighlighterLexer extends FlexAdapter {
    public JFlexHighlighterLexer(Project project, VirtualFile virtualFile) {
        super(new _JFlexLexer((java.io.Reader)null));
    }
}
