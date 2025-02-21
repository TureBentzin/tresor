package net.juligames.tresor.views.common;

import com.googlecode.lanterna.gui2.menu.MenuBar;
import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.views.common.menu.AboutMenu;
import net.juligames.tresor.views.common.menu.HomeMenu;
import net.juligames.tresor.views.common.menu.SettingsMenu;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class Common {

    private static final @NotNull Logger log = LoggerFactory.getLogger(Common.class);

    public static @NotNull HashMap<TresorGUI, MenuBar> menuBarHashMap = new HashMap<>();

    public static @NotNull MenuBar getMenu(@NotNull TresorGUI tresorGUI) {
        if (menuBarHashMap.containsKey(tresorGUI)) {
            return menuBarHashMap.get(tresorGUI);
        }
        MenuBar menuBar = new MenuBar();
        menuBar.add(HomeMenu.getHomeMenu(tresorGUI));
        menuBar.add(AboutMenu.getAboutMenu(tresorGUI));

        menuBar.add(SettingsMenu.getSystemMenu(tresorGUI));
        menuBarHashMap.put(tresorGUI, menuBar);
        log.info("Menu created");
        return menuBar;
    }

    public static void remove(@NotNull TresorGUI tresorGUI) {
        menuBarHashMap.remove(tresorGUI);
    }

    private Common() {
        throw new IllegalStateException("Utility class");
    }
}
