package de.jflex.ide.idea.psi.impl;

import com.intellij.lang.ASTNode;
import de.jflex.ide.idea.JFlexElementTypes;
import de.jflex.ide.idea.psi.JFlexExpression;
import de.jflex.ide.idea.psi.JFlexOptionStatement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 17.03.2008
 * Time: 23:21:47
 */
public abstract class JFlexOptionStatementBase extends JFlexElementImpl implements
    JFlexOptionStatement {

    public JFlexOptionStatementBase(@NotNull ASTNode node) {
        super(node);
    }

    @Nullable
    public JFlexExpression getValue() {
        final ASTNode node = getNode().findChildByType(JFlexElementTypes.EXPRESSIONS);
        return (JFlexExpression) (node != null ? node.getPsi() : null);
    }

}
