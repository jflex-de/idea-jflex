package de.jflex.ide.idea.psi.impl;

import com.intellij.lang.ASTNode;
import de.jflex.ide.idea.JFlexElementTypes;
import de.jflex.ide.idea.psi.JFlexExpression;
import de.jflex.ide.idea.psi.JFlexImplementsStatement;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 18.03.2008
 * Time: 23:53:42
 */
public class JFlexImplementsStatementImpl extends JFlexElementImpl implements
    JFlexImplementsStatement {

    public JFlexImplementsStatementImpl(@NotNull ASTNode node) {
        super(node);
    }

    public JFlexExpression[] getInterfaces() {
        ASTNode[] nodes = getNode().getChildren(JFlexElementTypes.EXPRESSIONS);
        JFlexExpression[] result = new JFlexExpression[nodes.length];
        int i = 0;
        for (ASTNode node : nodes) {
            result[i++] = (JFlexExpression) node.getPsi();
        }
        return result;
    }

}
