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
import net.juligames.tresor.controller.UserController;
import net.juligames.tresor.error.MissingAuthenticationException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static java.util.Map.of;


/**
 * @author Ture Bentzin
 * @since 17-02-2025
 */
public class DashboardView {


    private static final @NotNull Logger log = LoggerFactory.getLogger(DashboardView.class);

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
                of("motd", gui.getBankingController().getMOTD()))));

        panel.addComponent(getAccountStatusContainer(gui));

    }

    private static @NotNull Container getAccountStatusContainer(@NotNull TresorGUI gui) {
        Panel panel = new Panel(new LinearLayout(Direction.VERTICAL));

        BankingController bankingController = gui.getBankingController();
        UserController userController = gui.getUserController();


        panel.addComponent(gui.getTextWithParamsAsLabel("window.dashboard.account.backend", false,
                of(
                        "backend", gui.getAuthenticationController().getHost().orElse("?")
                )));

        panel.addComponent(gui.getTextWithParamsAsLabel("window.dashboard.account.username", false,
                of(
                        "username", gui.getAuthenticationController().getUsername().orElse("?")
                )));

        try {
            panel.addComponent(gui.getTextWithParamsAsLabel("window.dashboard.account.balance", false,
                    of(
                            "balance", String.valueOf(userController.getBalance()),
                            "currency", bankingController.getCurrency()
                    )));
        } catch (MissingAuthenticationException e) {
            log.error("Error getting balance", e);
        }


        return panel.withBorder(Borders.singleLine(gui.getText("window.dashboard.account.title", false)));
    }


}
