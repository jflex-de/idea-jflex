package org.intellij.lang.jflex.fileTypes.impl;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.fileTypes.FileTypeManager;
import org.intellij.lang.jflex.fileTypes.JFlexFileType;
import org.intellij.lang.jflex.fileTypes.JFlexFileTypeManager;
import org.jetbrains.annotations.NonNls;

final class JFlexFileTypeManagerImpl extends JFlexFileTypeManager implements ApplicationComponent {
    private final JFlexFileType fileType = new JFlexFileType();

    public void disposeComponent() {
    }

    @NonNls
    public String getComponentName() {
        return "JFlexFileTypeManager";
    }

    public void initComponent() {
        FileTypeManager.getInstance().registerFileType(fileType, JFlexFileType.DEFAULT_ASSOCIATED_EXTENSIONS);
    }

    public JFlexFileType getFileType() {
        return fileType;
    }

}
