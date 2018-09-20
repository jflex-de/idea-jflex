package de.jflex.ide.idea.psi;

import com.intellij.psi.PsiLanguageInjectionHost;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 15.03.2008
 * Time: 18:52:38
 */
public interface JFlexJavaCode extends JFlexElement, PsiLanguageInjectionHost {

    public boolean isMatchAction();

}
