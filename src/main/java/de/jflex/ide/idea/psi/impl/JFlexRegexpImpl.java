package de.jflex.ide.idea.psi.impl;

import com.intellij.lang.ASTNode;
import de.jflex.ide.idea.psi.JFlexRegexp;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 22.03.2008
 * Time: 23:18:54
 */
public class JFlexRegexpImpl extends JFlexElementImpl implements JFlexRegexp {

    public JFlexRegexpImpl(@NotNull ASTNode node) {
        super(node);
    }

}
