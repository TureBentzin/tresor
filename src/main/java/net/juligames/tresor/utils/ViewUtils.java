package net.juligames.tresor.utils;


import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.views.LoginView;
import net.juligames.tresor.views.TresorWindow;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * @author Ture Bentzin
 * @since 27-02-2025
 */
public class ViewUtils {

    private ViewUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final @NotNull String WINDOW_KEY_PREFIX = "window.";

    public static @NotNull TresorWindow authenticatedWindow(@NotNull TresorGUI gui, @NotNull String windowKey, @NotNull Consumer<TresorWindow> consumer) {
        TresorWindow window = new TresorWindow(gui, WINDOW_KEY_PREFIX + windowKey);

        if (gui.getAuthenticationController().isAuthenticated()) {
            consumer.accept(window);
        } else {
            Label label = gui.getTextAsLabel("window.common.not_authed", false);
            label.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

            window.getContentPanel().addComponent(new EmptySpace(TerminalSize.ONE));
            window.getContentPanel().addComponent(label);

            Button button = new Button(gui.getText("window.common.login", false), () -> {
                gui.getGui().addWindowAndWait(LoginView.getLoginWindow(gui));
            });
            button.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
            window.getContentPanel().addComponent(button);
        }

        return window;
    }
}
