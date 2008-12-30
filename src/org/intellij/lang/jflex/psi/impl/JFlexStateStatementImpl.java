package org.intellij.lang.jflex.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import org.intellij.lang.jflex.JFlexElementTypes;
import org.intellij.lang.jflex.psi.JFlexStateDefinition;
import org.intellij.lang.jflex.psi.JFlexStateStatement;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 24.03.2008
 * Time: 23:44:01
 */
public class JFlexStateStatementImpl extends JFlexElementImpl implements JFlexStateStatement {

    public static final TokenSet STATEDEF = TokenSet.create(JFlexElementTypes.STATE_DEFINITION);

    public JFlexStateStatementImpl(@NotNull ASTNode node) {
        super(node);
    }

    public JFlexStateDefinition[] getStateDefinitions() {
        ASTNode[] states = getNode().getChildren(STATEDEF);
        JFlexStateDefinition[] result = new JFlexStateDefinition[states.length];
        for (int i = 0; i < states.length; i++) {
            result[i] = (JFlexStateDefinition) states[i].getPsi();
        }
        return result;
    }

}
