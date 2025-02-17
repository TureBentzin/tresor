package net.juligames.tresor;


import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.ansi.TelnetTerminal;
import com.googlecode.lanterna.terminal.ansi.TelnetTerminalServer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;


/**
 * @author Ture Bentzin
 * @since 16-02-2025
 */
public class Tresor {

    private static final @NotNull Logger log = LoggerFactory.getLogger(Tresor.class);

    public static void main(@NotNull String @NotNull [] args) throws IOException {

        TelnetTerminalServer server = new TelnetTerminalServer(23);
        ServerSocket serverSocket = server.getServerSocket();
        log.info("Server started on {}:{}", serverSocket.getInetAddress(), serverSocket.getLocalPort());

        while (true) {
            log.info("Waiting for connection...");
            TelnetTerminal terminal = server.acceptConnection();
             new TresorGUI(terminal);

            /*
            TerminalScreen screen = new TerminalScreen(terminal);
            WindowBasedTextGUI gui = new MultiWindowTextGUI(screen);
            screen.startScreen();
            Window w = new BasicWindow("Hello");
            //w.setTheme(new SimpleTheme(TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK));
            w.setComponent(new com.googlecode.lanterna.gui2.Label("Hello, World!"));


            gui.addWindowAndWait(w);

             */

            log.info("Accepted connection from {}", terminal.getRemoteSocketAddress());
        }

        //screen.stopScreen();
    }
}