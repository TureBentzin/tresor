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

    public boolean isClientError() {
        return statusCode >= 400 && statusCode < 500;
    }

    public boolean isServerError() {
        return statusCode >= 500 && statusCode < 600;
    }

    public boolean isInformational() {
        return statusCode >= 100 && statusCode < 200;
    }

    public boolean isRedirection() {
        return statusCode >= 300 && statusCode < 400;
    }

    public boolean isError() {
        return statusCode >= 400;
    }

    public boolean isOk() {
        return statusCode == 200;
    }
}
