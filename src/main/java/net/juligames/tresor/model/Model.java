package net.juligames.tresor.model;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ture Bentzin
 * @since 26-02-2025
 */
public abstract class Model {

    protected static final @NotNull Logger log = LoggerFactory.getLogger(Model.class);


    private final @NotNull String host;

    public Model(@NotNull String host) {
        this.host = host;
        if (host.isEmpty()) {
            throw new IllegalArgumentException("Host cannot be null or empty");
        }

        log.debug("Creating model for host: " + host);
    }

    public @NotNull String getHost() {
        return host;
    }


}
