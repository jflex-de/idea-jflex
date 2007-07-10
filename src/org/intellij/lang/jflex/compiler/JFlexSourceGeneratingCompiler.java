package org.intellij.lang.jflex.compiler;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.compiler.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.intellij.lang.jflex.JFlex;
import org.intellij.lang.jflex.JFlexElementTypes;
import org.intellij.lang.jflex.fileTypes.JFlexFileTypeManager;
import org.intellij.lang.jflex.psi.JFlexClassStatement;
import org.intellij.lang.jflex.psi.JFlexExpression;
import org.intellij.lang.jflex.psi.JFlexPsiFile;
import org.intellij.lang.jflex.util.JFlexBundle;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The source generating compiler for *.flex files.
 *
 * @author Alexey Efimov
 */
public class JFlexSourceGeneratingCompiler implements SourceGeneratingCompiler {
    private static final GenerationItem[] EMPTY_GENERATION_ITEM_ARRAY = new GenerationItem[]{};

    public GenerationItem[] getGenerationItems(final CompileContext context) {
        Module[] affectedModules = context.getCompileScope().getAffectedModules();
        if (affectedModules != null && affectedModules.length > 0) {
            Application application = ApplicationManager.getApplication();
            return application.runReadAction(new PrepareAction(context));
        }
        return EMPTY_GENERATION_ITEM_ARRAY;
    }

    public GenerationItem[] generate(final CompileContext context, final GenerationItem[] items, VirtualFile outputRootDirectory) {
        if (items != null && items.length > 0) {
            Application application = ApplicationManager.getApplication();
            return application.runReadAction(new Computable<GenerationItem[]>() {
                public GenerationItem[] compute() {
                    List<GenerationItem> results = new ArrayList<GenerationItem>(items.length);
                    for (GenerationItem item : items) {
                        if (item instanceof JFlexGenerationItem) {
                            JFlexGenerationItem flexItem = (JFlexGenerationItem) item;
                            VirtualFile file = flexItem.getFile();
                            if (file != null && file.isValid()) {
                                try {
                                    String[] strings = JFlex.exec(file);
                                    String err = strings.length > 1 ? strings[1] : "";
                                    if (StringUtil.isEmptyOrSpaces(err)) {
                                        LocalFileSystem.getInstance().refresh(false);
                                        File outFile = new File(VfsUtil.virtualToIoFile(file).getParentFile(), flexItem.getGeneratedClassName() + ".java");
                                        if (outFile.exists() && outFile.isFile()) {
                                            flexItem.setPath(outFile.getAbsolutePath());
                                            results.add(flexItem);
                                            String out = strings[0];
                                            if (!StringUtil.isEmptyOrSpaces(out)) {
                                                context.addMessage(CompilerMessageCategory.INFORMATION, out.trim(), file.getPath(), -1, -1);
                                            }
                                        } else {
                                            context.addMessage(CompilerMessageCategory.ERROR, JFlexBundle.message("file.not.generated"), file.getPath(), -1, -1);
                                        }
                                    } else {
                                        context.addMessage(CompilerMessageCategory.ERROR, err.trim(), file.getPath(), -1, -1);
                                    }
                                } catch (IOException e) {
                                    context.addMessage(CompilerMessageCategory.ERROR, e.getMessage(), file.getPath(), -1, -1);
                                }
                            }
                        }
                    }
                    return results.toArray(items);
                }
            });
        }
        return EMPTY_GENERATION_ITEM_ARRAY;
    }

    @NotNull
    public String getDescription() {
        return "jflex";
    }

    public boolean validateConfiguration(CompileScope scope) {
        Module[] affectedModules = scope.getAffectedModules();
        if (affectedModules != null && affectedModules.length > 0) {
            Project project = affectedModules[0].getProject();
            VirtualFile[] files = scope.getFiles(JFlexFileTypeManager.getInstance().getFileType(), false);
            if (files != null && files.length > 0) {
                return JFlex.validateConfiguration(project);
            }
        }
        return true;
    }

    public ValidityState createValidityState(DataInputStream is) throws IOException {
        return new TimestampValidityState(System.currentTimeMillis());
    }

    private static class JFlexGenerationItem implements GenerationItem {
        private final Module module;
        private final VirtualFile file;
        private final String generatedClassName;
        private String path;
        private final TimestampValidityState timestampValidityState;

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
                        JFlexClassStatement psiClassStatement = (JFlexClassStatement) classStatement.getPsi();
                        JFlexExpression generateName = psiClassStatement.getExpression();
                        if (generateName != null) {
                            generationName = generateName.getText();
                        }
                    }
                }
            }
            VirtualFile parent = file.getParent();
            this.generatedClassName = generationName;
            VirtualFile generated = VfsUtil.findRelativeFile(generatedClassName, file);
            if (generated != null && generated.isValid()) {
                this.path = generated.getPath();
                this.timestampValidityState = new TimestampValidityState(file.getTimeStamp());
            } else {
                this.path = generationName != null && parent != null ? parent.getPath().concat("/").concat(generationName) : null;
                this.timestampValidityState = new TimestampValidityState(file.getTimeStamp());
            }

        }

        public VirtualFile getFile() {
            return file;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }

        public ValidityState getValidityState() {
            return timestampValidityState;
        }

        public Module getModule() {
            return module;
        }

        public String getGeneratedClassName() {
            return generatedClassName;
        }
    }

    private static class PrepareAction implements Computable<GenerationItem[]> {
        private final CompileContext context;

        public PrepareAction(CompileContext context) {
            this.context = context;
        }

        public GenerationItem[] compute() {
            CompileScope compileScope = context.getCompileScope();
            VirtualFile[] files = compileScope.getFiles(JFlexFileTypeManager.getInstance().getFileType(), false);
            if (files != null) {
                GenerationItem[] items = new GenerationItem[files.length];
                for (int i = 0; i < files.length; i++) {
                    VirtualFile file = files[i];
                    items[i] = new JFlexGenerationItem(context.getModuleByFile(file), file);
                }
                return items;
            }
            return EMPTY_GENERATION_ITEM_ARRAY;
        }
    }
}
