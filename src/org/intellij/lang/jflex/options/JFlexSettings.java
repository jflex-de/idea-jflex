package org.intellij.lang.jflex.options;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.components.ExportableApplicationComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.intellij.lang.jflex.util.JFlexBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Options of JFlex.
 *
 * @author Alexey Efimov
 */
@State(name = "JFlexSettings", storages = {@Storage(id = "jflex", file = "$APP_CONFIG$/jflex.xml")})
public final class JFlexSettings implements PersistentStateComponent<JFlexSettings>, ExportableApplicationComponent {
    @NonNls
    static final String TOOLS_DIR = "tools";
    @NonNls
    static final String IDEA_FLEX_SKELETON = "idea-flex.skeleton";
    @NonNls
    static final String DEFAULT_OPTIONS_CHARAT_NOBAK = "--charat --nobak";

    public static JFlexSettings getInstance() {
        return ApplicationManager.getApplication().getComponent(JFlexSettings.class);
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

    @NotNull
    public File[] getExportFiles() {
        return new File[]{PathManager.getOptionsFile("jflex")};
    }

    @NotNull
    public String getPresentableName() {
        return JFlexBundle.message("jflex.settings");
    }

    @NotNull
    @NonNls
    public String getComponentName() {
        return "JFlexSettings";
    }

    public void initComponent() {

    }

    public void disposeComponent() {

    }

    public static String getDefaultSkeletonPath(String jFlexHome) {
        return new File(jFlexHome, IDEA_FLEX_SKELETON).getPath();
    }

    public static String getDefaultJFlexHome() {
        return new File(new File(PathManager.getHomePath(), TOOLS_DIR), "jflex").getPath();
    }
}
