package org.intellij.lang.jflex.compiler;

import com.intellij.compiler.impl.CompilerUtil;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.compiler.*;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.intellij.lang.jflex.JFlexElementTypes;
import org.intellij.lang.jflex.fileTypes.JFlexFileTypeManager;
import org.intellij.lang.jflex.psi.JFlexClassStatement;
import org.intellij.lang.jflex.psi.JFlexExpression;
import org.intellij.lang.jflex.psi.JFlexPsiFile;
import org.intellij.lang.jflex.util.JFlexBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The source generating compiler for *.flex files.
 *
 * @author Alexey Efimov
 */
public class JFlexSourceGeneratingCompiler implements SourceGeneratingCompiler, ProjectComponent {
    @NonNls
    private static final String JAVA_FILE_NAME_PATTERN = "{0}.java";

    private final Project project;
    private CompilerManager compilerManager;

    public JFlexSourceGeneratingCompiler(Project project, CompilerManager compilerManager) {
        this.project = project;
        this.compilerManager = compilerManager;
    }

    public void projectOpened() {

    }

    public void projectClosed() {

    }

    @NotNull
    @NonNls
    public String getComponentName() {
        return "JFlexCompiler";
    }

    public void initComponent() {
        compilerManager.addCompilableFileType(JFlexFileTypeManager.getInstance().getFileType());
        compilerManager.addCompiler(this);
    }

    public void disposeComponent() {
        compilerManager.removeCompiler(this);
        compilerManager.removeCompilableFileType(JFlexFileTypeManager.getInstance().getFileType());

    }

    private static final GenerationItem[] EMPTY_GENERATION_ITEM_ARRAY = new GenerationItem[]{};

    public GenerationItem[] getGenerationItems(CompileContext context) {
        Module[] affectedModules = context.getCompileScope().getAffectedModules();
        if (affectedModules != null && affectedModules.length > 0) {
            Application application = ApplicationManager.getApplication();
            return application.runReadAction(new PrepareAction(context));
        }
        return EMPTY_GENERATION_ITEM_ARRAY;
    }

    public GenerationItem[] generate(CompileContext context, GenerationItem[] items, VirtualFile outputRootDirectory) {
        if (items != null && items.length > 0) {
            Application application = ApplicationManager.getApplication();
            GenerationItem[] generationItems = application.runReadAction(new GenerateAction(context, items, outputRootDirectory));
            for (GenerationItem item : generationItems) {
                CompilerUtil.refreshIOFile(((JFlexGenerationItem) item).getGeneratedFile());
            }
            return generationItems;
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
        return TimestampValidityState.load(is);
    }

    private static class JFlexGenerationItem implements GenerationItem {
        private final Module module;
        private final boolean testSource;
        private final VirtualFile file;
        private final String generatedClassName;
        private final File generatedFile;
        private String path;

        public JFlexGenerationItem(Module module, VirtualFile file, boolean testSource) {
            this.file = file;
            this.module = module;
            this.testSource = testSource;
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
            this.generatedClassName = generationName;
            this.generatedFile = new File(VfsUtil.virtualToIoFile(file.getParent()), MessageFormat.format(JAVA_FILE_NAME_PATTERN, generatedClassName));
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
            return null;
        }

        public Module getModule() {
            return module;
        }

        public String getGeneratedClassName() {
            return generatedClassName;
        }

        @NotNull
        public File getGeneratedFile() {
            return generatedFile;
        }

        public boolean isTestSource() {
            return testSource;
        }
    }

    private final class PrepareAction implements Computable<GenerationItem[]> {
        private final CompileContext context;

        public PrepareAction(CompileContext context) {
            this.context = context;
        }

        public GenerationItem[] compute() {
            ProjectFileIndex fileIndex = ProjectRootManager.getInstance(project).getFileIndex();
            CompileScope compileScope = context.getCompileScope();
            VirtualFile[] files = compileScope.getFiles(JFlexFileTypeManager.getInstance().getFileType(), false);
            if (files != null) {
                List<GenerationItem> items = new ArrayList<GenerationItem>(files.length);
                for (VirtualFile file : files) {
                    JFlexGenerationItem generationItem = new JFlexGenerationItem(context.getModuleByFile(file), file, fileIndex.isInTestSourceContent(file));
                    if (context.isMake()) {
                        File generatedFile = generationItem.getGeneratedFile();
                        if (!generatedFile.exists() || generatedFile.lastModified() <= file.getModificationStamp()) {
                            items.add(generationItem);
                        }
                    } else {
                        items.add(generationItem);
                    }
                }
                return items.toArray(new GenerationItem[items.size()]);
            }
            return EMPTY_GENERATION_ITEM_ARRAY;
        }
    }

    private static class GenerateAction implements Computable<GenerationItem[]> {
        private final CompileContext context;
        private final GenerationItem[] items;
        private final File outputDir;

        public GenerateAction(CompileContext context, GenerationItem[] items, VirtualFile outputRootDirectory) {
            this.context = context;
            this.items = items;
            outputDir = VfsUtil.virtualToIoFile(outputRootDirectory);
        }

        public GenerationItem[] compute() {
            List<GenerationItem> results = new ArrayList<GenerationItem>(items.length);
            for (GenerationItem item : items) {
                if (item instanceof JFlexGenerationItem) {
                    JFlexGenerationItem flexItem = (JFlexGenerationItem) item;
                    VirtualFile file = flexItem.getFile();
                    if (file != null && file.isValid()) {
                        try {
                            Map<CompilerMessageCategory, List<JFlexMessage>> messages = JFlex.compile(file);
                            addMessages(file, messages);
                            if (messages.get(CompilerMessageCategory.ERROR).isEmpty()) {
                                File outFile = flexItem.getGeneratedFile();
                                if (outFile.isFile() && outFile.exists()) {
                                    flexItem.setPath(FileUtil.getRelativePath(outputDir, outFile));
                                    results.add(flexItem);
                                } else {
                                    context.addMessage(CompilerMessageCategory.ERROR, JFlexBundle.message("file.not.generated"), file.getUrl(), -1, -1);
                                }
                            }
                        } catch (IOException e) {
                            context.addMessage(CompilerMessageCategory.ERROR, e.getMessage(), file.getUrl(), -1, -1);
                        }
                    }
                }
            }
            return results.toArray(new GenerationItem[results.size()]);
        }

        private void addMessages(VirtualFile file, Map<CompilerMessageCategory, List<JFlexMessage>> messages) {
            for (CompilerMessageCategory category : messages.keySet()) {
                List<JFlexMessage> messageList = messages.get(category);
                for (JFlexMessage message : messageList) {
                    context.addMessage(category, message.getMessage(), file.getUrl(), message.getLine(), message.getColumn());
                }
            }
        }
    }
}
