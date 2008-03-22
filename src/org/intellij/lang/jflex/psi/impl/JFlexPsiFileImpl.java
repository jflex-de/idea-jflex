package org.intellij.lang.jflex.psi.impl;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.tree.TokenSet;
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

    public static final TokenSet MACROSET = TokenSet.create(JFlexElementTypes.MACRO_DEFINITION);

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
        ASTNode returnnode = getNode().findChildByType(JFlexElementTypes.TYPE_STATEMENT);
        if (returnnode != null) {
            classexp = ((JFlexTypeStatement) returnnode.getPsi()).getValue();
        }
        return classexp;
    }

    public JFlexExpression[] getImplementedInterfaces() {
        JFlexExpression[] result = new JFlexExpression[0];
        ASTNode implmentsnode = getNode().findChildByType(JFlexElementTypes.IMPLEMENTS_STATEMENT);
        if (implmentsnode != null) {
            result = ((JFlexImplementsStatement) implmentsnode.getPsi()).getInterfaces();
        }
        return result;
    }

    public JFlexMacroDefinition[] getDeclaredMacroses() {
        ASTNode[] macroses = getNode().getChildren(MACROSET);
        JFlexMacroDefinition[] result = new JFlexMacroDefinition[macroses.length];
        int i = 0;
        for (ASTNode node : macroses) {
            result[i++] = (JFlexMacroDefinition) node.getPsi();
        }
        return result;
    }

    @NotNull
    public FileType getFileType() {
        return JFlexFileTypeManager.getInstance().getFileType();
    }

    public String toString() {
        return "JFlex: " + getName();
    }
}
