package net.juligames.tresor.rest;


import net.juligames.tresor.TresorGUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

/**
 * @author Ture Bentzin
 * @since 22-02-2025
 */
public record ServersideProcessingError(String ctx, String loc, String msg, String type) {

    private static final @NotNull Logger log = LoggerFactory.getLogger(ServersideProcessingError.class);

    public static void handleErrors(@NotNull TresorGUI gui, @Nullable Set<ServersideProcessingError> errors) {
        if (errors == null) {
            log.warn("No errors to handle! This method should not be called with null");
            return;
        }
        for (ServersideProcessingError error : errors) {
            gui.showError("unprocessable_entity", Map.of(
                    "msg", error.msg(),
                    "ctx", error.ctx(),
                    "loc", error.loc(),
                    "type", error.type()
            ));
        }
    }
}
