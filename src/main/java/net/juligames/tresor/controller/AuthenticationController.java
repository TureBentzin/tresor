package net.juligames.tresor.controller;


import net.juligames.tresor.Tresor;
import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.error.MissingAuthenticationException;
import net.juligames.tresor.model.AuthenticationModel;
import net.juligames.tresor.model.ConfigModel;
import net.juligames.tresor.rest.*;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static net.juligames.tresor.rest.ServersideProcessingError.handleErrors;

/**
 * @author Ture Bentzin
 * @since 19-02-2025
 */
@SuppressWarnings("ClassEscapesDefinedScope") //intended behaviour
public class AuthenticationController {

    private static final @NotNull Logger log = LoggerFactory.getLogger(AuthenticationController.class);
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
                case ERROR -> {
                    GenericError error = Objects.requireNonNull(jwt.getError());
                    gui.showError("auth.error", Map.of("error", error.error()));
                    yield AuthenticationResult.FAILURE;
                }
                case DIFFERENT_JSON -> AuthenticationResult.API_ERROR;
                case UNPROCESSABLE_ENTITY -> {
                    handleErrors(gui, Objects.requireNonNull(jwt.getUnprocessableEntity()));
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

            @NotNull ResponseContainer<GenericSuccess> register = authenticationModel.register(username, password);

            return switch (register.getResponseType()) {
                case RESPONSE -> {
                    GenericSuccess genericSuccess = Objects.requireNonNull(register.getResponse());
                    gui.showInfo("register.success", Map.of("status", String.valueOf(genericSuccess.status())));
                    yield RegistrationResult.SUCCESS;
                }
                case ERROR -> {
                    GenericError error = Objects.requireNonNull(register.getError());
                    gui.showError("register.error", Map.of("error", error.error()));
                    yield RegistrationResult.FAILURE;
                }
                case DIFFERENT_JSON -> RegistrationResult.API_ERROR;
                case UNPROCESSABLE_ENTITY -> {
                    handleErrors(gui, Objects.requireNonNull(register.getUnprocessableEntity()));
                    yield RegistrationResult.FAILURE;
                }
            };
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

    record Context(@NotNull String username, @NotNull String host, @NotNull String jwt) {
    }

    /**
     * This will only execute the action if the {@link AuthenticationController} thinks the current gui session is authenticated
     *
     * @param action to be executed
     * @param <R>    type of the result
     * @return result from the action
     * @throws MissingAuthenticationException thrown if not authenticated (dont need to be displayed to the user, as this is handled internally by this method)
     */
    public <R> @NotNull ResponseContainer<R> assertAuthenticated(@NotNull Function<Context, ResponseContainer<R>> action) throws MissingAuthenticationException {
        if (isAuthenticated()) {
            Context context = new Context(getUsername().orElseThrow(), getHost().orElseThrow(), getJwt().orElseThrow());
            return action.apply(context);
        } else {
            gui.showError("auth.not_authenticated");
            throw new MissingAuthenticationException(gui, getClass());
        }
    }

}
