package net.juligames.tresor.model;


import net.juligames.tresor.rest.Authentication;
import org.jetbrains.annotations.NotNull;

import static net.juligames.tresor.rest.RESTCaller.*;

/**
 * @author Ture Bentzin
 * @since 22-02-2025
 */
public class AuthenticationModel {

    private final @NotNull String host;

    public AuthenticationModel(@NotNull String host) {
        this.host = host;

        //check if host is valid
        createURL(host);

    }

    public @NotNull String authenticate(@NotNull String username, @NotNull String password) {
        return callPublic(createURL(host, "/authenticate"), Method.POST, new Authentication(username, password), String.class);
    }
}
