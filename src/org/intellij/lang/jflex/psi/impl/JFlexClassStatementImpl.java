package org.intellij.lang.jflex.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.intellij.lang.jflex.JFlexElementTypes;
import org.intellij.lang.jflex.psi.JFlexClassStatement;
import org.intellij.lang.jflex.psi.JFlexElementVisitor;
import org.intellij.lang.jflex.psi.JFlexExpression;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of JFlex class statement.
 *
 * @author Alexey Efimov
 */
public class JFlexClassStatementImpl extends JFlexStatementImpl implements JFlexClassStatement {
    public JFlexClassStatementImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof JFlexElementVisitor) {
            ((JFlexElementVisitor) visitor).visitJFlexClassStatement(this);
        } else {
            super.accept(visitor);
        }
    }

    public JFlexExpression getExpression() {
        final ASTNode node = getNode().findChildByType(JFlexElementTypes.EXPRESSIONS);
        return (JFlexExpression) (node != null ? node.getPsi() : null);
    }
}
