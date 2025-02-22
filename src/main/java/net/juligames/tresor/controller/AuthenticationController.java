package net.juligames.tresor.controller;


import net.juligames.tresor.Tresor;
import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.model.AuthenticationModel;
import net.juligames.tresor.model.ConfigModel;
import net.juligames.tresor.rest.*;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Ture Bentzin
 * @since 19-02-2025
 */
@SuppressWarnings("ClassEscapesDefinedScope") //intended behaviour
public class AuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final @NotNull TresorGUI gui;

    private @Nullable String username = null;
    private @Nullable String jwt = null;
    private @Nullable String host = null;

    /// MODELS ///

    private @Nullable AuthenticationModel authenticationModel = null;

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

    public enum RegistrationResult {
        SUCCESS,
        USER_ALREADY_EXISTS,
        FAILURE,
        NOT_ALLOWED,
        API_ERROR
    }

    @Blocking
    public @NotNull AuthenticationResult authenticate(@NotNull String host, @NotNull String username, @NotNull String password) {
        try {
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

            AuthenticationModel authenticationModel = getOrOverrideAuthenticationModel(host);

            @NotNull ResponseContainer<JWTResponse> jwt = authenticationModel.authenticate(username, password);

            return switch (jwt.getResponseType()) {
                case RESPONSE -> {
                    JWTResponse jwtResponse = Objects.requireNonNull(jwt.getResponse());
                    this.jwt = jwtResponse.getJwt();
                    this.username = jwtResponse.getUsername();
                    yield AuthenticationResult.SUCCESS;
                }
                case UNAUTHORIZED -> {
                    UnauthorizedResponse unauthorizedResponse = Objects.requireNonNull(jwt.getUnauthorizedResponse());
                    gui.showError("auth.unauthorized", Map.of("error", unauthorizedResponse.error()));
                    yield AuthenticationResult.FAILURE;
                }
                case DIFFERENT_JSON -> {
                    String differentJson = Objects.requireNonNull(jwt.getDifferentJson());
                    log.error("Different JSON: {}", differentJson);
                    yield AuthenticationResult.API_ERROR;
                }
                case UNPROCESSABLE_ENTITY -> {
                    UnprocessableEntity unprocessableEntity = Objects.requireNonNull(jwt.getUnprocessableEntity());
                    gui.showError("unprocessable_entity", Map.of(
                            "msg", unprocessableEntity.msg(),
                            "ctx", unprocessableEntity.ctx(),
                            "loc", unprocessableEntity.loc(),
                            "type", unprocessableEntity.type()
                    ));
                    yield AuthenticationResult.FAILURE;
                }
            };
        } catch (Exception e) {
            log.error("Error during authentication", e);
            return AuthenticationResult.API_ERROR;
        }
    }

    @Blocking
    public @NotNull RegistrationResult register(@NotNull String host, @NotNull String username, @NotNull String password) {
        try {
            final ConfigModel config = Tresor.getConfig();
            if (config.getServerBlacklistRegex().stream().anyMatch(host::matches)) {
                return RegistrationResult.NOT_ALLOWED;
            }

            if (!config.getServerWhitelistRegex().isEmpty() && config.getServerWhitelistRegex().stream().noneMatch(host::matches)) {
                return RegistrationResult.NOT_ALLOWED;
            }

            if (username.isEmpty()) {
                return RegistrationResult.FAILURE;
            }

            if (password.isEmpty()) {
                return RegistrationResult.FAILURE;
            }

            AuthenticationModel authenticationModel = getOrOverrideAuthenticationModel(host);


            //TODO
            return RegistrationResult.SUCCESS;
        } catch (Exception e) {
            log.error("Error during registration", e);
            return RegistrationResult.API_ERROR;
        }
    }


    /**
     * FOR DISPLAY PURPOSES ONLY
     *
     * @return stored username
     */
    public @NotNull Optional<String> getUsername() {
        return Optional.ofNullable(username);
    }

    public @NotNull Optional<String> getHost() {
        return Optional.ofNullable(host);
    }

    private @NotNull AuthenticationModel getAuthenticationModel() {
        return Objects.requireNonNull(authenticationModel, "AuthenticationModel not yet initialized");
    }

    @ApiStatus.Internal
    public void logout() {
        username = null;
        jwt = null;
    }

    private @NotNull AuthenticationModel getOrOverrideAuthenticationModel(@NotNull String host) {
        if (authenticationModel == null || !host.equals(this.host)) {
            this.host = host;
            authenticationModel = new AuthenticationModel(host);
        }
        return authenticationModel;
    }

    public boolean isAuthenticated() {
        return username != null && jwt != null;
    }

}
