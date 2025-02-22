package net.juligames.tresor.model;


import net.juligames.tresor.rest.Authentication;
import net.juligames.tresor.rest.JWTResponse;
import net.juligames.tresor.rest.ResponseContainer;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Objects;

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

    public @NotNull ResponseContainer<JWTResponse> authenticate(@NotNull String username, @NotNull String password) {
        final URL url = createURL(host, "/api/v1/auth/login");
        final Authentication authentication = new Authentication(username, password);

        //call REST API
        return callPublic(url, Method.POST, authentication, JWTResponse.class);
    }

}
