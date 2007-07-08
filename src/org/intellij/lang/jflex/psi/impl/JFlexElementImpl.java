package org.intellij.lang.jflex.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.psi.PsiElementVisitor;
import org.intellij.lang.jflex.fileTypes.JFlexFileTypeManager;
import org.intellij.lang.jflex.psi.JFlexElement;
import org.intellij.lang.jflex.psi.JFlexElementVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * JFlex base element.
 *
 * @author Alexey Efimov
 */
public class JFlexElementImpl extends ASTWrapperPsiElement implements JFlexElement {
    public JFlexElementImpl(@NotNull ASTNode node) {
        super(node);
    }

    @NotNull
    public Language getLanguage() {
        return JFlexFileTypeManager.getInstance().getFileType().getLanguage();
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof JFlexElementVisitor) {
            ((JFlexElementVisitor) visitor).visitJFlexElement(this);
        } else {
            super.accept(visitor);
        }
    }
}
