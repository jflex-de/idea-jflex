package de.jflex.ide.idea.validation;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReferenceExpression;
import de.jflex.ide.idea.psi.JFlexMacroReference;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 22.03.2008
 * Time: 17:19:55
 */
public class JFlexAnnotatingVisitor extends PsiElementVisitor implements Annotator {

    public void annotate(PsiElement psiElement, AnnotationHolder holder) {
        psiElement.accept(this);
    }

    public void visitReferenceExpression(PsiReferenceExpression expression) {
    }

    public void visitMacroReference(JFlexMacroReference expression) {

    }

}
