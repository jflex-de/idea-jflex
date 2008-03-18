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

            StringBuilder prefix = new StringBuilder();

            //let's add some imports and package statements from flex file header
            if (importSection instanceof JFlexJavaCode) {
                prefix.append(importSection.getText());
            }

            JFlexPsiFile jfpf = ((JFlexPsiFile) file);

            String classnamestr = DEFCLASS;
            JFlexElement classname = jfpf.getClassname();
            if (classname != null) {
                classnamestr = classname.getText();
            }

            String returntypestr = DEFTYPE;
            JFlexElement returntype = jfpf.getReturnType();
            if (returntype != null) {
                returntypestr = returntype.getText();
            }

            StringBuilder implementedstr = new StringBuilder();
            JFlexElement[] implemented = jfpf.getImplementedInterfaces();
            //what a lousy piece of code.
            if (implemented.length > 0) {
                implementedstr.append(" implements ");
                for (int i = 0; i < implemented.length; i++) {
                    JFlexElement jFlexElement = implemented[i];
                    implementedstr.append(jFlexElement.getText());
                    if (i < implemented.length - 1) {
                        implementedstr.append(",");
                    }
                }
            }

            prefix.append("\npublic class ").append(classnamestr).append(implementedstr.toString()).append("{");

            StringBuilder suffix = new StringBuilder();

            //todo: that should be done the other way.
            if (host.getPrevSibling().getNode().getElementType() == JFlexElementTypes.OPTION_LEFT_BRACE) {
                suffix.append("}");
            } else {
                prefix.append("public ").append(returntypestr).append(" yylex(){");
                suffix.append("}}");
            }

            registrar.addPlace(StdLanguages.JAVA, new TextRange(0, host.getTextLength()), prefix.toString(), suffix.toString());

        }

    }
}