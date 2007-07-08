package org.intellij.lang.jflex.compiler;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.compiler.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.intellij.lang.jflex.JFlex;
import org.intellij.lang.jflex.JFlexElementTypes;
import org.intellij.lang.jflex.fileTypes.JFlexFileTypeManager;
import org.intellij.lang.jflex.psi.JFlexClassStatement;
import org.intellij.lang.jflex.psi.JFlexExpression;
import org.intellij.lang.jflex.psi.JFlexPsiFile;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The source generating compiler for *.flex files.
 *
 * @author Alexey Efimov
 */
public class JFlexSourceGeneratingCompiler implements SourceGeneratingCompiler {
    public GenerationItem[] getGenerationItems(CompileContext context) {
        CompileScope compileScope = context.getCompileScope();
        VirtualFile[] files = compileScope.getFiles(JFlexFileTypeManager.getInstance().getFileType(), true);

        GenerationItem[] items = new GenerationItem[files.length];
        for (int i = 0; i < files.length; i++) {
            VirtualFile file = files[i];
            items[i] = new JFlexGenerationItem(context.getModuleByFile(file), file);
        }
        return items;
    }

    public GenerationItem[] generate(CompileContext context, GenerationItem[] items, VirtualFile outputRootDirectory) {
        List<GenerationItem> results = new ArrayList<GenerationItem>(items.length);
        for (GenerationItem item : items) {
            if (item instanceof JFlexGenerationItem) {
                JFlexGenerationItem flexItem = (JFlexGenerationItem) item;
                VirtualFile file = flexItem.getFile();
                if (file != null && file.isValid()) {
                    try {
                        JFlex.exec(file);
                    } catch (IOException e) {
                        context.addMessage(CompilerMessageCategory.ERROR, e.getMessage(), file.getPath(), -1, -1);
                    }
                }
            }
        }
        return results.toArray(items);
    }

    @NotNull
    public String getDescription() {
        return "jflex";
    }

    public boolean validateConfiguration(CompileScope scope) {
        return true;
    }

    public ValidityState createValidityState(DataInputStream is) throws IOException {
        return null;
    }

    private static class JFlexGenerationItem implements GenerationItem {
        private final Module module;
        private final VirtualFile file;
        private final String path;

        public JFlexGenerationItem(Module module, VirtualFile file) {
            this.file = file;
            this.module = module;
            String generationName = null;
            PsiFile psiFile = PsiManager.getInstance(module.getProject()).findFile(file);
            if (psiFile instanceof JFlexPsiFile) {
                JFlexPsiFile flexPsiFile = (JFlexPsiFile) psiFile;
                ASTNode node = flexPsiFile.getNode();
                if (node != null) {
                    ASTNode classStatement = node.findChildByType(JFlexElementTypes.CLASS_STATEMENT);
                    if (classStatement != null) {
                        JFlexExpression generateName = ((JFlexClassStatement) classStatement).getExpression();
                        if (generateName != null) {
                            generationName = generateName.getText();
                        }
                    }
                }
            }
            VirtualFile parent = file.getParent();
            this.path = generationName != null && parent != null ? parent.getPath() + "/" + generationName : null;
        }

        public VirtualFile getFile() {
            return file;
        }

        public String getPath() {
            return path;
        }

        public ValidityState getValidityState() {
            return new ValidityState() {
                public boolean equalsTo(ValidityState otherState) {
                    return false;
                }

                public void save(DataOutputStream os) throws IOException {

                }
            };
        }

        public Module getModule() {
            return module;
        }
    }
}
