package org.intellij.lang.jflex.psi;

import org.jetbrains.annotations.Nullable;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 17.03.2008
 * Time: 2:18:18
 */
public interface JFlexPsiFile extends JFlexElement {

    @Nullable
    JFlexElement getClassname();

    @Nullable
    JFlexElement getReturnType();

    JFlexExpression[] getImplementedInterfaces();

    JFlexMacroDefinition[] getDeclaredMacroses();

    JFlexStateStatement[] getStateStatements();

    @Nullable
    JFlexJavaCode getImports();


}
