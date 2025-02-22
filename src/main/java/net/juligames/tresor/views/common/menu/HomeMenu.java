package net.juligames.tresor.views.common.menu;

import com.googlecode.lanterna.gui2.menu.Menu;
import com.googlecode.lanterna.gui2.menu.MenuItem;
import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.views.DashboardView;
import net.juligames.tresor.views.LoginView;
import net.juligames.tresor.views.RegisterView;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class HomeMenu {

    public static @NotNull Menu getHomeMenu(@NotNull TresorGUI tresorGUI) {
        Menu menu = new Menu(tresorGUI.getText("menu.home.title", true));
        menu.add(getDashboardItem(tresorGUI));
        menu.add(getLoginItem(tresorGUI));
        menu.add(getRegisterItem(tresorGUI));
        return menu;
    }

    @Contract("_ -> new")
    private static @NotNull MenuItem getDashboardItem(@NotNull TresorGUI tresorGUI) {
        return new MenuItem(tresorGUI.getText("menu.home.dashboard.title", true),
                () -> tresorGUI.switchWindow(DashboardView.getDashboardWindow(tresorGUI)));
    }

    private static @NotNull MenuItem getLoginItem(@NotNull TresorGUI tresorGUI) {
        return new MenuItem(tresorGUI.getText("menu.home.login.title", true),
                () -> tresorGUI.switchWindow(LoginView.getLoginWindow(tresorGUI)));
    }

    private static @NotNull MenuItem getRegisterItem(@NotNull TresorGUI tresorGUI) {
        return new MenuItem(tresorGUI.getText("menu.home.register.title", true),
                () -> tresorGUI.switchWindow(RegisterView.getRegisterWindow(tresorGUI)));
    }

    private HomeMenu() {
        throw new IllegalStateException("Utility class");
    }
}
