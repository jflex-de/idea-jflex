package org.intellij.lang.jflex.compiler;

import com.intellij.execution.CantRunException;
import com.intellij.execution.configurations.CommandLineBuilder;
import com.intellij.execution.configurations.JavaParameters;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.compiler.CompilerMessageCategory;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.io.StreamUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.intellij.lang.jflex.options.JFlexConfigurable;
import org.intellij.lang.jflex.options.JFlexSettings;
import org.intellij.lang.jflex.util.JFlexBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JFlexx wrapper to command line tool.
 *
 * @author Alexey Efimov
 */
public final class JFlex {
    @NonNls
    private static final Pattern LINE_NUMBER_PATTERN = Pattern.compile(".*?\\(line\\s(\\d+)\\)\\:\\s*?$");

    @NonNls
    private static final String JFLEX_MAIN_CLASS = "JFlex.Main";
    @NonNls
    private static final String JFLEX_JAR_PATH = "./lib/JFlex.jar";
    @NonNls
    public static final String BIN_BASH = "/bin/bash";
    @NonNls
    public static final String HYPHEN_C = "-c";
    @NonNls
    public static final String SLASH_C = "/C";
    @NonNls
    private static final String COMMAND_COM = "command.com";
    @NonNls
    private static final String CMD_EXE = "cmd.exe";
    @NonNls
    private static final String OPTION_SKEL = " --skel ";
    @NonNls
    private static final String OPTION_D = " -d ";
    @NonNls
    private static final String OPTION_QUIET = " --quiet ";
    @NonNls
    private static final String OPTION_Q = " -q ";
    @NonNls
    private static final char QUOT = '"';
    @NonNls
    private static final char SPACE = ' ';

    @NotNull
    public static Map<CompilerMessageCategory, List<JFlexMessage>> compile(VirtualFile file, Sdk projectSdk) throws IOException, CantRunException {

        JFlexSettings settings = JFlexSettings.getInstance();

        //Earlier code used jflex.bat or jflex.sh. These files are broken after IDEA went open-source and changed
        //its sdk distribution structure. It's better to call JFlex.Main directly, not using any dumb bat or sh files.
        JavaParameters javaParameters = new JavaParameters();
        javaParameters.setJdk(projectSdk);
        javaParameters.setMainClass(JFLEX_MAIN_CLASS);
        javaParameters.getClassPath().add(JFLEX_JAR_PATH);

        StringBuilder command = new StringBuilder(CommandLineBuilder.createFromJavaParameters(javaParameters).getCommandLineString());

        //That options stuff can be refactored using javaParameters.getProgramParametersList().add(anOption),
        //as it does auto-quoting if necessary.
        String options = MessageFormat.format(" {0} ", settings.COMMAND_LINE_OPTIONS);
        if (!StringUtil.isEmptyOrSpaces(options)) {
            command.append(options);
        }
        if (!StringUtil.isEmptyOrSpaces(settings.SKELETON_PATH) && options.indexOf(OPTION_SKEL) == -1) {
            command.append(OPTION_SKEL).append(QUOT).append(settings.SKELETON_PATH).append(QUOT);
        }
        if (options.indexOf(OPTION_Q) == -1 && options.indexOf(OPTION_QUIET) == -1) {
            command.append(OPTION_QUIET);
        }
        VirtualFile parent = file.getParent();
        if (parent != null) {
            command.append(OPTION_D).append(QUOT).append(parent.getPath()).append(QUOT);
        }
        command.append(SPACE).append(QUOT).append(file.getPath()).append(QUOT);

        String shell = SystemInfo.isWindows ? SystemInfo.isWindows9x ? COMMAND_COM : CMD_EXE : BIN_BASH;
        String[] commands;
        if (SystemInfo.isWindows) {
            commands = new  String[]{shell, SLASH_C, QUOT + command.toString() + QUOT};
        } else {
            commands = new  String[]{shell, HYPHEN_C, command.toString()};
        }
        Process process = Runtime.getRuntime().exec(commands, null, new File(settings.JFLEX_HOME));
        try {
            InputStream out = process.getInputStream();
            try {
                InputStream err = process.getErrorStream();
                try {
                    List<JFlexMessage> information = new ArrayList<JFlexMessage>();
                    List<JFlexMessage> error = new ArrayList<JFlexMessage>();
                    filter(StreamUtil.readText(out), information, error);
                    filter(StreamUtil.readText(err), information, error);
                    Map<CompilerMessageCategory, List<JFlexMessage>> messages = new HashMap<CompilerMessageCategory, List<JFlexMessage>>();
                    messages.put(CompilerMessageCategory.ERROR, error);
                    messages.put(CompilerMessageCategory.INFORMATION, information);
                    int code = 0;
                    try {
                        code = process.waitFor();
                    } catch (InterruptedException e) {
                        List<JFlexMessage> warnings = new ArrayList<JFlexMessage>();
                        warnings.add(new JFlexMessage("Interrupted while waiting for Jflex to complete"));
                        messages.put(CompilerMessageCategory.WARNING, warnings);
                    }
                    if (code == 0) {
                        return messages;
                    } else {
                        if (messages.get(CompilerMessageCategory.ERROR).size() > 0) {
                            return messages;
                        }
                        throw new IOException(JFlexBundle.message("command.0.execution.failed.with.exit.code.1", command, code));
                    }
                } finally {
                    err.close();
                }
            } finally {
                out.close();
            }
        } finally {
            process.destroy();
        }
    }

