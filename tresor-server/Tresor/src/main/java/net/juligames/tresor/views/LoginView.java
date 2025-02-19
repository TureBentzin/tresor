package net.juligames.tresor.views;


import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import net.juligames.tresor.Tresor;
import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.model.ConfigModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author Ture Bentzin
 * @since 19-02-2025
 */
public class LoginView {

    private static final @NotNull Logger log = LoggerFactory.getLogger(LoginView.class);

    public static @NotNull Window getLoginWindow(final @NotNull TresorGUI gui) {
        TresorWindow tresorWindow = new TresorWindow(gui, "window.login", false);
        tresorWindow.setHints(Set.of(Window.Hint.CENTERED, Window.Hint.MENU_POPUP));

        // Add components to the window
        tresorWindow.getContentPanel().addComponent(gui.getTextAsLabel("window.login.content", false));
        tresorWindow.getContentPanel().addComponent(getLoginPane(gui));
        return tresorWindow;
    }

    public static @NotNull Container getLoginPane(@NotNull TresorGUI gui) {

        //server, username, password, login button
        Panel panel = new Panel(new LinearLayout(Direction.VERTICAL));


        TerminalSize inputSize = new TerminalSize(35, 1);


        TextBox serverTextBox = new TextBox(inputSize);
        panel.addComponent(gui.getTextAsLabel("window.login.server", false));
        serverTextBox.setValidationPattern(Pattern.compile("^(http|https)://.*$"));
        serverTextBox.setText(Tresor.getConfig().defaultServer());

        panel.addComponent(serverTextBox);

        TextBox usernameTextBox = new TextBox(inputSize);
        panel.addComponent(gui.getTextAsLabel("window.login.username", false));
        panel.addComponent(usernameTextBox);

        TextBox passwordTextBox = new TextBox(inputSize);
        panel.addComponent(gui.getTextAsLabel("window.login.password", false));
        panel.addComponent(passwordTextBox);
        passwordTextBox.setMask('*');

        Button loginButton = new Button(gui.getText("window.login.login", false), () -> {
            log.info("Login button pressed: Server: {}, Username: {}, Password: {}",
                    serverTextBox.getText(),
                    usernameTextBox.getText(),
                    passwordTextBox.getText()
            );
            String server = serverTextBox.getText();
            ConfigModel config = Tresor.getConfig();
            if (config.getServerWhitelistRegex().stream().anyMatch(server::matches) &&
                    config.getServerBlacklistRegex().stream().noneMatch(server::matches)) {
                log.info("Server {} is allowed", server);
            } else {
                log.warn("Server {} is not allowed", server);
            }


        });

        panel.addComponent(new EmptySpace(inputSize));
        panel.addComponent(loginButton);


        return panel.withBorder(Borders.singleLine(gui.getText("window.login.title", false)));
    }


    private LoginView() {
        throw new IllegalStateException("Utility class");
    }


}
