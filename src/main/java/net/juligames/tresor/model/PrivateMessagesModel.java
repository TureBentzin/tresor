package net.juligames.tresor.model;


import net.juligames.tresor.rest.*;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

import static net.juligames.tresor.rest.RESTCaller.call;
import static net.juligames.tresor.rest.RESTCaller.createURL;

/**
 * @author Ture Bentzin
 * @since 27-02-2025
 */
public class PrivateMessagesModel extends Model {

    public PrivateMessagesModel(@NotNull String host) {
        super(host);
    }

    public @NotNull ResponseContainer<GenericSuccess> sendMessage(@NotNull String jwt, @NotNull String message, @NotNull String recipient) {
        final URL url = createURL(getHost(), "/api/v1/user/pminbox/send");
        final PrivateMessage privateMessage = new PrivateMessage(message, recipient);

        //call REST API
        return call(url, jwt, RESTCaller.Method.POST, privateMessage, GenericSuccess.class);
    }
}
