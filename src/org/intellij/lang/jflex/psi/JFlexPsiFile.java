package org.intellij.lang.jflex.psi;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 17.03.2008
 * Time: 2:18:18
 */
public interface JFlexPsiFile extends JFlexElement {

    JFlexElement getClassname();

    JFlexElement getReturnType();

    JFlexElement[] getImplementedInterfaces();


}
