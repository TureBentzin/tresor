package net.juligames.tresor.rest;


import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 22-02-2025
 */
public record JWTResponse(@NotNull String username, @NotNull String jwt) {
    public JWTResponse {
        if (username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (jwt.isEmpty()) {
            throw new IllegalArgumentException("JWT cannot be null or empty");
        }
    }

    public @NotNull String getUsername() {
        return username;
    }

    public @NotNull String getJwt() {
        return jwt;
    }
}
