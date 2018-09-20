package org.intellij.lang.jflex.psi.impl;

import com.intellij.lang.ASTNode;
import org.intellij.lang.jflex.psi.JFlexRegexp;
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
