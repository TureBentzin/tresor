package net.juligames.tresor.model;


import net.juligames.tresor.rest.Authentication;
import net.juligames.tresor.rest.GenericSuccess;
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
public class AuthenticationModel extends Model {

    public AuthenticationModel(@NotNull String host) {
        super(host);
    }

    public @NotNull ResponseContainer<JWTResponse> authenticate(@NotNull String username, @NotNull String password) {
        final URL url = createURL(getHost(), "/api/v1/auth/login");
        final Authentication authentication = new Authentication(username, password);

        //call REST API
        return callPublic(url, Method.POST, authentication, JWTResponse.class);
    }


    public @NotNull ResponseContainer<GenericSuccess> register(@NotNull String username, @NotNull String password) {
        final URL url = createURL(getHost(), "/api/v1/auth/register");
        final Authentication authentication = new Authentication(username, password);

        //call REST API
        return callPublic(url, Method.POST, authentication, GenericSuccess.class);
    }

}
