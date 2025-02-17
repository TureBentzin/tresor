package net.juligames.tresor.views.common;

import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.gui2.menu.Menu;
import com.googlecode.lanterna.gui2.menu.MenuBar;
import com.googlecode.lanterna.gui2.menu.MenuItem;
import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.views.common.menu.AboutMenu;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Common {

    private static final @NotNull Logger log = LoggerFactory.getLogger(Common.class);

    public static @NotNull MenuBar getMenu(@NotNull TresorGUI tresorGUI, @NotNull Window window) {
        MenuBar menuBar = new MenuBar();
        menuBar.add(AboutMenu.getAboutMenu(tresorGUI));

        menuBar.add(getQuitMenu(tresorGUI, window));
        return menuBar;
    }

    public static @NotNull Menu getQuitMenu(@NotNull TresorGUI tresorGUI, @NotNull Window window) {
        Menu menu = new Menu(tresorGUI.getText("menu.quit.title", true));
        menu.add(new MenuItem(tresorGUI.getText("menu.quit.entry", true),
                () -> {
                    if (MessageDialog.showMessageDialog(tresorGUI.getGui(),
                            tresorGUI.getText("menu.quit.title", true),
                            tresorGUI.getText("menu.quit.content", false),
                            MessageDialogButton.Yes, MessageDialogButton.Abort) == MessageDialogButton.Yes) {
                        window.close();
                    }

                }));
        return menu;
    }

    private Common() {
        throw new IllegalStateException("Utility class");
    }
}
