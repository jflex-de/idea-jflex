package de.jflex.ide.idea.psi.impl;

import com.intellij.lang.ASTNode;
import de.jflex.ide.idea.psi.JFlexTypeStatement;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 17.03.2008
 * Time: 23:19:57
 */
public class JFlexTypeStatementImpl extends JFlexOptionStatementBase implements JFlexTypeStatement {

    public JFlexTypeStatementImpl(@NotNull ASTNode node) {
        super(node);
    }

}
