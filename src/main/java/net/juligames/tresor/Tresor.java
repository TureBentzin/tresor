package net.juligames.tresor;


import com.googlecode.lanterna.bundle.LanternaThemes;
import com.googlecode.lanterna.graphics.PropertyTheme;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.Theme;
import com.googlecode.lanterna.terminal.ansi.TelnetTerminal;
import com.googlecode.lanterna.terminal.ansi.TelnetTerminalServer;
import net.juligames.tresor.lang.Translations;
import net.juligames.tresor.model.ConfigModel;
import net.juligames.tresor.model.DevConfig;
import net.juligames.tresor.theme.CustomThemeManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


/**
 * @author Ture Bentzin
 * @since 16-02-2025
 */
public class Tresor {

    private static final @NotNull Logger log = LoggerFactory.getLogger(Tresor.class);

    private static @Nullable ConfigModel config;

    public static @NotNull ConfigModel getConfig() {
        return Objects.requireNonNull(config, "Config not set");
    }


    public static void main(@NotNull String @NotNull [] args) throws IOException {
        config = DevConfig.getInstance();

        CustomThemeManager.findAndRegisterThemes();

        TelnetTerminalServer server = new TelnetTerminalServer(23);
        ServerSocket serverSocket = server.getServerSocket();
        log.info("Server started on {}:{}", serverSocket.getInetAddress(), serverSocket.getLocalPort());

        Translations.getAvailableMessageSets(); //load translations

        while (true) {
            log.info("Waiting for connection...");
            TelnetTerminal terminal = server.acceptConnection();
            new TresorGUI(terminal);
            log.info("Accepted connection from {}", terminal.getRemoteSocketAddress());
        }
    }

}