package org.intellij.lang.jflex.psi;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 24.03.2008
 * Time: 23:43:18
 */
public interface JFlexStateStatement extends JFlexElement {

    public JFlexStateDefinition[] getStateDefinitions();

}
