package net.juligames.tresor;


import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.ansi.TelnetTerminal;
import com.googlecode.lanterna.terminal.ansi.TelnetTerminalServer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * @author Ture Bentzin
 * @since 16-02-2025
 */
public class Tresor {

    private static final @NotNull Logger log = LoggerFactory.getLogger(Tresor.class);

    public static void main(@NotNull String @NotNull [] args) throws IOException {

        TelnetTerminalServer server = new TelnetTerminalServer(23);

        TelnetTerminal terminal = server.acceptConnection();
        Screen screen = new TerminalScreen(terminal);
        TextGraphics tGraphics = screen.newTextGraphics();

        log.info("Starting Tresor");

        screen.startScreen();
        screen.clear();

        TerminalPosition pos = new TerminalPosition(5, 5);
        TerminalSize size = new TerminalSize(3, 3);

        screen.refresh();

        loop:
        while (true) {

            tGraphics.drawRectangle(pos, size, '#');
            screen.refresh();

            KeyStroke keyStroke = screen.readInput();
            KeyType type = keyStroke.getKeyType();

            log.info("KeyStroke: {}", keyStroke);

            switch (type) {
                case Escape:
                    break loop;
                case ArrowUp:
                    pos = pos.withRelativeRow(-1);
                    break;
                case ArrowDown:
                    pos = pos.withRelativeRow(1);
                    break;
                case ArrowLeft:
                    pos = pos.withRelativeColumn(-1);
                    break;
                case ArrowRight:
                    pos = pos.withRelativeColumn(1);
                    break;
                default:
                    break;
            }
            screen.clear();
        }

        screen.stopScreen();
    }
}