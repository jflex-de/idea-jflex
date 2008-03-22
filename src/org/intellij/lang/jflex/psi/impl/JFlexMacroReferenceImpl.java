package org.intellij.lang.jflex.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.Query;
import org.intellij.lang.jflex.psi.JFlexMacroDefinition;
import org.intellij.lang.jflex.psi.JFlexMacroReference;
import org.intellij.lang.jflex.psi.JFlexPsiFile;
import org.intellij.lang.jflex.validation.JFlexAnnotatingVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 19.03.2008
 * Time: 23:22:03
 */
public class JFlexMacroReferenceImpl extends JFlexElementImpl implements JFlexMacroReference {

    public JFlexMacroReferenceImpl(@NotNull ASTNode node) {
        super(node);
    }

    public PsiReference getReference() {
        return this;
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof JFlexAnnotatingVisitor) {
            ((JFlexAnnotatingVisitor) visitor).visitMacroReference(this);
        }
    }

    public PsiElement getElement() {
        return this;
    }

    public TextRange getRangeInElement() {
        return new TextRange(0, getTextLength());
    }

    @Nullable
    public PsiElement resolve() {
        //that's not right. but it wokrs :) todo: do the right way
        JFlexPsiFile file = (JFlexPsiFile) this.getContainingFile();
        PsiNamedElement[] macroses = file.getDeclaredMacroses();
        for (PsiNamedElement m : macroses) {
            if (getText().equals(m.getName())) {
                return m;
            }
        }
        return null;
    }

    public String getCanonicalText() {
        return getText();
    }

    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        throw new IncorrectOperationException();
    }

    public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
        throw new IncorrectOperationException();
    }

    public boolean isReferenceTo(PsiElement element) {
        return element instanceof JFlexMacroDefinition && element.getText().equals(getText());
    }

    public Object[] getVariants() {
        Query query = ReferencesSearch.search(this);
        return query.findAll().toArray();
    }

    public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull PsiSubstitutor substitutor, PsiElement lastParent, @NotNull PsiElement place) {
        return super.processDeclarations(processor, substitutor, lastParent, place);
    }

    public boolean isSoft() {
        return false;
    }
}
