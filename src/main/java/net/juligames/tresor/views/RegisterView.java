package net.juligames.tresor.views;


import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import net.juligames.tresor.Tresor;
import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.controller.AuthenticationController;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author Ture Bentzin
 * @since 22-02-2025
 */
public class RegisterView {
    private static final @NotNull Logger log = LoggerFactory.getLogger(LoginView.class);

    public static @NotNull Window getRegisterWindow(final @NotNull TresorGUI gui) {
        TresorWindow tresorWindow = new TresorWindow(gui, "window.register", false);
        tresorWindow.setHints(Set.of(Window.Hint.CENTERED, Window.Hint.MENU_POPUP));

        // Add components to the window
        tresorWindow.getContentPanel().addComponent(gui.getTextAsLabel("window.register.content", false));
        tresorWindow.getContentPanel().addComponent(getRegisterPane(gui, tresorWindow));
        return tresorWindow;
    }

    public static @NotNull Container getRegisterPane(@NotNull TresorGUI gui, @NotNull Window window) {

        //server, username, password, register button
        Panel panel = new Panel(new LinearLayout(Direction.VERTICAL));


        TerminalSize inputSize = new TerminalSize(35, 1);


        TextBox serverTextBox = new TextBox(inputSize);
        panel.addComponent(gui.getTextAsLabel("window.register.server", false));
        serverTextBox.setValidationPattern(Pattern.compile("^(http|https)://.*$"));
        serverTextBox.setText(Tresor.getConfig().defaultServer());

        panel.addComponent(serverTextBox);

        TextBox usernameTextBox = new TextBox(inputSize);
        panel.addComponent(gui.getTextAsLabel("window.register.username", false));
        panel.addComponent(usernameTextBox);

        TextBox passwordTextBox = new TextBox(inputSize);
        panel.addComponent(gui.getTextAsLabel("window.register.password", false));
        panel.addComponent(passwordTextBox);
        passwordTextBox.setMask('*');

        Button registerButton = new Button(gui.getText("window.register.button", false), () -> {
            AuthenticationController.RegistrationResult result;
            int maxTries = 7; // just avoid spamming the server
            do {
                result = gui.getAuthenticationController().register(serverTextBox.getText(), usernameTextBox.getText(), passwordTextBox.getText());
            } while (switch (result) {
                case SUCCESS:
                    window.close();
                    yield false;
                case NOT_ALLOWED:
                    yield gui.showError("register.not_allowed");
                case API_ERROR:
                    yield gui.showError("register.api_error", true);
                default:
                    yield false;
            } && maxTries-- > 0);
        });

        panel.addComponent(new EmptySpace(inputSize));
        panel.addComponent(registerButton);

        return panel.withBorder(Borders.singleLine(gui.getText("window.register.title", false)));
    }


    private RegisterView() {
        throw new IllegalStateException("Utility class");
    }
}
