package net.juligames.tresor.views;


import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import net.juligames.tresor.TresorGUI;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.SimpleTimeZone;


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
        TresorWindow window = new TresorWindow(gui, "window.dashboard");
        window.getContentPanel().addComponent(new Label(gui.getText("window.dashboard.content", false)));
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
