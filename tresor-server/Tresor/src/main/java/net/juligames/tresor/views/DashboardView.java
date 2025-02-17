package net.juligames.tresor.views;


import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.menu.Menu;
import com.googlecode.lanterna.gui2.menu.MenuBar;
import com.googlecode.lanterna.gui2.menu.MenuItem;
import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.lang.Translations;
import net.juligames.tresor.model.ProjectPropertiesUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.SimpleTimeZone;


/**
 * @author Ture Bentzin
 * @since 17-02-2025
 */
public class DashboardView {

    public static @NotNull Window getDashboardWindow(@NotNull TresorGUI gui) {
        Window window = new BasicWindow(gui.getText("window.dashboard.title", false));
        window.setMenuBar(getMenu(gui, window));
        Panel contentPanel = new Panel(new GridLayout(2));
        window.setHints(Set.of(Window.Hint.EXPANDED));

        contentPanel.addComponent(new Label(gui.getText("window.dashboard.content", false)));
        window.setComponent(contentPanel);

        return window;
    }

    private static @NotNull MenuBar getMenu(@NotNull TresorGUI tresorGUI, @NotNull Window window) {
        MenuBar menuBar = new MenuBar();
        menuBar.add(getAboutMenu(tresorGUI, window));
        menuBar.add(getAboutMenu(tresorGUI, window));
        return menuBar;
    }

    private static @NotNull Menu getAboutMenu(@NotNull TresorGUI tresorGUI, @NotNull Window window) {
        Menu menu = new Menu(tresorGUI.getText("menu.about.title", false));
        menu.add(getAboutVersionMenuItem(tresorGUI, window));
        return menu;
    }

    private static @NotNull MenuItem getAboutVersionMenuItem(@NotNull TresorGUI tresorGUI, @Nullable Window window) {
        String brand = ProjectPropertiesUtil.getArtifactId();
        String version = ProjectPropertiesUtil.getGitVersion();
        String commit = ProjectPropertiesUtil.getGitCommit();
        String buildTime = ProjectPropertiesUtil.getGitBuildTime();
        String buildUser = ProjectPropertiesUtil.getGitBuildUserName();
        String branch = ProjectPropertiesUtil.getGitBranch();

        return new MenuItem(tresorGUI.getText("menu.about.version.title", false),
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


}
