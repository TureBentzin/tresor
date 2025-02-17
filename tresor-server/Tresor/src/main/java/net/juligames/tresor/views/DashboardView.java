package net.juligames.tresor.views;


import com.googlecode.lanterna.gui2.*;
import net.juligames.tresor.TresorGUI;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static net.juligames.tresor.views.common.Common.getMenu;


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


}
