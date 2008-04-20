package org.intellij.lang.jflex.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.intellij.lang.jflex.JFlexElementTypes;
import org.intellij.lang.jflex.psi.JFlexMacroDefinition;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 20.03.2008
 * Time: 22:45:46
 */
public class JFlexMacroDefinitionImpl extends JFlexElementImpl implements JFlexMacroDefinition {

    public JFlexMacroDefinitionImpl(@NotNull ASTNode node) {
        super(node);
    }

    public ASTNode findNameElement() {
        return getNode().findChildByType(JFlexElementTypes.MACROS);
    }

    public PsiElement getNameElement() {
        ASTNode node = findNameElement();
        return node != null ? node.getPsi() : null;
    }

    public String getName() {
        ASTNode node = findNameElement();
        return node != null ? node.getText() : null;
    }

    public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
        throw new IncorrectOperationException();
    }

}
