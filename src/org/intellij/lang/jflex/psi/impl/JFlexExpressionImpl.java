package org.intellij.lang.jflex.psi.impl;

import com.intellij.lang.ASTNode;
import org.intellij.lang.jflex.psi.JFlexExpression;
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
