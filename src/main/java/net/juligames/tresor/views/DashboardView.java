package net.juligames.tresor.views;


import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Container;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;
import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.controller.BankingController;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Ture Bentzin
 * @since 17-02-2025
 */
public class DashboardView {


    public static @NotNull Window getDashboardWindow(@NotNull TresorGUI gui) {

        TresorWindow window = new TresorWindow(gui, "window.dashboard");
        if (gui.getAuthenticationController().isAuthenticated()) {
            Panel panel = window.getContentPanel();
            populateDashboardContainer(gui, panel);
        } else {
            //user not logged in
            window.getContentPanel().addComponent(new Label(gui.getText("window.dashboard.not_authed", false)));
            window.getContentPanel().addComponent(new Button(gui.getText("window.dashboard.login", false), () -> {
                gui.getGui().addWindowAndWait(LoginView.getLoginWindow(gui));
            }));
        }

        return window;
    }


    private DashboardView() {
        throw new IllegalStateException("Utility class");
    }

    private static void populateDashboardContainer(@NotNull TresorGUI gui, @NotNull Panel panel) {
        panel.setLayoutManager(new GridLayout(2));
        panel.addComponent((gui.getTextWithParamsAsLabel("window.dashboard.motd", false,
                Map.of("motd", gui.getBankingController().getMOTD()))));
        panel.addComponent(gui.getTextWithParamsAsLabel("window.dashboard.authed", false,
                Map.of("username", gui.getAuthenticationController().getUsername().orElse("?"))));

        panel.addComponent(getAccountStatusContainer(gui));

    }

    private static @NotNull Container getAccountStatusContainer(@NotNull TresorGUI gui) {
        Panel panel = new Panel(new LinearLayout(Direction.VERTICAL));

        BankingController controller = gui.getBankingController();

        panel.addComponent(gui.getTextWithParamsAsLabel("window.dashboard.account.backend", false,
                Map.of("backend", gui.getAuthenticationController().getHost().orElse("?"))));

        panel.addComponent(gui.getTextWithParamsAsLabel("window.dashboard.account.username", false,
                Map.of(
                        "username", gui.getAuthenticationController().getUsername().orElse("?")
                )));

        panel.addComponent(gui.getTextWithParamsAsLabel("window.dashboard.account.balance", false,
                Map.of(
                        "balance", String.valueOf(controller.getBalance()),
                        "currency", controller.getCurrency()
                )));


        return panel.withBorder(Borders.singleLine(gui.getText("window.dashboard.account.title", false)));
    }


}
