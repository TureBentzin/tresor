package net.juligames.tresor.rest;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ture Bentzin
 * @since 22-02-2025
 */
public class ResponseContainer<T> {

    /* This class contains either a response or an error */


    @Contract("_ -> new")
    public static @NotNull <T> ResponseContainer<T> successful(@NotNull T response) {
        return new ResponseContainer<>(response, null, null);
    }

    @Contract("_ -> new")
    public static @NotNull <T> ResponseContainer<T> unauthorized(@NotNull UnauthorizedResponse unauthorizedResponse) {
        return new ResponseContainer<>(null, unauthorizedResponse, null);
    }

    @Contract("_ -> new")
    public static @NotNull <T> ResponseContainer<T> differentJson(@NotNull String differentJson) {
        return new ResponseContainer<>(null, null, differentJson);
    }


    public enum ResponseType {
        RESPONSE,
        UNAUTHORIZED,
        DIFFERENT_JSON
    }

    private final @Nullable T response;

    private final @Nullable UnauthorizedResponse unauthorizedResponse;

    private final @Nullable String differentJson;

    private final @NotNull ResponseType responseType;

    @SuppressWarnings("ConstantValue")
    private ResponseContainer(@Nullable T response, @Nullable UnauthorizedResponse unauthorizedResponse, @Nullable String differentJson) {
        this.response = response;
        this.unauthorizedResponse = unauthorizedResponse;
        this.differentJson = differentJson;
        responseType = response != null ? ResponseType.RESPONSE : unauthorizedResponse != null ? ResponseType.UNAUTHORIZED : ResponseType.DIFFERENT_JSON;

        if (responseType == ResponseType.RESPONSE && (unauthorizedResponse != null || differentJson != null)) {
            throw new IllegalArgumentException("Response type is RESPONSE, but unauthorizedResponse or differentJson is not null");
        }

        if (responseType == ResponseType.UNAUTHORIZED && (response != null || differentJson != null)) {
            throw new IllegalArgumentException("Response type is UNAUTHORIZED, but response or differentJson is not null");
        }

        if (responseType == ResponseType.DIFFERENT_JSON && (response != null || unauthorizedResponse != null)) {
            throw new IllegalArgumentException("Response type is DIFFERENT_JSON, but response or unauthorizedResponse is not null");
        }

    }

    public @Nullable String getDifferentJson() {
        return differentJson;
    }

    public @Nullable UnauthorizedResponse getUnauthorizedResponse() {
        return unauthorizedResponse;
    }

    public @Nullable T getResponse() {
        return response;
    }

    public @NotNull ResponseType getResponseType() {
        return responseType;
    }

    public boolean isSuccessful() {
        return responseType == ResponseType.RESPONSE;
    }

    @Override
    public @NotNull String toString() {
        return "{" + responseType + ": " + (response != null ? response : unauthorizedResponse != null ? unauthorizedResponse : differentJson) + "}";
    }
}

