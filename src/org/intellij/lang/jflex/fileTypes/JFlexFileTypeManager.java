package org.intellij.lang.jflex.fileTypes;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;
import org.intellij.lang.jflex.compiler.JFlexSourceGeneratingCompiler;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * JFlex file type manager.
 *
 * @author Alexey Efimov
 */
public final class JFlexFileTypeManager implements ApplicationComponent, ProjectManagerListener {
    private final JFlexSourceGeneratingCompiler compiler = new JFlexSourceGeneratingCompiler();

    public static JFlexFileTypeManager getInstance() {
        return ApplicationManager.getApplication().getComponent(JFlexFileTypeManager.class);
    }

    private final JFlexFileType fileType = new JFlexFileType();

    @NotNull
    @NonNls
    public String getComponentName() {
        return "JFlexFileTypeManager";
    }

    public void initComponent() {
        FileTypeManager.getInstance().registerFileType(fileType, JFlexFileType.DEFAULT_ASSOCIATED_EXTENSIONS);
        ProjectManager.getInstance().addProjectManagerListener(this);
    }

    public void disposeComponent() {
        ProjectManager.getInstance().removeProjectManagerListener(this);
    }

    public final JFlexFileType getFileType() {
        return fileType;
    }

    public void projectOpened(Project project) {
        CompilerManager manager = CompilerManager.getInstance(project);
        manager.addCompilableFileType(fileType);
        manager.addCompiler(compiler);
    }

    public boolean canCloseProject(Project project) {
        return true;
    }

    public void projectClosed(Project project) {
        CompilerManager manager = CompilerManager.getInstance(project);
        manager.removeCompilableFileType(fileType);
        manager.removeCompiler(compiler);

    }

    public void projectClosing(Project project) {

    }
}
