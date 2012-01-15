package org.intellij.lang.jflex.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import org.intellij.lang.jflex.JFlexLanguage;
import org.intellij.lang.jflex.psi.JFlexElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * JFlex base element.
 *
 * @author Alexey Efimov
 */
public class JFlexElementImpl extends ASTWrapperPsiElement implements JFlexElement {

    @NonNls
    private static final String IMPL = "Impl";

    public JFlexElementImpl(@NotNull ASTNode node) {
        super(node);
    }

    @NotNull
    public Language getLanguage() {
        return JFlexLanguage.LANGUAGE;
    }

    public String toString() {
        String classname = getClass().getName();
        if (classname.endsWith(IMPL)) {
            classname = classname.substring(0, classname.length() - IMPL.length());
        }

        classname = classname.substring(classname.lastIndexOf(".") + 1);
        return classname;
    }
}
