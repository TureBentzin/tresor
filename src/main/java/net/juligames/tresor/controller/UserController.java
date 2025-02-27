package net.juligames.tresor.controller;


import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.error.MissingAuthenticationException;
import net.juligames.tresor.model.UserModel;
import net.juligames.tresor.rest.BalanceResponse;
import net.juligames.tresor.rest.ResponseContainer;
import net.juligames.tresor.rest.ValidUsernames;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Set;

/**
 * @author Ture Bentzin
 * @since 26-02-2025
 */
public class UserController {

    private static final @NotNull Logger log = LoggerFactory.getLogger(UserController.class);
    private final @NotNull TresorGUI gui;

    public UserController(@NotNull TresorGUI gui) {
        this.gui = gui;
    }

    public @Range(from = 0, to = Integer.MAX_VALUE) int getBalance() throws MissingAuthenticationException {
        ResponseContainer<BalanceResponse> response = gui.getAuthenticationController().assertAuthenticated(ctx -> {
            //call REST API
            log.info("Getting balance for user: {}", ctx.username());
            UserModel model = new UserModel(ctx.host());
            return model.getBalance(ctx.jwt());
        });

        return switch (response.getResponseType()) {
            case RESPONSE:
                yield Objects.requireNonNull(response.getResponse()).balance();
            case ERROR:
                throw new IllegalArgumentException("Error: " + response.getError());
            case UNPROCESSABLE_ENTITY:
                throw new IllegalArgumentException("Unprocessable entity: " + response.getUnprocessableEntity());
            case DIFFERENT_JSON:
                throw new IllegalArgumentException("Different JSON: " + response.getDifferentJson());
        };
    }

    public @NotNull @Unmodifiable Set<String> getValidUsernames() throws MissingAuthenticationException {
        ResponseContainer<ValidUsernames> response = gui.getAuthenticationController().assertAuthenticated(ctx -> {
            //call REST API
            log.info("Getting valid usernames for user: {}", ctx.username());
            UserModel model = new UserModel(ctx.host());
            return model.getValidRecipients(ctx.jwt());
        });

        return switch (response.getResponseType()) {
            case RESPONSE:
                yield Objects.requireNonNull(response.getResponse()).usernames();
            case ERROR:
                throw new IllegalArgumentException("Error: " + response.getError());
            case UNPROCESSABLE_ENTITY:
                throw new IllegalArgumentException("Unprocessable entity: " + response.getUnprocessableEntity());
            case DIFFERENT_JSON:
                throw new IllegalArgumentException("Different JSON: " + response.getDifferentJson());
        };
    }
}
