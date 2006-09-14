package org.intellij.lang.jflex.editor.colors;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.options.colors.ColorSettingsPages;
import org.jetbrains.annotations.NonNls;

final class JFlexEditorColorsManager implements ApplicationComponent {
    @NonNls
    public String getComponentName() {
        return "JFlexEditorColorsManager";
    }

    public void initComponent() {
        ColorSettingsPages.getInstance().registerPage(new JFlexColorPage());
    }

    public void disposeComponent() {
    }
}
