package org.intellij.lang.jflex.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import org.intellij.lang.jflex.JFlexElementTypes;
import org.intellij.lang.jflex.psi.JFlexExpression;
import org.intellij.lang.jflex.psi.JFlexImplementsStatement;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 18.03.2008
 * Time: 23:53:42
 */
public class JFlexImplementsStatementImpl extends JFlexElementImpl implements JFlexImplementsStatement {

    public static final TokenSet OPTS = TokenSet.create(JFlexElementTypes.OPTION_PARAMETER);

    public JFlexImplementsStatementImpl(@NotNull ASTNode node) {
        super(node);
    }

    public JFlexExpression[] getInterfaces() {
        ASTNode[] nodes = getNode().getChildren(OPTS);
        JFlexExpression[] result = new JFlexExpression[nodes.length];
        int i = 0;
        for (ASTNode node : nodes) {
            result[i++] = (JFlexExpression) node.getPsi();
        }
        return result;
    }


}
