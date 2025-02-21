package net.juligames.tresor.views;


import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Container;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;
import net.juligames.tresor.TresorGUI;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


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
        if (gui.getAuthenticationController().isAuthenticated()) {
            window.getContentPanel().addComponent(new Label(gui.getText("window.dashboard.content", false)));
            window.getContentPanel().addComponent(getDashboardContainer(gui));
        } else {
            //user not logged in
            window.getContentPanel().addComponent(new Label(gui.getText("window.dashboard.not_authed", false)));
            window.getContentPanel().addComponent(new Button(gui.getText("window.dashboard.login", false), () -> {
                gui.getGui().addWindowAndWait(LoginView.getLoginWindow(gui));
            }));
        }
        dashboardViewMap.put(gui, window);
        return window;
    }

    public static void remove(@NotNull TresorGUI gui) {
        dashboardViewMap.remove(gui);
    }

    private DashboardView() {
        throw new IllegalStateException("Utility class");
    }

    private static @NotNull Container getDashboardContainer(@NotNull TresorGUI gui) {
        Panel panel = new Panel(new GridLayout(2));
        panel.addComponent(new Label(gui.getText("window.dashboard.content", false)));
        panel.addComponent(gui.getTextWithParamsAsLabel("window.dashboard.authed", false, Map.of("username", gui.getAuthenticationController().getUsername().orElse("?"))));
        panel.addComponent(new Button("Button 1"));
        panel.addComponent(new Button("Button 2"));
        panel.addComponent(new Button("Button 3"));
        panel.addComponent(new Button("Button 4"));
        return panel.withBorder(Borders.singleLine(gui.getText("window.dashboard.title", false)));
    }


}
