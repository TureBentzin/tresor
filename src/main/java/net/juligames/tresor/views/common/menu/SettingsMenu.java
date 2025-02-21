package net.juligames.tresor.views.common.menu;

import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.gui2.menu.Menu;
import com.googlecode.lanterna.gui2.menu.MenuItem;
import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.model.ProjectPropertiesUtil;
import net.juligames.tresor.views.DashboardView;
import net.juligames.tresor.views.SettingsView;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

public class SettingsMenu {

    public static @NotNull Menu getSystemMenu(@NotNull TresorGUI tresorGUI) {
        Menu menu = new Menu(tresorGUI.getText("menu.system.title", true));
        menu.add(new MenuItem(tresorGUI.getText("menu.system.quit.title", true),
                () -> {
                    if (MessageDialog.showMessageDialog(tresorGUI.getGui(),
                            tresorGUI.getText("menu.system.quit.title", true),
                            tresorGUI.getText("menu.system.quit.content", false),
                            MessageDialogButton.Yes, MessageDialogButton.Abort) == MessageDialogButton.Yes) {
                        tresorGUI.quit();
                    }

                }));
        menu.add(new MenuItem(tresorGUI.getText("menu.system.settings.title", true),
                () -> {
                    tresorGUI.switchWindow(SettingsView.getSettingsWindow(tresorGUI));
                }));
        return menu;
    }

    private SettingsMenu() {
        throw new IllegalStateException("Utility class");
    }
}
