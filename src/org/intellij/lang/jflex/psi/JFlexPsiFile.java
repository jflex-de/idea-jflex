package org.intellij.lang.jflex.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.intellij.lang.jflex.fileTypes.JFlexFileTypeManager;
import org.jetbrains.annotations.NotNull;

/**
 * JFlex PSI file.
 *
 * @author Alexey Efimov
 */
public class JFlexPsiFile extends PsiFileBase implements JFlexElement {
    public JFlexPsiFile(FileViewProvider viewProvider) {
        super(viewProvider, JFlexFileTypeManager.getInstance().getFileType().getLanguage());
    }

    @NotNull
    public FileType getFileType() {
        return JFlexFileTypeManager.getInstance().getFileType();
    }

    public String toString() {
        return "JFlex: " + getName();
    }
}
