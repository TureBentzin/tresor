package net.juligames.tresor.controller;


import net.juligames.tresor.Tresor;
import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.model.ConfigModel;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 19-02-2025
 */
@SuppressWarnings("ClassEscapesDefinedScope") //intended behaviour
public class AuthenticationController {

    private final @NotNull TresorGUI gui;

    public AuthenticationController(@NotNull TresorGUI gui) {
        this.gui = gui;
    }

    public enum AuthenticationResult {
        SUCCESS,
        USER_NOT_FOUND,
        FAILURE,
        NOT_ALLOWED,
        API_ERROR
    }

    public @NotNull AuthenticationResult authenticate(@NotNull String host, @NotNull String username, @NotNull String password) {
        final ConfigModel config = Tresor.getConfig();

        if (config.getServerBlacklistRegex().stream().anyMatch(host::matches)) {
            return AuthenticationResult.NOT_ALLOWED;
        }

        if (!config.getServerWhitelistRegex().isEmpty() && config.getServerWhitelistRegex().stream().noneMatch(host::matches)) {
            return AuthenticationResult.NOT_ALLOWED;
        }

        if (username.isEmpty()) {
            return AuthenticationResult.USER_NOT_FOUND;
        }

        if (password.isEmpty()) {
            return AuthenticationResult.FAILURE;
        }

        //TODO
        return AuthenticationResult.SUCCESS;
    }

}
