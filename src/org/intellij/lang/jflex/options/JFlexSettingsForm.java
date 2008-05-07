package org.intellij.lang.jflex.options;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.ui.ComponentWithBrowseButton;
import com.intellij.openapi.ui.FixedSizeButton;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.StateRestoringCheckBox;
import com.intellij.ui.TextFieldWithStoredHistory;
import com.intellij.util.ui.update.ComponentDisposable;
import org.intellij.lang.jflex.util.JFlexBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * JFlex options.
 *
 * @author Alexey Efimov
 */
public final class JFlexSettingsForm implements PersistentStateComponent<JFlexSettings> {
    @NonNls
    private static final String JFLEX_HOME_KEY = "JFlex.Home";
    @NonNls
    private static final String JFLEX_SKELETON_KEY = "JFlex.Skeleton";
    @NonNls
    private static final String JFLEX_OPTIONS_KEY = "JFlex.Options";
    @NonNls
    private static final String JFLEX_EMBEDJAVA_KEY = "JFlex.EmbedJava";

    private ComponentWithBrowseButton<TextFieldWithStoredHistory> jFlexHomeTextField;
    private ComponentWithBrowseButton<TextFieldWithStoredHistory> skeletonPathTextField;
    private JPanel formComponent;
    private TextFieldWithStoredHistory commandLineOptionsTextField;
    private JCheckBox injectJava;

    private final JFlexSettings settings = new JFlexSettings();

    public JFlexSettingsForm(JFlexSettings settings) {
        FileChooserDescriptor folderDescriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        FileChooserDescriptor fileDescriptor = FileChooserDescriptorFactory.createSingleLocalFileDescriptor();

        jFlexHomeTextField.addBrowseFolderListener(JFlexBundle.message("select.jflex.home"), JFlexBundle.message("please.select.jflex.folder"), null, folderDescriptor, new HistoryAccessor());
        skeletonPathTextField.addBrowseFolderListener(JFlexBundle.message("select.skeleton.file"), JFlexBundle.message("please.select.jflex.skeleton.file"), null, fileDescriptor, new HistoryAccessor());

        final Application application = ApplicationManager.getApplication();
        if (application != null && !application.isUnitTestMode() && !application.isHeadlessEnvironment()) {
            installAutoCompletion(folderDescriptor, jFlexHomeTextField.getChildComponent().getTextEditor());
            installAutoCompletion(fileDescriptor, skeletonPathTextField.getChildComponent().getTextEditor());
        }

        loadState(settings);
    }

    private static void installAutoCompletion(FileChooserDescriptor folderDescriptor, JTextField field) {
        FileChooserFactory.getInstance().installFileCompletion(field, folderDescriptor, true, new ComponentDisposable(field, null));
    }

    private void createUIComponents() {
        TextFieldWithStoredHistory jflexHomeHistory = createHistoryTextField(JFLEX_HOME_KEY, JFlexSettings.getDefaultJFlexHome());
        jFlexHomeTextField = new ComponentWithBrowseButton<TextFieldWithStoredHistory>(jflexHomeHistory, null);
        fixButton(jflexHomeHistory, jFlexHomeTextField);
        TextFieldWithStoredHistory skeletonPathHistory = createHistoryTextField(JFLEX_SKELETON_KEY, JFlexSettings.getDefaultSkeletonPath(JFlexSettings.getDefaultJFlexHome()));
        skeletonPathTextField = new ComponentWithBrowseButton<TextFieldWithStoredHistory>(skeletonPathHistory, null);
        fixButton(skeletonPathHistory, skeletonPathTextField);
        commandLineOptionsTextField = createHistoryTextField(JFLEX_OPTIONS_KEY, JFlexSettings.DEFAULT_OPTIONS_CHARAT_NOBAK);
        injectJava = new StateRestoringCheckBox(JFLEX_EMBEDJAVA_KEY, true);
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
        List<String> list = (List<String>) storedHistory.getHistory();
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
        return !jFlexHomeTextField.getChildComponent().getText().equals(state.JFLEX_HOME) ||
                !skeletonPathTextField.getChildComponent().getText().equals(state.SKELETON_PATH) ||
                !commandLineOptionsTextField.getText().equals(state.COMMAND_LINE_OPTIONS) ||
                injectJava.isSelected() != state.EMBEDJAVA;
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

    public ComponentWithBrowseButton<TextFieldWithStoredHistory> getJFlexHomeTextField() {
        return jFlexHomeTextField;
    }

    public ComponentWithBrowseButton<TextFieldWithStoredHistory> getSkeletonPathTextField() {
        return skeletonPathTextField;
    }

    public TextFieldWithStoredHistory getCommandLineOptionsTextField() {
        return commandLineOptionsTextField;
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
            settings.JFLEX_HOME = jFlexHomeTextField.getChildComponent().getText();
            settings.SKELETON_PATH = skeletonPathTextField.getChildComponent().getText();
            settings.COMMAND_LINE_OPTIONS = commandLineOptionsTextField.getText();
            settings.EMBEDJAVA = injectJava.isSelected();
        }
        return settings;
    }

    private boolean validate() {
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

        return true;
    }

    public final void loadState(JFlexSettings state) {
        settings.loadState(state);
        setTextWithHistory(jFlexHomeTextField.getChildComponent(), state.JFLEX_HOME);
        setTextWithHistory(skeletonPathTextField.getChildComponent(), state.SKELETON_PATH);
        setTextWithHistory(commandLineOptionsTextField, state.COMMAND_LINE_OPTIONS);
        injectJava.setSelected(state.EMBEDJAVA);
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
