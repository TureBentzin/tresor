package net.juligames.tresor.rest;


import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 21-02-2025
 */
public record GenericError(String error, int status) {
    public GenericError {
        if (error == null || error.isEmpty()) {
            throw new IllegalArgumentException("Error cannot be null or empty");
        }
        if (status < 400 || status > 599) {
            throw new IllegalArgumentException("Status must be between 400 and 599");
        }
    }

    public GenericError(@NotNull String error) {
        this(error, 500);
    }
}
