package org.intellij.lang.jflex.injection;

import com.intellij.lang.StdLanguages;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import org.intellij.lang.jflex.JFlexElementTypes;
import org.intellij.lang.jflex.psi.JFlexElement;
import org.intellij.lang.jflex.psi.JFlexJavaCode;
import org.intellij.lang.jflex.psi.JFlexPsiFile;
import org.jetbrains.annotations.NotNull;

public class JFlexJavaInjector implements LanguageInjector {

    public static final String DEFCLASS = "Yylex";
    public static final String DEFMETHOD = "yylex";
    public static final String DEFTYPE = "int";

    public void getLanguagesToInject(@NotNull PsiLanguageInjectionHost host, @NotNull InjectedLanguagePlaces registrar) {

        if (host instanceof JFlexJavaCode) {

            PsiFile file = host.getContainingFile();
            assert file instanceof JFlexPsiFile;

            //todo: that should be done like file.getImportsSection()
            PsiElement importSection = file.getFirstChild();

            //that is processing imports and package section
            if (importSection == host) {
                registrar.addPlace(StdLanguages.JAVA, new TextRange(0, host.getTextLength()), null, "\npublic class a{}");
                return;
            }

            StringBuilder imports = new StringBuilder();

            //let's add some imports and package statements from flex file header
            if (importSection instanceof JFlexJavaCode) {
                imports.append(importSection.getText());
            }

            String classnamestr = DEFCLASS;
            String returntypestr = DEFTYPE;

            JFlexElement classname = ((JFlexPsiFile) file).getClassname();
            JFlexElement returntype = ((JFlexPsiFile) file).getReturnType();

            if (classname != null) {
                classnamestr = classname.getText();
            }
            if (returntype != null) {
                returntypestr = returntype.getText();
            }

            imports.append(" public class ").append(classnamestr).append("{");

            StringBuilder suffix = new StringBuilder();

            //todo: that should be done the other way.
            if (host.getPrevSibling().getNode().getElementType() == JFlexElementTypes.OPTION_LEFT_BRACE) {
                suffix.append("}");
            } else {
                imports.append("public ").append(returntypestr).append(" yylex(){");
                suffix.append("}}");
            }

            registrar.addPlace(StdLanguages.JAVA, new TextRange(0, host.getTextLength()), imports.toString(), suffix.toString());

        }

    }
}