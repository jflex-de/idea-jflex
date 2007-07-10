package org.intellij.lang.jflex.fileTypes;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.fileTypes.FileTypeManager;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * JFlex file type manager.
 *
 * @author Alexey Efimov
 */
public final class JFlexFileTypeManager implements ApplicationComponent {
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
    }

    public void disposeComponent() {
    }

    public final JFlexFileType getFileType() {
        return fileType;
    }

}
