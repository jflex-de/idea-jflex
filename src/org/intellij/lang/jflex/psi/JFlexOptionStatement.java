package org.intellij.lang.jflex.psi;

import org.jetbrains.annotations.Nullable;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 17.03.2008
 * Time: 23:23:12
 */

/**
 * "Class options and user class code" section statement
 */
public interface JFlexOptionStatement extends JFlexStatement {

    @Nullable
    public JFlexExpression getValue();

}
