package org.intellij.lang.jflex.options;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.intellij.lang.jflex.util.JFlexBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Configurable for JFlex.
 *
 * @author Alexey Efimov
 */
public final class JFlexConfigurable implements Configurable {
    private JFlexSettingsForm settingsForm;

    @Nls
    public String getDisplayName() {
        return JFlexBundle.message("jflex");
    }

    @Nullable
    public Icon getIcon() {
        return null;
    }

    @Nullable
    @NonNls
    public String getHelpTopic() {
        return null;
    }

    public JComponent createComponent() {
        if (settingsForm == null) {
            settingsForm = new JFlexSettingsForm(JFlexSettings.getInstance());
        }
        return settingsForm.getFormComponent();
    }

    public boolean isModified() {
        if (settingsForm != null) {
            return settingsForm.isModified(JFlexSettings.getInstance());
        }
        return false;
    }

    public void apply() throws ConfigurationException {
        if (settingsForm != null) {
            JFlexSettings.getInstance().loadState(settingsForm.getState());
        }
    }

    public void reset() {
        if (settingsForm != null) {
            settingsForm.loadState(JFlexSettings.getInstance());
        }
    }

    public void disposeUIResources() {
        settingsForm = null;
    }

    @NonNls
    @NotNull
    public String getComponentName() {
        return "JFlexSettings.Configurable";
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }
}
