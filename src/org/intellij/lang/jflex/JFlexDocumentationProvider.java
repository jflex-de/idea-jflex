package org.intellij.lang.jflex;

import com.intellij.lang.ASTNode;
import com.intellij.lang.documentation.DocumentationProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import org.intellij.lang.jflex.psi.JFlexMacroDefinition;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 01.04.2008
 * Time: 23:25:17
 */
public class JFlexDocumentationProvider implements DocumentationProvider {

    @Nullable
    public String getQuickNavigateInfo(PsiElement element) {
        return null;
    }

    @Nullable
    public List<String> getUrlFor(PsiElement element, PsiElement originalElement) {
        return null;
    }

    @Nullable
    public String generateDoc(PsiElement element, PsiElement originalElement) {
        if (element instanceof JFlexMacroDefinition) {
            ASTNode astNode = element.getNode();
            ASTNode regexp = astNode != null ? astNode.findChildByType(JFlexElementTypes.REGEXP) : null;
            return regexp != null ? regexp.getText() : "No regexp found.";
        }
        return null;
    }

    @Nullable
    public PsiElement getDocumentationElementForLookupItem(PsiManager psiManager, Object object, PsiElement element) {
        return null;
    }

    @Nullable
    public PsiElement getDocumentationElementForLink(PsiManager psiManager, String link, PsiElement context) {
        return null;
    }

    @Nullable
    public String getQuickNavigateInfo(PsiElement element, PsiElement originalElement) {
        return null;
    }
}
