package net.juligames.tresor.views.common.menu;

import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.menu.Menu;
import com.googlecode.lanterna.gui2.menu.MenuItem;
import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.model.ProjectPropertiesUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class AboutMenu {

    public static @NotNull Menu getAboutMenu(@NotNull TresorGUI tresorGUI) {
        Menu menu = new Menu(tresorGUI.getText("menu.about.title", true));
        menu.add(getVersionItem(tresorGUI));
        return menu;
    }

    public static @NotNull MenuItem getVersionItem(@NotNull TresorGUI tresorGUI) {
        String brand = ProjectPropertiesUtil.getArtifactId();
        String version = ProjectPropertiesUtil.getGitVersion();
        String commit = ProjectPropertiesUtil.getGitCommit() + (ProjectPropertiesUtil.isDevelopment() ? " (dev)" : "");
        String buildTime = ProjectPropertiesUtil.getPrettyBuildTime();
        String buildUser = ProjectPropertiesUtil.getGitBuildUserName();
        String branch = ProjectPropertiesUtil.getGitBranch();

        return new MenuItem(tresorGUI.getText("menu.about.version.title", true),
                () -> MessageDialog.showMessageDialog(tresorGUI.getGui(),
                        tresorGUI.getText("menu.about.version.title",
                                false), tresorGUI.getTextWithParams("menu.about.version.content",
                                false,
                                Map.of(
                                        "brand", brand,
                                        "version", version,
                                        "buildTime", buildTime,
                                        "buildUser", buildUser,
                                        "commit", commit,
                                        "branch", branch
                                ))));
    }

    private AboutMenu() {
        throw new IllegalStateException("Utility class");
    }
}
