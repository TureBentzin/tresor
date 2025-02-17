package net.juligames.tresor;


import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.ansi.TelnetTerminal;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author Ture Bentzin
 * @since 17-02-2025
 */
public final class TresorGUI {

    private static final @NotNull Logger log = LoggerFactory.getLogger(TresorGUI.class);

    private final @NotNull TelnetTerminal terminal;
    private final @NotNull Screen screen;
    private final @NotNull TextGraphics textGraphics;
    private @NotNull TerminalSize terminalSize;


    private final @NotNull ArrayBlockingQueue<Long> timestamps = new ArrayBlockingQueue<>(64);

    private static final @NotNull ThreadGroup threadGroup = new ThreadGroup("TresorGUI");

    public TresorGUI(@NotNull TelnetTerminal terminal) throws IOException {
        this.terminal = terminal;
        this.screen = new TerminalScreen(terminal);
        this.textGraphics = screen.newTextGraphics();
        terminalSize = terminal.getTerminalSize();

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
        try (terminal) {
            log.info("Starting a TresorGUI for {}", terminal.getRemoteSocketAddress());

            terminal.setCursorVisible(false);


            screen.startScreen();
            terminal.setTitle("TresorGUI");

            try (terminal) {
                applicationPane();
                recordTimestamp(System.currentTimeMillis());
                screen.refresh();
            }
        } catch (SocketException e) {
            log.info("Connection closed!");
        } catch (Exception e) {
            log.error("Error handling TelnetTerminal", e);
        }
    }

    /*
     * Application Pane
     *
     * This method draws the application pane. (MOTD, Menu, etc.)
     *
     *
     * returns true if the application pane was drawn successfully. false if not (e.g. the terminal is too small)
     */
    private boolean applicationPane() {
        MultiWindowTextGUI gui = new MultiWindowTextGUI(screen);

        Window window = new BasicWindow("TresorGUI");
        window.setTheme(new SimpleTheme(TextColor.ANSI.BLACK, TextColor.ANSI.YELLOW_BRIGHT));
        window.setHints(List.of(Window.Hint.FULL_SCREEN));

        Panel panel = new Panel(new GridLayout(2));

        Label title = new Label("Befator Inc. grüßt Sie!");
        title.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.BEGINNING,
                true,
                false,
                2,
                1));
        panel.addComponent(title);

        Button button = new Button("Button 1", () -> MessageDialog.showMessageDialog(gui, "Button 1", "Button 1 was pressed"));
        button.setTheme(new SimpleTheme(TextColor.ANSI.WHITE, TextColor.ANSI.BLUE));
        panel.addComponent(button, GridLayout.createLayoutData(GridLayout.Alignment.CENTER, GridLayout.Alignment.CENTER));

        window.setComponent(panel);

        gui.addWindowAndWait(window);

        return true;
    }


    public @NotNull Screen getScreen() {
        return screen;
    }

    public @NotNull TelnetTerminal getTerminal() {
        return terminal;
    }

    public @NotNull TextGraphics getTextGraphics() {
        return textGraphics;
    }

    public long @NotNull [] getTimestamps() {
        return timestamps.stream().mapToLong(Long::longValue).toArray();
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
