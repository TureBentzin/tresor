package net.juligames.tresor;


import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.ansi.TelnetTerminal;
import net.juligames.tresor.elements.ApplicationFrame;
import net.juligames.tresor.elements.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;
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
    private final @NotNull TextGraphics textGraphics;
    private @NotNull TerminalSize terminalSize;

    private @NotNull ApplicationFrame applicationFrame = new ApplicationFrame();

    private @NotNull Theme theme = Theme.DEFAULT;

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
            textGraphics.setBackgroundColor(theme.backgroundColor());
            textGraphics.setForegroundColor(theme.highlightForegroundColor());
            loop:
            while (true) {
                {

                    @Nullable TerminalSize newSize = screen.doResizeIfNecessary();
                    if (newSize != null) {
                        terminalSize = newSize;
                        log.info("Terminal resized to {}", terminalSize);
                    }

                    KeyStroke keyStroke = terminal.pollInput();
                    if (keyStroke != null) {
                        if (Objects.requireNonNull(keyStroke.getKeyType()) == KeyType.Escape) {
                            break loop;
                        }

                        //TODO propper button mapping for navigation (this is demo only)
                        {
                            KeyType keyType = keyStroke.getKeyType();
                            if (keyType == KeyType.ArrowLeft) {
                                activeMenu = Math.max(0, activeMenu - 1);
                            } else if (keyType == KeyType.ArrowRight) {
                                activeMenu = Math.min(3, activeMenu + 1);
                            }
                        }
                    }

                }

                //fill the screen with spaces
                textGraphics.fill(' ');

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
        //display size
        textGraphics.putString(3, 3, "Terminal Size: " + terminalSize.getColumns() + "x" + terminalSize.getRows());

        drawApplicationFrame();
        drawHeader();
        drawMenuBar();

        drawPopUp("ALERT!", "PopUp Message goes here...");

        drawFooter();


        return true;
    }

    private void drawApplicationFrame() {
        int width = terminalSize.getColumns();
        int height = terminalSize.getRows();
        textGraphics.setBackgroundColor(theme.borderColor());
        textGraphics.drawLine(2, 2, width - 2, 2, theme.borderHorizontal());
        textGraphics.drawLine(2, height - 2, width - 2, height - 2, theme.borderHorizontal());
        textGraphics.drawLine(2, 2, 2, height - 2, theme.borderVertical());
        textGraphics.drawLine(width - 2, 2, width - 2, height - 2, theme.borderVertical());

        textGraphics.setBackgroundColor(theme.backgroundColor());
    }

    // TRESOR GUI - MOTD (centerd) - VERSION 1.0

    private void drawHeader() {
        String motd = "Testwert - Dynamic";
        String version = "Version 1.0";
        String fps = "FPS: " + calculateFPS();
        String brand = "TresorGUI";

        int width = terminalSize.getColumns();

        int x = (width - motd.length()) / 2;

        int sum = motd.length() + version.length() + brand.length() + fps.length() + 4;

        if (sum > width) {
            if (motd.length() <= width) {
                textGraphics.putString(1, 1, motd);
            } else if (brand.length() <= width) {
                textGraphics.putString(1, 1, brand);
            }
            return;
        }

        textGraphics.putString(x, 1, motd);

        // Draw the version number

        textGraphics.putString((width - version.length()) - 1, 1, version);

        // Draw the brand
        textGraphics.putString(2, 1, brand);
    }

    private void drawFooter() {
        String footer = "Press ESC to exit";
        int width = terminalSize.getColumns();
        int height = terminalSize.getRows();

        if (footer.length() > width) {
            return;
        }

        int x = (width - footer.length()) / 2;
        textGraphics.putString(x, height - 1, footer);
    }

    private int activeMenu = 0;

    private boolean drawMenuBar() {
        List<String> menuItems = List.of("Acount", "Messages", "About", "Logout");

        int width = terminalSize.getColumns();
        int spacing = 2;
        int totalWidth = menuItems.stream().mapToInt(String::length).sum() + (menuItems.size() - 1) * spacing;
        if (menuItems.size() * spacing + totalWidth > width) {
            return false;
        }

        int x = 3;
        int y = 3;

        for (int i = 0; i < menuItems.size(); i++) {
            String menuItem = menuItems.get(i);
            if (i == activeMenu) {
                textGraphics.setBackgroundColor(theme.highlightBackgroundColor());
                textGraphics.setForegroundColor(theme.highlightForegroundColor());
            } else {
                textGraphics.setBackgroundColor(theme.backgroundColor());
                textGraphics.setForegroundColor(theme.foregroundColor());
            }
            textGraphics.putString(x, y, menuItem);
            textGraphics.setBackgroundColor(theme.backgroundColor());
            textGraphics.setForegroundColor(theme.foregroundColor());
            //print menu spacing
            x += menuItem.length();
            textGraphics.putString(x, y, " ".repeat(spacing));
            x += spacing;
        }


        return true;
    }

    private boolean drawPopUp(@NotNull String title, @NotNull String message) {
        int width = terminalSize.getColumns();
        int height = terminalSize.getRows();


        int y = (height - 5) / 2;
        int requiredWidth = Math.max(title.length(), message.length()) + 2;
        if (requiredWidth > width) {
            return false;
        }
        int x = (width - requiredWidth) / 2;
        int titleX = (width - title.length()) / 2;

        int requiredHeight = 5;

        if (y + requiredHeight > height) {
            return false;
        }

        textGraphics.putString(titleX, y, title);
        textGraphics.putString(x, y + 1, message);

        textGraphics.setBackgroundColor(theme.highlightBackgroundColor());
        textGraphics.setForegroundColor(theme.highlightForegroundColor());
        textGraphics.drawLine(x - 1, y - 1, x + requiredWidth, y - 1, theme.borderHorizontal());
        textGraphics.drawLine(x - 1, y - 1, x - 1, y + 3, theme.borderVertical());
        textGraphics.drawLine(x - 1, y + 3, x + requiredWidth, y + 3, theme.borderHorizontal());
        textGraphics.drawLine(x + requiredWidth, y - 1, x + requiredWidth, y + 3, theme.borderVertical());

        textGraphics.drawLine(x - 1, y + 4, x + requiredWidth, y + 4, theme.borderHorizontal());
        textGraphics.putString(x, y + 4, "OK");


        textGraphics.setBackgroundColor(theme.backgroundColor());
        textGraphics.setForegroundColor(theme.foregroundColor());

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

    /**
     * Never hold this object for longer then a single draw cycle.
     *
     * @return the current theme
     */
    public @NotNull Theme getTheme() {
        return theme;
    }

    public void setTheme(@NotNull Theme theme) {
        this.theme = theme;
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
