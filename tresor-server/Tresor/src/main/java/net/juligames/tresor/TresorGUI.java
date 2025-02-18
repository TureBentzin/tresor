package net.juligames.tresor;


import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.ansi.TelnetTerminal;
import net.juligames.tresor.lang.Translations;
import net.juligames.tresor.theme.BefatorTheme;
import net.juligames.tresor.views.DashboardView;
import net.juligames.tresor.views.SettingsView;
import net.juligames.tresor.views.common.Common;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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

    private boolean requestRegenerate = true;

    private @NotNull String messageSet = Translations.DEFAULT_SET;


    private final @NotNull ArrayBlockingQueue<Long> timestamps = new ArrayBlockingQueue<>(64);

    private static final @NotNull ThreadGroup threadGroup = new ThreadGroup("TresorGUI");

    public TresorGUI(@NotNull TelnetTerminal terminal) throws IOException {
        this.terminal = terminal;
        this.screen = new TerminalScreen(terminal);

        //execute handle asynchronously
        Thread thread = new Thread(threadGroup, () -> {
            try {
                handle();
            } catch (IOException e) {
                log.error("Error handling TelnetTerminal", e);
            }
        });

        thread.start();

    }

    public static @NotNull ThreadGroup getThreadGroup() {
        return threadGroup;
    }

    private void recordTimestamp(long timestamp) {
        if (timestamps.remainingCapacity() == 0) {
            timestamps.poll(); //ring buffer
        }
        timestamps.offer(timestamp);
    }

    private void handle() throws IOException {
        gui = new MultiWindowTextGUI(screen);
        screen.startScreen();
        try (terminal) {
            //gui.setTheme(new BefatorTheme());
            log.info("Starting a TresorGUI for {}", terminal.getRemoteSocketAddress());
            while (hasRequestRegenerate()) {
                {
                    //add Windows
                    gui.addWindow(DashboardView.getDashboardWindow(this));
                    gui.addWindow(SettingsView.getSettingsWindow(this));

                }
                requestRegenerate = false;
                gui.addWindowAndWait(DashboardView.getDashboardWindow(this));
                screen.clear();
            }




        } catch (SocketException e) {
            log.info("Connection closed!");
        } catch (Exception e) {
            log.error("Error handling TelnetTerminal", e);
        }
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

    public @NotNull String getTextWithParams(@NotNull String key, boolean tiny, @NotNull Map<String, String> params) {
        String message = getText(key, tiny);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            message = message.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return message;
    }


    public @NotNull String getMessageSet() {
        return messageSet;
    }

    public int calculateFPS() {
        long[] times = getTimestamps();
        int count = times.length;

        if (count < 2) {
            return 0;
        }

        long first = times[0];
        long last = times[count - 1];
        long duration = last - first;

        if (duration <= 0) {
            return 0;
        }

        return (int) ((count - 1) * 1000 / duration);
    }

    public void setMessageSet(@NotNull String messageSet) {
        if (Translations.getAvailableMessageSets().contains(messageSet)) {
            this.messageSet = messageSet;
        } else {
            log.warn("Message set {} not available", messageSet);
        }
    }

    //remove all cached elements, then create new ones
    public void regenerate() {
        Common.remove(this);
        DashboardView.remove(this);
        SettingsView.remove(this);
        requestRegenerate();
        getGui().getWindows().stream().toList().forEach(Window::close);

    }

    public boolean hasRequestRegenerate() {
        return requestRegenerate;
    }

    public void requestRegenerate() {
        this.requestRegenerate = true;
    }
}
