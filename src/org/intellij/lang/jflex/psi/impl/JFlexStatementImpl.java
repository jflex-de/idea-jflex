package org.intellij.lang.jflex.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.intellij.lang.jflex.psi.JFlexElementVisitor;
import org.intellij.lang.jflex.psi.JFlexStatement;
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

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof JFlexElementVisitor) {
            ((JFlexElementVisitor) visitor).visitJFlexStatement(this);
        } else {
            super.accept(visitor);
        }
    }
}
