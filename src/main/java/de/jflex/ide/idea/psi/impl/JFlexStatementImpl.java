package de.jflex.ide.idea.psi.impl;

import com.intellij.lang.ASTNode;
import de.jflex.ide.idea.psi.JFlexStatement;
import org.jetbrains.annotations.NotNull;

/**
 * JFlex statement implementation
 *
 * @author Alexey Efimov
 */
public class JFlexStatementImpl extends JFlexElementImpl implements JFlexStatement {
    public JFlexStatementImpl(@NotNull ASTNode node) {
        super(node);
    }

}
