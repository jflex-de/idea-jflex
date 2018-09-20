package de.jflex.ide.idea.options;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NonNls;

import java.io.File;

/**
 * Options of JFlex.
 *
 * @author Alexey Efimov
 */
@State(name = "JFlexSettings", storages = {@Storage(file = "$APP_CONFIG$/jflex.xml")})
public final class JFlexSettings implements PersistentStateComponent<JFlexSettings> {
    @NonNls
    static final String TOOLS_DIR = "tools";
    @NonNls
    static final String IDEA_FLEX_SKELETON = "idea-flex.skeleton";
    @NonNls
    static final String DEFAULT_OPTIONS_CHARAT_NOBAK = "--charat --nobak";

    public static JFlexSettings getInstance() {
        return ServiceManager.getService(JFlexSettings.class);
    }

    public boolean ENABLED_COMPILATION = true;
    public String JFLEX_HOME = getDefaultJFlexHome();
    public String SKELETON_PATH = getDefaultSkeletonPath(JFLEX_HOME);
    public String COMMAND_LINE_OPTIONS = DEFAULT_OPTIONS_CHARAT_NOBAK;
    public boolean ENABLED_EMBED_JAVA = true;

    public JFlexSettings getState() {
        return this;
    }

    public void loadState(JFlexSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }



    public static String getDefaultSkeletonPath(String jFlexHome) {
        return new File(jFlexHome, IDEA_FLEX_SKELETON).getPath();
    }

    public static String getDefaultJFlexHome() {
        return new File(new File(PathManager.getHomePath(), TOOLS_DIR), "jflex").getPath();
    }
}
