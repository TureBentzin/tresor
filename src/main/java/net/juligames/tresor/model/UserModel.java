package net.juligames.tresor.model;


import net.juligames.tresor.rest.BalanceResponse;
import net.juligames.tresor.rest.ResponseContainer;
import net.juligames.tresor.rest.ValidUsernames;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Objects;
import java.util.Set;

import static net.juligames.tresor.rest.RESTCaller.*;

/**
 * @author Ture Bentzin
 * @since 26-02-2025
 */
public class UserModel extends Model {
    public UserModel(@NotNull String host) {
        super(host);
    }

    public @NotNull ResponseContainer<BalanceResponse> getBalance(@NotNull String jwt) {
        final URL url = createURL(getHost(), "/api/v1/user/balance");

        return call(url, jwt, Method.GET, null, BalanceResponse.class);
    }

    public @NotNull ResponseContainer<ValidUsernames> getValidRecipients(@NotNull String jwt) {
        final URL url = createURL(getHost(), "/api/v1/user/valid_recipients");

        //TODO
        Set<String> usernames = Set.of("tdr1234", "test", "bommels", "befator");
        return ResponseContainer.successful(new ValidUsernames(usernames));
    }

}
