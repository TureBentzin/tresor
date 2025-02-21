package net.juligames.tresor;


import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.ansi.TelnetTerminal;
import net.juligames.tresor.controller.AuthenticationController;
import net.juligames.tresor.controller.BankingController;
import net.juligames.tresor.lang.Translations;
import net.juligames.tresor.utils.SecureRunnableRunner;
import net.juligames.tresor.utils.TresorExceptionHandler;
import net.juligames.tresor.views.DashboardView;
import net.juligames.tresor.views.DefaultWindow;
import net.juligames.tresor.views.SettingsView;
import net.juligames.tresor.views.TresorWindow;
import net.juligames.tresor.views.common.Common;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Async;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author Ture Bentzin
 * @since 17-02-2025
 */
public final class TresorGUI {

    private static final @NotNull Logger log = LoggerFactory.getLogger(TresorGUI.class);


    private final @NotNull TelnetTerminal terminal;
    private final @NotNull Screen screen;
    private @Nullable WindowBasedTextGUI gui;
    private @NotNull String messageSet = Translations.DEFAULT_SET;

    /**
     * Static windows are NOT CLOSED when switching with {@link #switchWindow(Window)}
     */
    private @NotNull Set<Window> staticWindows = new HashSet<>();

    private final @NotNull ArrayBlockingQueue<Long> timestamps = new ArrayBlockingQueue<>(64);

    private final @NotNull AuthenticationController authenticationController;
    private final @NotNull BankingController bankingController;

    public TresorGUI(@NotNull TelnetTerminal terminal) throws IOException {
        this.terminal = terminal;
        this.screen = new TerminalScreen(terminal);
        authenticationController = new AuthenticationController(this);
        bankingController = new BankingController(this);

        handle();
    }

    private void handle() throws IOException {
        terminal.maximize();
        gui = new MultiWindowTextGUI(new SeparateTextGUIThread.Factory(), screen);
        gui.addListener((textGUI, keyStroke) -> {
            log.debug("unhandled key event: {}", keyStroke);
            return false;
        });

        screen.startScreen();
        log.info("Starting a TresorGUI for {}", terminal.getRemoteSocketAddress());

        DefaultWindow defaultWindow = new DefaultWindow(this);

        staticWindows.add(defaultWindow);
        gui.addWindow(defaultWindow);

        AsynchronousTextGUIThread guiThread = (AsynchronousTextGUIThread) gui.getGUIThread();
        guiThread.setExceptionHandler(new TresorExceptionHandler(this));

        guiThread.start();

    }


    public @NotNull Screen getScreen() {
        return screen;
    }

    public @NotNull TelnetTerminal getTerminal() {
        return terminal;
    }


    public long @NotNull [] getTimestamps() {
        return timestamps.stream().mapToLong(Long::longValue).toArray();
    }

    public @NotNull WindowBasedTextGUI getGui() {
        return Objects.requireNonNull(gui, "GUI not initialized");
    }

    public @NotNull String getText(@NotNull String key, boolean tiny) {
        return Translations.getMessage(key, messageSet, tiny);
    }

    public @NotNull Label getTextAsLabel(@NotNull String key, boolean tiny) {
        return new Label(getText(key, tiny));
    }

    public @NotNull String getTextWithParams(@NotNull String key, boolean tiny, @NotNull Map<String, String> params) {
        String message = getText(key, tiny);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            message = message.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return message;
    }

    public @NotNull Label getTextWithParamsAsLabel(@NotNull String key, boolean tiny, @NotNull Map<String, String> params) {
        return new Label(getTextWithParams(key, tiny, params));
    }


    public @NotNull String getMessageSet() {
        return messageSet;
    }

    public void setMessageSet(@NotNull String messageSet) {
        if (Translations.getAvailableMessageSets().contains(messageSet)) {
            this.messageSet = messageSet;
        } else {
            log.warn("Message set {} not available", messageSet);
        }
    }

    public @NotNull AuthenticationController getAuthenticationController() {
        return authenticationController;
    }

    public boolean showError(@NotNull String errorKey) {
        return showError(errorKey, Map.of());
    }

    public boolean showError(@NotNull String errorKey, @NotNull Map<String, String> params) {
        return showError(errorKey, params, false);
    }

    public boolean showError(@NotNull String errorKey, boolean allowRetry) {
        return showError(errorKey, Map.of(), allowRetry);
    }

    public boolean showError(@NotNull String errorKey, @NotNull Map<String, String> params, boolean allowRetry) {
        final String title = getText("window.error.title", false);
        final String message = getTextWithParams("window.error." + errorKey, false, params);

        MessageDialogBuilder messageDialogBuilder = new MessageDialogBuilder().setTitle(title)
                .setText(message)
                .addButton(MessageDialogButton.Abort);
        if (allowRetry) {
            messageDialogBuilder.addButton(MessageDialogButton.Retry);
        }

        MessageDialog messageDialog = messageDialogBuilder.build();
        MessageDialogButton messageDialogButton = messageDialog.showDialog(getGui());

        return switch (messageDialogButton) {
            case Abort:
                yield false;
            case Retry:
                yield true;
            default:
                throw new IllegalStateException("Unexpected value: " + messageDialogButton);
        };
    }

    public @NotNull BankingController getBankingController() {
        return bankingController;
    }

    /**
     * Closes the old window if it is not static and adds the new window to the GUI
     *
     * @param window the new window
     */
    public void switchWindow(@NotNull Window window) {
        MultiWindowTextGUI gui = (MultiWindowTextGUI) getGui();

        if (!(window instanceof TresorWindow twin && !twin.isPermanent()))
            while (gui.getActiveWindow() != null) {
                if (gui.getActiveWindow() instanceof DefaultWindow) {
                    break;
                }
                gui.getActiveWindow().close();
            }
        gui.addWindow(window);
    }
}
