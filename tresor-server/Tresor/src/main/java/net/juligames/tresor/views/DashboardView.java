package net.juligames.tresor.views;


import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.menu.Menu;
import com.googlecode.lanterna.gui2.menu.MenuBar;
import com.googlecode.lanterna.gui2.menu.MenuItem;
import net.juligames.tresor.TresorGUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.SimpleTimeZone;


/**
 * @author Ture Bentzin
 * @since 17-02-2025
 */
public class DashboardView {

    static @NotNull Window getDashboardWindow(@NotNull TresorGUI gui) {
        Window window = new BasicWindow(gui.getText("window.dashboard.title", false));
        window.setMenuBar(getMenu(gui, window));

        return window;
    }

    private static MenuBar getMenu(@NotNull TresorGUI tresorGUI, @NotNull Window window) {
        MenuBar menuBar = new MenuBar();
        menuBar.add(getAboutMenu(tresorGUI, window));
        return menuBar;
    }

    private static @NotNull Menu getAboutMenu(@NotNull TresorGUI tresorGUI, @NotNull Window window) {
        Menu menu = new Menu(tresorGUI.getText("menu.about.title", false));
        menu.add(getAboutVersionMenuItem(tresorGUI, window));
        return menu;
    }

    private static MenuItem getAboutVersionMenuItem(@NotNull TresorGUI tresorGUI, @Nullable Window window) {
        String brand = "Tresor";
        MenuItem menuItem = new MenuItem(tresorGUI.getText("menu.about.version", false),
                () -> MessageDialog.showMessageDialog(
                        tresorGUI.getGui(),
                        tresorGUI.getText("menu.about.version", false),
                        tresorGUI.getTextWithParams("menu.about.version.message", false, Map.of("brand", ""))
                ));
        return menuItem;
    }
}
