package net.juligames.tresor;


import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.ansi.TelnetTerminal;
import net.juligames.tresor.lang.Translations;
import net.juligames.tresor.theme.BefatorTheme;
import net.juligames.tresor.views.DashboardView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
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

    private @NotNull Map<String, String> messageSet = new HashMap<>();


    private final @NotNull ArrayBlockingQueue<Long> timestamps = new ArrayBlockingQueue<>(64);

    private static final @NotNull ThreadGroup threadGroup = new ThreadGroup("TresorGUI");

    public TresorGUI(@NotNull TelnetTerminal terminal) throws IOException {
        this.terminal = terminal;
        this.screen = new TerminalScreen(terminal);
        messageSet = Translations.getDefaultMessageSet();

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
            gui.addWindowAndWait(DashboardView.getDashboardWindow(this));


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
        return gui;
    }

    public @NotNull String getText(@NotNull String key, boolean tiny) {
        return messageSet.getOrDefault(key, (tiny ? "?" : "?" + key + "?"));
    }

    public @NotNull String getTextWithParams(@NotNull String key, boolean tiny, @NotNull Map<String, String> params) {
        String orDefault = messageSet.getOrDefault(key, (tiny ? "?" : "?" + key + "?"));
        for (Map.Entry<String, String> entry : params.entrySet()) {
            orDefault = orDefault.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return orDefault;
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
}
