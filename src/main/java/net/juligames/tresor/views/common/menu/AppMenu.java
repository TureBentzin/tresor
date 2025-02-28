package net.juligames.tresor.views.common.menu;


import com.googlecode.lanterna.gui2.menu.Menu;
import com.googlecode.lanterna.gui2.menu.MenuItem;
import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.views.PrivateMessagesView;
import org.jetbrains.annotations.NotNull;


/**
 * @author Ture Bentzin
 * @since 28-02-2025
 */
public class AppMenu {

    public static @NotNull Menu getAppMenu(@NotNull TresorGUI tresorGUI) {
        Menu menu = new Menu(tresorGUI.getText("menu.app.title", true));
        menu.add(getPMItem(tresorGUI));
        return menu;
    }

    public static @NotNull MenuItem getPMItem(@NotNull TresorGUI tresorGUI) {
        return new MenuItem(tresorGUI.getText("menu.app.private_messages.title", true),
                () -> tresorGUI.switchWindow(PrivateMessagesView.getPrivateMessagesWindow(tresorGUI)));
    }


    private AppMenu() {
        throw new IllegalStateException("Utility class");
    }
}
