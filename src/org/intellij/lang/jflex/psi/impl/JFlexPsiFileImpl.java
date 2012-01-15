package org.intellij.lang.jflex.psi.impl;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.tree.TokenSet;
import org.intellij.lang.jflex.JFlexElementTypes;
import org.intellij.lang.jflex.JFlexLanguage;
import org.intellij.lang.jflex.fileTypes.JFlexFileType;
import org.intellij.lang.jflex.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * JFlex PSI file.
 *
 * @author Alexey Efimov
 */
public class JFlexPsiFileImpl extends PsiFileBase implements JFlexPsiFile {

    public static final TokenSet MACROSET = TokenSet.create(JFlexElementTypes.MACRO_DEFINITION);
    public static final TokenSet STATESTATEMENTSET = TokenSet.create(JFlexElementTypes.STATE_STATEMENT);

    public JFlexPsiFileImpl(FileViewProvider viewProvider) {
        super(viewProvider, JFlexLanguage.LANGUAGE);
    }

    @Nullable
    public JFlexElement getClassname() {
        JFlexExpression classexp = null;
        ASTNode classnode = getNode().findChildByType(JFlexElementTypes.CLASS_STATEMENT);
        if (classnode != null) {
            classexp = ((JFlexClassStatement) classnode.getPsi()).getValue();
        }
        return classexp;
    }

    @Nullable
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

    @Nullable
    public JFlexJavaCode getImports() {
        return getNode().getFirstChildNode().getElementType() == JFlexElementTypes.JAVA_CODE ? (JFlexJavaCode) getNode().getFirstChildNode().getPsi() : null;
    }

    public JFlexStateStatement[] getStateStatements() {
        ASTNode[] statestatements = getNode().getChildren(STATESTATEMENTSET);
        JFlexStateStatement[] result = new JFlexStateStatement[statestatements.length];
        int i = 0;
        for (ASTNode node : statestatements) {
            result[i++] = (JFlexStateStatement) node.getPsi();
        }
        return result;
    }

    @NotNull
    public FileType getFileType() {
        return JFlexFileType.FILE_TYPE;
    }

    public String toString() {
        return "JFlex: " + getName();
    }
}
