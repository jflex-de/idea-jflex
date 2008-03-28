package org.intellij.lang.jflex;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.documentation.DocumentationProvider;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import org.intellij.lang.jflex.fileTypes.JFlexSyntaxHighlighter;
import org.intellij.lang.jflex.psi.JFlexMacroDefinition;
import org.intellij.lang.jflex.validation.JFlexAnnotatingVisitor;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * JFlex language.
 *
 * @author Alexey Efimov
 */
public class JFlexLanguage extends Language {
    @NonNls
    private static final String ID = "JFlex";

    private static final Annotator ANNOTATOR = new JFlexAnnotatingVisitor();

    private static ParserDefinition pd;

    private DocumentationProvider prov;

    public JFlexLanguage() {
        super(ID);
    }

    @NotNull
    public SyntaxHighlighter getSyntaxHighlighter(Project project, VirtualFile virtualFile) {
        return new JFlexSyntaxHighlighter(project, virtualFile);
    }

    @Nullable
    public ParserDefinition getParserDefinition() {
        if (pd == null) {
            pd = new JFlexParserDefinition();
        }
        return pd;
    }

    @Nullable
    public Annotator getAnnotator() {
        return ANNOTATOR;
    }

    @Nullable
    public DocumentationProvider getDocumentationProvider() {

        if (prov == null) {
            prov = new DocumentationProvider() {
                @Nullable
                public String getQuickNavigateInfo(PsiElement element) {
                    return null;
                }

                @Nullable
                public String getUrlFor(PsiElement element, PsiElement originalElement) {
                    return null;
                }

                @Nullable
                public String generateDoc(PsiElement element, PsiElement originalElement) {
                    if (element instanceof JFlexMacroDefinition) {
                        ASTNode regexp = element.getNode().findChildByType(JFlexElementTypes.REGEXP);
                        return regexp != null ? regexp.getText() : "No regexp found for macro";
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
            };
        }


        return prov;

    }
}
