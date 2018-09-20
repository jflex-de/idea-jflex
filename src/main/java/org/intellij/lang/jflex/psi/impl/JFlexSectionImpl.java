package org.intellij.lang.jflex.psi.impl;

import com.intellij.lang.ASTNode;
import org.intellij.lang.jflex.psi.JFlexSection;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 19.03.2008
 * Time: 22:45:06
 */
public class JFlexSectionImpl extends JFlexElementImpl implements JFlexSection {

    public JFlexSectionImpl(@NotNull ASTNode node) {
        super(node);
    }

}
