package org.intellij.lang.jflex;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import org.intellij.lang.jflex.lexer.JFlexMergingLexer;
import org.intellij.lang.jflex.psi.JFlexMacroDefinition;
import org.intellij.lang.jflex.psi.JFlexStateDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 01.04.2008
 * Time: 23:31:29
 */
public class JFlexFindUsagesProvider implements FindUsagesProvider {

    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return psiElement instanceof PsiNamedElement;
    }

    @NotNull
    public String getDescriptiveName(@NotNull PsiElement element) {
        String name = ((PsiNamedElement) element).getName();
        return name != null ? name : "";
    }

    @Nullable
    public String getHelpId(@NotNull PsiElement psiElement) {
        return null;
    }

    @NotNull
    public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
        return getDescriptiveName(element);
    }

    @NotNull
    public String getType(@NotNull PsiElement element) {
        if (element instanceof JFlexStateDefinition) return "State";
        if (element instanceof JFlexMacroDefinition) return "Macro";
        return "";
    }

    @Nullable
    public WordsScanner getWordsScanner() {
        return new DefaultWordsScanner(new JFlexMergingLexer(), JFlexElementTypes.IDENTIFIERS, JFlexElementTypes.COMMENTS, JFlexElementTypes.REGEXP_SCOPE);
    }
}
