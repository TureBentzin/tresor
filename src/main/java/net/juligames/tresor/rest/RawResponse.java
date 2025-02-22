package net.juligames.tresor.rest;


import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 22-02-2025
 */
public record RawResponse(@NotNull String response, int statusCode) {
    public RawResponse {
        if (response.isEmpty()) {
            throw new IllegalArgumentException("Response cannot be null or empty");
        }
    }

    public @NotNull String getResponse() {
        return response;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public boolean isSuccessful() {
        return statusCode >= 200 && statusCode < 300;
    }

    public boolean isUnauthorized() {
        return statusCode == 401;
    }

}
