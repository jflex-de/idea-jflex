package de.jflex.ide.idea.psi.impl;

import com.intellij.lang.ASTNode;
import de.jflex.ide.idea.psi.JFlexExpression;
import org.jetbrains.annotations.NotNull;

/**
 * JFlex expression implmentation.
 *
 * @author Alexey Efimov
 */
public class JFlexExpressionImpl extends JFlexElementImpl implements JFlexExpression {

    public JFlexExpressionImpl(@NotNull ASTNode node) {
        super(node);
    }

}
