package org.intellij.lang.jflex.psi;

import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReferenceExpression;

/**
 * Element visitor.
 *
 * @author Alexey Efimov
 */
public class JFlexElementVisitor extends PsiElementVisitor {

    public void visitReferenceExpression(PsiReferenceExpression expression) {
        visitExpression(expression);
    }

}
