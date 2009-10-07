package org.intellij.lang.jflex.options;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.ui.ComponentWithBrowseButton;
import com.intellij.openapi.ui.FixedSizeButton;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.StateRestoringCheckBox;
import com.intellij.ui.TextFieldWithStoredHistory;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.intellij.lang.jflex.util.JFlexBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * JFlex options.
 *
 * @author Alexey Efimov
 */
public final class JFlexSettingsForm implements PersistentStateComponent<JFlexSettings> {
    @NonNls
    private static final String JFLEX_ENABLED_COMPILATION_KEY = "JFlex.EnabledCompilation";
    @NonNls
    private static final String JFLEX_HOME_KEY = "JFlex.Home";
    @NonNls
    private static final String JFLEX_SKELETON_KEY = "JFlex.Skeleton";
    @NonNls
    private static final String JFLEX_OPTIONS_KEY = "JFlex.Options";
    @NonNls
    private static final String JFLEX_ENABLED_EMBED_JAVA_KEY = "JFlex.EnabledEmbedJava";

    private ComponentWithBrowseButton<TextFieldWithStoredHistory> jFlexHomeTextField;
    private ComponentWithBrowseButton<TextFieldWithStoredHistory> skeletonPathTextField;
    private JPanel formComponent;
    private TextFieldWithStoredHistory commandLineOptionsTextField;
    private JCheckBox enabledEmbedJavaCheckBox;
    private JCheckBox enabledCompilationCheckBox;

    private final JFlexSettings settings = new JFlexSettings();

    public JFlexSettingsForm(JFlexSettings settings) {
        $$$setupUI$$$();
        FileChooserDescriptor folderDescriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        FileChooserDescriptor fileDescriptor = FileChooserDescriptorFactory.createSingleLocalFileDescriptor();

        jFlexHomeTextField.addBrowseFolderListener(JFlexBundle.message("select.jflex.home"), JFlexBundle.message("please.select.jflex.folder"), null, folderDescriptor, new HistoryAccessor());
        skeletonPathTextField.addBrowseFolderListener(JFlexBundle.message("select.skeleton.file"), JFlexBundle.message("please.select.jflex.skeleton.file"), null, fileDescriptor, new HistoryAccessor());

        loadState(settings);
    }

    private void createUIComponents() {
        enabledCompilationCheckBox = new StateRestoringCheckBox(JFLEX_ENABLED_COMPILATION_KEY, true);
        TextFieldWithStoredHistory jflexHomeHistory = createHistoryTextField(JFLEX_HOME_KEY, JFlexSettings.getDefaultJFlexHome());
        jFlexHomeTextField = new ComponentWithBrowseButton<TextFieldWithStoredHistory>(jflexHomeHistory, null);
        fixButton(jflexHomeHistory, jFlexHomeTextField);
        TextFieldWithStoredHistory skeletonPathHistory = createHistoryTextField(JFLEX_SKELETON_KEY, JFlexSettings.getDefaultSkeletonPath(JFlexSettings.getDefaultJFlexHome()));
        skeletonPathTextField = new ComponentWithBrowseButton<TextFieldWithStoredHistory>(skeletonPathHistory, null);
        fixButton(skeletonPathHistory, skeletonPathTextField);
        commandLineOptionsTextField = createHistoryTextField(JFLEX_OPTIONS_KEY, JFlexSettings.DEFAULT_OPTIONS_CHARAT_NOBAK);
        enabledEmbedJavaCheckBox = new StateRestoringCheckBox(JFLEX_ENABLED_EMBED_JAVA_KEY, true);
    }

    private void fixButton(final TextFieldWithStoredHistory historyField, ComponentWithBrowseButton<TextFieldWithStoredHistory> control) {
        FixedSizeButton button = control.getButton();
        control.remove(button);
        BorderLayout borderLayout = new BorderLayout();
        JPanel buttonPanel = new JPanel(borderLayout);
        buttonPanel.setBorder(IdeBorderFactory.createEmptyBorder(4, 0, 4, 0));
        buttonPanel.add(button, BorderLayout.CENTER);
        control.add(buttonPanel, BorderLayout.EAST);
        button.setAttachedComponent(new JComponent() {
            public Dimension getPreferredSize() {
                Dimension size = historyField.getTextEditor().getPreferredSize();
                return new Dimension(-1, size.height + 6);
            }

        });
    }

    @SuppressWarnings({"unchecked"})
    private static TextFieldWithStoredHistory createHistoryTextField(@NotNull String name, @NotNull String... defaultValues) {
        TextFieldWithStoredHistory storedHistory = new TextFieldWithStoredHistoryBugFixed(name);
        storedHistory.reset();
        List<String> list = storedHistory.getHistory();
        list.removeAll(Arrays.asList(defaultValues));
        if (list.isEmpty()) {
            // Default histories
            for (String defaultValue : defaultValues) {
                setTextWithHistory(storedHistory, defaultValue);
            }
        } else {
            storedHistory.setSelectedItem(list.get(list.size() - 1));
        }
        return storedHistory;
    }

