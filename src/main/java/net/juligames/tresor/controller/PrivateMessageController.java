package net.juligames.tresor.controller;


import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.error.MissingAuthenticationException;
import net.juligames.tresor.model.PrivateMessagesModel;
import net.juligames.tresor.rest.EmptyBody;
import net.juligames.tresor.rest.GenericSuccess;
import net.juligames.tresor.rest.ResponseContainer;
import net.juligames.tresor.views.PrivateMessagesView;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

import static net.juligames.tresor.rest.ServersideProcessingError.handleErrors;

/**
 * @author Ture Bentzin
 * @since 27-02-2025
 */
public class PrivateMessageController extends AbstractController {

    private static final @NotNull Logger log = LoggerFactory.getLogger(PrivateMessageController.class);

    public PrivateMessageController(@NotNull TresorGUI gui) {
        super(gui);
    }

    public void sendMessage(@NotNull String message, @NotNull String recipient) throws MissingAuthenticationException {
        log.info("Sending message to \"{}\" with content: \"{}\"", recipient, message);

        ResponseContainer<GenericSuccess> response = gui.getAuthenticationController().assertAuthenticated(ctx -> {
            PrivateMessagesModel model = new PrivateMessagesModel(ctx.host());
            return model.sendMessage(ctx.jwt(), message, recipient);
        });

        switch (response.getResponseType()) {
            case RESPONSE:
                log.debug("Message sent successfully");
                break;
            case ERROR:
                gui.showError("message.send", Map.of(
                        "error", Objects.requireNonNull(response.getError()).error()
                ));
                break;
            case UNPROCESSABLE_ENTITY:
                handleErrors(gui, response.getUnprocessableEntity());
                break;
            case DIFFERENT_JSON:
                throw new IllegalArgumentException(response.getDifferentJson());
        }
    }
}
