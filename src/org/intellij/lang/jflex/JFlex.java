package org.intellij.lang.jflex;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.intellij.lang.jflex.options.JFlexSettings;

import java.io.File;
import java.io.IOException;

/**
 * JFlexx wrapper to command line tool.
 *
 * @author Alexey Efimov
 */
public class JFlex {
    public static void exec(VirtualFile file) throws IOException {
        JFlexSettings settings = JFlexSettings.getInstance();
        StringBuffer command = new StringBuffer("flex");
        if (!StringUtil.isEmptyOrSpaces(settings.COMMAND_LINE_OPTIONS)) {
            command.append(" ").append(settings.COMMAND_LINE_OPTIONS);
        }
        if (!StringUtil.isEmptyOrSpaces(settings.SKELETON_PATH)) {
            command.append(" --skel \"").append(settings.SKELETON_PATH).append("\"");
        }
        VirtualFile parent = file.getParent();
        if (parent != null) {
            command.append(" -d \"").append(parent.getPath()).append("\"");
        }
        command.append(" \"").append(file.getPath()).append("\"");
        Runtime.getRuntime().exec(command.toString(), null, new File(settings.JFLEX_HOME, "bin"));
    }
}