    private static void filter(@NonNls String output, @NotNull List<JFlexMessage> information, @NotNull List<JFlexMessage> error) {
        if (!StringUtil.isEmptyOrSpaces(output)) {
            String[] lines = output.split("[\\n\\r]+");
            for (int i = 0; i < lines.length; i++) {
                @NonNls String line = lines[i];
                if (line.startsWith("Error in file") && i + 3 < lines.length) {
                    // Parse "error in file" message
                    // it look like:
                    // Error in file "JFlex.flex" (line 72):
                    // Syntax error.
                    // <LEXI CAL_RULES> {
                    //       ^
                    String message = lines[++i];
                    int lineNumber = -1;
                    int columnNumber = -1;
                    Matcher matcher = LINE_NUMBER_PATTERN.matcher(line);
                    if (matcher.matches()) {
                        try {
                            lineNumber = Integer.parseInt(matcher.group(1));
                        } catch (NumberFormatException e) {
                            // Ignore
                        }
                    }
                    // Skip line
                    i++;
                    char[] columnPointer = lines[++i].toCharArray();
                    for (int j = 0; columnNumber == -1 && j < columnPointer.length; j++) {
                        char c = columnPointer[j];
                        if (c != ' ') {
                            if (c == '^') {
                                columnNumber = j + 1;
                            } else {
                                // It is invalid code pointer line
                                // Rollback i to previous lines
                                i -= 2;
                                break;
                            }
                        }
                    }
                    error.add(new JFlexMessage(message, lineNumber, columnNumber));
                } else if (!line.startsWith("Reading skeleton file")) {
                    information.add(new JFlexMessage(line));
                }
            }
        }
    }

    public static boolean validateConfiguration(Project project) {
        JFlexSettings settings = JFlexSettings.getInstance();
        File home = new File(settings.JFLEX_HOME);
        if (home.isDirectory() && home.exists()) {
            if (!StringUtil.isEmptyOrSpaces(settings.SKELETON_PATH) && settings.COMMAND_LINE_OPTIONS.indexOf(OPTION_SKEL) == -1) {
                File skel = new File(settings.SKELETON_PATH);
                if (!skel.isFile() || !skel.exists()) {
                    return showWarningMessageAndConfigure(project, JFlexBundle.message("jflex.skeleton.file.was.not.found"));
                }
            }
        } else {
            return showWarningMessageAndConfigure(project, JFlexBundle.message("jflex.home.path.is.invalid"));
        }
        return true;
    }

    private static boolean showWarningMessageAndConfigure(final Project project, String message) {
        Messages.showWarningDialog(project, message, JFlexBundle.message("jflex"));
        // Show settings
        final Application application = ApplicationManager.getApplication();
        application.invokeLater(new Runnable() {
            public void run() {
                ShowSettingsUtil.getInstance().editConfigurable(project, application.getComponent(JFlexConfigurable.class));
            }
        });
        return false;
    }

    public static boolean isCompilationEnabled() {
        return JFlexSettings.getInstance().ENABLED_COMPILATION;
    }
}
