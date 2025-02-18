package net.juligames.tresor.views;


import com.googlecode.lanterna.gui2.*;
import net.juligames.tresor.TresorGUI;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static net.juligames.tresor.views.common.Common.getMenu;


/**
 * @author Ture Bentzin
 * @since 17-02-2025
 */
public class DashboardView {

    private static @NotNull Map<TresorGUI, Window> dashboardViewMap = new HashMap<>();

    public static @NotNull Window getDashboardWindow(@NotNull TresorGUI gui) {
        if (dashboardViewMap.containsKey(gui)) {
            return dashboardViewMap.get(gui);
        }
        Window window = new TresorWindow(gui, "window.dashboard");
        window.setMenuBar(getMenu(gui));
        Panel contentPanel = new Panel(new GridLayout(2));
        window.setHints(Set.of(Window.Hint.EXPANDED));

        contentPanel.addComponent(new Label(gui.getText("window.dashboard.content", false)));
        window.setComponent(contentPanel);

        dashboardViewMap.put(gui, window);

        return window;
    }

    public static void remove(@NotNull TresorGUI gui) {
        dashboardViewMap.remove(gui);
    }

    private DashboardView() {
        throw new IllegalStateException("Utility class");
    }


}
