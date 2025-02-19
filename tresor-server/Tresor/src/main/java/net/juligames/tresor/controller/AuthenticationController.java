package net.juligames.tresor.controller;


import net.juligames.tresor.Tresor;
import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.model.ConfigModel;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * @author Ture Bentzin
 * @since 19-02-2025
 */
@SuppressWarnings("ClassEscapesDefinedScope") //intended behaviour
public class AuthenticationController {

    private final @NotNull TresorGUI gui;

    private @Nullable String username = null;
    private @Nullable String jwt = null;

    public AuthenticationController(@NotNull TresorGUI gui) {
        this.gui = gui;
    }

    public int getJwtFingerprint() {
        return Objects.hash(jwt);
    }

    protected @NotNull Optional<String> getJwt() {
        return Optional.ofNullable(jwt);
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

        this.username = username;
        //TODO
        this.jwt = "jwt";
        return AuthenticationResult.SUCCESS;
    }


    /**
     * FOR DISPLAY PURPOSES ONLY
     *
     * @return stored username
     */
    public @NotNull Optional<String> getUsername() {
        return Optional.ofNullable(username);
    }

    @ApiStatus.Internal
    public void logout() {
        username = null;
        jwt = null;
    }

    public boolean isAuthenticated() {
        return username != null && jwt != null;
    }

}
