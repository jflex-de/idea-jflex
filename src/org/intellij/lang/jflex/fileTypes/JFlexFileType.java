package org.intellij.lang.jflex.fileTypes;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.intellij.lang.jflex.JFlexLanguage;
import org.intellij.lang.jflex.util.JFlexBundle;

import javax.swing.*;

/**
 * JFlex file type.
 *
 * @author Alexey Efimov
 */
public final class JFlexFileType extends LanguageFileType {
    @NonNls
    public static final String[] DEFAULT_ASSOCIATED_EXTENSIONS = new String[] {"flex"};

    public JFlexFileType() {
        super(new JFlexLanguage());
    }

    @NotNull
    @NonNls
    public String getDefaultExtension() {
        return DEFAULT_ASSOCIATED_EXTENSIONS[0];
    }

    @NotNull
    public String getDescription() {
        return JFlexBundle.message("jflex.filetype.description");
    }

    @Nullable
    public Icon getIcon() {
        return IconLoader.getIcon("/fileTypes/jflex.png");
    }

    @NotNull
    @NonNls
    public String getName() {
        return "JFlex";
    }
}
