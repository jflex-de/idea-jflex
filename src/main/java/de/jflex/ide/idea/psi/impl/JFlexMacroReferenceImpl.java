package de.jflex.ide.idea.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.Query;
import de.jflex.ide.idea.psi.JFlexMacroDefinition;
import de.jflex.ide.idea.psi.JFlexMacroReference;
import de.jflex.ide.idea.psi.JFlexPsiFile;
import de.jflex.ide.idea.validation.JFlexAnnotatingVisitor;
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
        //Is it the correct way?
        JFlexPsiFile file = (JFlexPsiFile) this.getContainingFile();
        JFlexMacroDefinition[] macroses = file.getDeclaredMacroses();
        for (JFlexMacroDefinition m : macroses) {
            if (getText().equals(m.getName())) {
                return m;
            }
        }
        return null;
    }

    public int getTextOffset() {
        return super.getTextOffset();
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
        return element instanceof JFlexMacroDefinition && ((JFlexMacroDefinition) element).getName().equals(getText());
    }

    public Object[] getVariants() {
        Query query = ReferencesSearch.search(this);
        return query.findAll().toArray();
    }

    public boolean isSoft() {
        return false;
    }
}
