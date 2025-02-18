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
import net.juligames.tresor.lang.Translations;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.Set;


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

        List<String> availableMessageSets = Translations.getAvailableMessageSets();
        //TODO provide message sets

        while (true) {
            log.info("Waiting for connection...");
            TelnetTerminal terminal = server.acceptConnection();
             new TresorGUI(terminal);
            log.info("Accepted connection from {}", terminal.getRemoteSocketAddress());
        }

        //screen.stopScreen();
    }
}