    public JComponent getFormComponent() {
        return formComponent;
    }

    public boolean isModified(JFlexSettings state) {
        return enabledCompilationCheckBox.isSelected() != state.ENABLED_COMPILATION ||
            !jFlexHomeTextField.getChildComponent().getText().equals(state.JFLEX_HOME) ||
            !skeletonPathTextField.getChildComponent().getText().equals(state.SKELETON_PATH) ||
            !commandLineOptionsTextField.getText().equals(state.COMMAND_LINE_OPTIONS) ||
            enabledEmbedJavaCheckBox.isSelected() != state.ENABLED_EMBED_JAVA;
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        formComponent = new JPanel();
        formComponent.setLayout(new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("org/intellij/lang/jflex/util/JFlexBundle").getString("path.to.jflex"));
        formComponent.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        formComponent.add(spacer1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        formComponent.add(jFlexHomeTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("org/intellij/lang/jflex/util/JFlexBundle").getString("skeleton.file"));
        formComponent.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        formComponent.add(skeletonPathTextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("org/intellij/lang/jflex/util/JFlexBundle").getString("command.line.options"));
        formComponent.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        formComponent.add(commandLineOptionsTextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        this.$$$loadButtonText$$$(enabledEmbedJavaCheckBox, ResourceBundle.getBundle("org/intellij/lang/jflex/util/JFlexBundle").getString("enabled.embed.java.code.support"));
        formComponent.add(enabledEmbedJavaCheckBox, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        label1.setLabelFor(jFlexHomeTextField);
        label2.setLabelFor(skeletonPathTextField);
        label3.setLabelFor(commandLineOptionsTextField);
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadLabelText$$$(JLabel component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) {
                    break;
                }
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) {
                    break;
                }
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return formComponent;
    }

    /**
     * Fixed text component. Enabling is valid now.
     */
    private static final class TextFieldWithStoredHistoryBugFixed extends TextFieldWithStoredHistory {
        public TextFieldWithStoredHistoryBugFixed(String name) {
            super(name, false);
        }

        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            getTextEditor().setEnabled(enabled);
            getTextEditor().setEditable(enabled);
        }
    }

    private static class HistoryAccessor implements TextComponentAccessor<TextFieldWithStoredHistory> {
        public String getText(TextFieldWithStoredHistory component) {
            return component.getText().replace('\\', '/');
        }

        public void setText(TextFieldWithStoredHistory component, String text) {
            setTextWithHistory(component, text);
        }
    }

    public final JFlexSettings getState() {
        if (validate()) {
            settings.ENABLED_COMPILATION = enabledCompilationCheckBox.isSelected();
            settings.JFLEX_HOME = jFlexHomeTextField.getChildComponent().getText();
            settings.SKELETON_PATH = skeletonPathTextField.getChildComponent().getText();
            settings.COMMAND_LINE_OPTIONS = commandLineOptionsTextField.getText();
            settings.ENABLED_EMBED_JAVA = enabledEmbedJavaCheckBox.isSelected();
        }
        return settings;
    }

    private boolean validate() {
        if (enabledCompilationCheckBox.isSelected()) {
            String text = jFlexHomeTextField.getChildComponent().getText();
            if (StringUtil.isEmptyOrSpaces(text)) {
                Messages.showWarningDialog(jFlexHomeTextField, JFlexBundle.message("please.enter.path.to.jflex.home.directory"), JFlexBundle.message("jflex"));
                jFlexHomeTextField.requestFocus();
                return false;
            }

            // All fine add to history
            jFlexHomeTextField.getChildComponent().addCurrentTextToHistory();
            skeletonPathTextField.getChildComponent().addCurrentTextToHistory();
            commandLineOptionsTextField.addCurrentTextToHistory();
        }
        return true;
    }

    public final void loadState(JFlexSettings state) {
        settings.loadState(state);
        enabledCompilationCheckBox.setSelected(state.ENABLED_COMPILATION);
        setTextWithHistory(jFlexHomeTextField.getChildComponent(), state.JFLEX_HOME);
        setTextWithHistory(skeletonPathTextField.getChildComponent(), state.SKELETON_PATH);
        setTextWithHistory(commandLineOptionsTextField, state.COMMAND_LINE_OPTIONS);
        enabledEmbedJavaCheckBox.setSelected(state.ENABLED_EMBED_JAVA);
    }

    private static void setTextWithHistory(TextFieldWithStoredHistory component, String text) {
        if (StringUtil.isEmptyOrSpaces(text)) {
            component.setText(null);
        } else {
            component.setText(text);
            component.addCurrentTextToHistory();
        }
    }

}
