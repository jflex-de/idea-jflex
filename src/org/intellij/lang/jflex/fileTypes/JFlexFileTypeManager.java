package org.intellij.lang.jflex.fileTypes;

import com.intellij.openapi.application.ApplicationManager;

/**
 * JFlex file type manager.
 *
 * @author Alexey Efimov
 */
public abstract class JFlexFileTypeManager {
    public static JFlexFileTypeManager getInstance() {
        return ApplicationManager.getApplication().getComponent(JFlexFileTypeManager.class);
    }

    public abstract JFlexFileType getFileType();
}
