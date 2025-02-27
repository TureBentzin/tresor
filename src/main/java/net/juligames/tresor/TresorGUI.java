package net.juligames.tresor;


import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.ansi.TelnetTerminal;
import net.juligames.tresor.controller.AuthenticationController;
import net.juligames.tresor.controller.BankingController;
import net.juligames.tresor.controller.UserController;
import net.juligames.tresor.lang.Translations;
import net.juligames.tresor.model.ProjectPropertiesUtil;
import net.juligames.tresor.theme.CustomThemeManager;
import net.juligames.tresor.utils.SecureRunnableRunner;
import net.juligames.tresor.utils.TresorExceptionHandler;
import net.juligames.tresor.views.*;
import net.juligames.tresor.views.common.Common;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Async;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
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
    private final @NotNull UserController userController;

    public TresorGUI(@NotNull TelnetTerminal terminal) throws IOException {
        this.terminal = terminal;
        this.screen = new TerminalScreen(terminal);
        authenticationController = new AuthenticationController(this);
        bankingController = new BankingController(this);
        userController = new UserController(this);

        handle();
    }

    private void handle() throws IOException {
        terminal.maximize();
        gui = new MultiWindowTextGUI(new SeparateTextGUIThread.Factory(), screen);
        gui.setTheme(CustomThemeManager.getRegisteredTheme("blaster"));
        gui.addListener((textGUI, keyStroke) -> {
            if (keyStroke.isCtrlDown() && keyStroke.getCharacter() == 'c') {
                quit();
                return true;
            }
            if (keyStroke.getKeyType().equals(KeyType.F1)) {
                switchWindow(DashboardView.getDashboardWindow(this));
                return true;
            }

            if (keyStroke.getKeyType().equals(KeyType.F2)) {
                switchWindow(PrivateMessagesView.getPrivateMessagesWindow(this));
                return true;
            }
            if (keyStroke.getKeyType().equals(KeyType.F3)) {
                switchWindow(SettingsView.getSettingsWindow(this));
                return true;
            }
            if (keyStroke.getKeyType().equals(KeyType.F5)) {
                log.info("Refreshing...");
                gui.getActiveWindow().invalidate();
                return true;
            }
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
        try {
            terminal.bell();
        } catch (IOException ignored) {
        }
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


    public void showInfo(@NotNull String infoKey, @NotNull Map<String, String> params) {
        final String title = getText("window.popup.title", false);
        final String message = getTextWithParams("window.popup." + infoKey, false, params);
        new MessageDialogBuilder().setTitle(title)
                .setText(message)
                .addButton(MessageDialogButton.Close)
                .build().showDialog(getGui());
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

        List<Window> oldWindows = List.copyOf(gui.getWindows());

        for (Window oldWindow : oldWindows) {
            if (oldWindow instanceof DefaultWindow) {
                continue;
            }
            if (!staticWindows.contains(oldWindow)) {
                oldWindow.close();
            }
        }

        gui.addWindow(window);
    }

    public void resetTelnetView() {
        MultiWindowTextGUI gui = (MultiWindowTextGUI) getGui();

        List<Window> oldWindows = List.copyOf(gui.getWindows());

        for (Window oldWindow : oldWindows) {
            oldWindow.close();
        }

        DefaultWindow defaultWindow = new DefaultWindow(this);
        staticWindows.add(defaultWindow);
        gui.addWindow(defaultWindow);
    }


    public void quit() {
        Thread thread = new Thread(this::quitBlocking);
        thread.start();
    }

    private void quitBlocking() {

        AsynchronousTextGUIThread guiThread = (AsynchronousTextGUIThread) getGui().getGUIThread();
        guiThread.stop();
        log.info("Stopping TresorGUI for {}", terminal.getRemoteSocketAddress());
        try {
            guiThread.waitForStop();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            terminal.setTitle("Tresor - Quitting");
            terminal.setBackgroundColor(TextColor.ANSI.BLACK);
            terminal.setForegroundColor(TextColor.ANSI.WHITE);

            screen.clear();
            screen.close();

            terminal.clearScreen();
            getGui().getWindows().forEach(Window::close);

            String quitMessage = getText("app.goodbye", false);
            String brand = ProjectPropertiesUtil.getArtifactId();
            terminal.putString(brand + "> " + quitMessage + "\n");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            terminal.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public @NotNull UserController getUserController() {
        return userController;
    }
}
