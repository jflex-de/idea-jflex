package org.intellij.lang.jflex;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.io.StreamUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.intellij.lang.jflex.options.JFlexSettings;
import org.intellij.lang.jflex.util.JFlexBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * JFlexx wrapper to command line tool.
 *
 * @author Alexey Efimov
 */
public class JFlex {
    @NonNls
    public static final String BIN_DIRECTORY = "bin";
    @NonNls
    public static final String JFLEX_BAT = "jflex.bat";
    @NonNls
    public static final String JFLEX_SH = "jflex.sh";
    @NonNls
    private static final String COMMAND_COM = "command.com /C ";
    @NonNls
    private static final String CMD_EXE = "cmd.exe /C";
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
    public static String[] exec(VirtualFile file) throws IOException {
        JFlexSettings settings = JFlexSettings.getInstance();
        StringBuffer command = new StringBuffer(SystemInfo.isWindows ? SystemInfo.isWindows9x ? COMMAND_COM : CMD_EXE : "");
        command.append(SystemInfo.isWindows ? JFLEX_BAT : JFLEX_SH);
        if (!StringUtil.isEmptyOrSpaces(settings.COMMAND_LINE_OPTIONS)) {
            command.append(SPACE).append(settings.COMMAND_LINE_OPTIONS);
        }
        if (!StringUtil.isEmptyOrSpaces(settings.SKELETON_PATH) && settings.COMMAND_LINE_OPTIONS.indexOf(OPTION_SKEL) == -1) {
            command.append(OPTION_SKEL).append(QUOT).append(settings.SKELETON_PATH).append(QUOT);
        }
        if (settings.COMMAND_LINE_OPTIONS.indexOf(OPTION_Q) == -1 &&
                settings.COMMAND_LINE_OPTIONS.indexOf(OPTION_QUIET) == -1) {
            command.append(OPTION_QUIET);
        }
        VirtualFile parent = file.getParent();
        if (parent != null) {
            command.append(OPTION_D).append(QUOT).append(parent.getPath()).append(QUOT);
        }
        command.append(SPACE).append(QUOT).append(file.getPath()).append(QUOT);
        Process process = Runtime.getRuntime().exec(command.toString(), null, new File(settings.JFLEX_HOME, BIN_DIRECTORY));
        try {
            InputStream out = process.getInputStream();
            try {
                InputStream err = process.getErrorStream();
                try {
                    String outText = StreamUtil.readText(out);
                    String errText = StreamUtil.readText(err);
                    int code = process.exitValue();
                    if (code == 0) {
                        return new String[]{outText, errText};
                    } else {
                        if (!StringUtil.isEmptyOrSpaces(errText)) {
                            return new String[]{outText, errText};
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

    public static boolean validateConfiguration(Project project) {
        JFlexSettings settings = JFlexSettings.getInstance();
        File home = new File(settings.JFLEX_HOME);
        if (home.isDirectory() && home.exists()) {
            File bin = new File(home, BIN_DIRECTORY);
            if (bin.isDirectory() && bin.exists()) {
                File script = new File(bin, SystemInfo.isWindows ? JFLEX_BAT : JFLEX_SH);
                if (script.isFile() && script.exists()) {
                    if (!StringUtil.isEmptyOrSpaces(settings.SKELETON_PATH) && settings.COMMAND_LINE_OPTIONS.indexOf(OPTION_SKEL) == -1) {
                        File skel = new File(settings.SKELETON_PATH);
                        if (!skel.isFile() || !skel.exists()) {
                            Messages.showWarningDialog(project, JFlexBundle.message("jflex.skeleton.file.was.not.found"), JFlexBundle.message("jflex"));
                            return false;
                        }
                    }
                } else {
                    Messages.showWarningDialog(project, JFlexBundle.message("jflex.home.path.is.invalid"), JFlexBundle.message("jflex"));
                    return false;
                }
            } else {
                Messages.showWarningDialog(project, JFlexBundle.message("jflex.home.path.is.invalid"), JFlexBundle.message("jflex"));
                return false;
            }
        } else {
            Messages.showWarningDialog(project, JFlexBundle.message("jflex.home.path.is.invalid"), JFlexBundle.message("jflex"));
            return false;
        }
        return true;
    }
}
