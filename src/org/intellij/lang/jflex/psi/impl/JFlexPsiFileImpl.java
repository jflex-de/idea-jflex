package org.intellij.lang.jflex.psi.impl;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.intellij.lang.jflex.JFlexElementTypes;
import org.intellij.lang.jflex.fileTypes.JFlexFileTypeManager;
import org.intellij.lang.jflex.psi.*;
import org.jetbrains.annotations.NotNull;

/**
 * JFlex PSI file.
 *
 * @author Alexey Efimov
 */
public class JFlexPsiFileImpl extends PsiFileBase implements JFlexPsiFile {

    public JFlexPsiFileImpl(FileViewProvider viewProvider) {
        super(viewProvider, JFlexFileTypeManager.getInstance().getFileType().getLanguage());
    }

    public JFlexElement getClassname() {
        JFlexExpression classexp = null;
        ASTNode classnode = getNode().findChildByType(JFlexElementTypes.CLASS_STATEMENT);
        if (classnode != null) {
            classexp = ((JFlexClassStatement) classnode.getPsi()).getValue();
        }
        return classexp;
    }

    public JFlexElement getReturnType() {
        JFlexExpression classexp = null;
        ASTNode classnode = getNode().findChildByType(JFlexElementTypes.TYPE_STATEMENT);
        if (classnode != null) {
            classexp = ((JFlexTypeStatement) classnode.getPsi()).getValue();
        }
        return classexp;
    }

    @NotNull
    public FileType getFileType() {
        return JFlexFileTypeManager.getInstance().getFileType();
    }

    public String toString() {
        return "JFlex: " + getName();
    }
}
