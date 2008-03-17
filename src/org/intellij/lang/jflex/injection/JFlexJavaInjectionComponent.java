package org.intellij.lang.jflex.injection;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.psi.LanguageInjector;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 15.03.2008
 * Time: 19:10:00
 */
public class JFlexJavaInjectionComponent implements ProjectComponent {

    PsiManager psiman;

    LanguageInjector inj = new JFlexJavaInjector();

    public JFlexJavaInjectionComponent(PsiManager psiman) {
        this.psiman = psiman;
    }

    public void projectOpened() {
        psiman.registerLanguageInjector(inj);
    }

    public void projectClosed() {
        psiman.unregisterLanguageInjector(inj);
    }

    @NonNls
    @NotNull
    public String getComponentName() {
        return "JFlex.JavaInjection";
    }

    public void initComponent() {

    }

    public void disposeComponent() {

    }


}
