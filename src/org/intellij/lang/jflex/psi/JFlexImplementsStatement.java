package org.intellij.lang.jflex.psi;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 18.03.2008
 * Time: 22:53:00
 */

/**
 * %implements "interface 1"[, "interface 2", ..]
 */
public interface JFlexImplementsStatement extends JFlexElement {

    public JFlexExpression[] getInterfaces();

}
