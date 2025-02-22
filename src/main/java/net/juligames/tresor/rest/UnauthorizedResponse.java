package net.juligames.tresor.rest;


import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 22-02-2025
 */
public record UnauthorizedResponse(@NotNull String error) {
    public UnauthorizedResponse {
        if (error.isEmpty()) {
            throw new IllegalArgumentException("Error cannot be null or empty");
        }
    }

    public @NotNull String getError() {
        return error;
    }
}
