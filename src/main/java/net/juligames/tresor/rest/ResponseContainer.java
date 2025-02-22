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
        return new ResponseContainer<>(response, null, null, null);
    }

    @Contract("_ -> new")
    public static @NotNull <T> ResponseContainer<T> unauthorized(@NotNull UnauthorizedResponse unauthorizedResponse) {
        return new ResponseContainer<>(null, unauthorizedResponse, null, null);
    }

    @Contract("_ -> new")
    public static @NotNull <T> ResponseContainer<T> differentJson(@NotNull String differentJson) {
        return new ResponseContainer<>(null, null, differentJson, null);
    }

    @Contract("_ -> new")
    public static @NotNull <T> ResponseContainer<T> unprocessableEntity(@NotNull UnprocessableEntity unprocessableEntity) {
        return new ResponseContainer<>(null, null, null, unprocessableEntity);
    }


    public enum ResponseType {
        RESPONSE,
        UNAUTHORIZED,
        UNPROCESSABLE_ENTITY,
        DIFFERENT_JSON,
    }

    private final @Nullable T response;

    private final @Nullable UnauthorizedResponse unauthorizedResponse;

    private final @Nullable String differentJson;

    private final @Nullable UnprocessableEntity unprocessableEntity;

    private final @NotNull ResponseType responseType;


    @SuppressWarnings("ConstantValue")
    private ResponseContainer(@Nullable T response, @Nullable UnauthorizedResponse unauthorizedResponse, @Nullable String differentJson, @Nullable UnprocessableEntity unprocessableEntity) {


        if (response != null) {
            responseType = ResponseType.RESPONSE;
            this.response = response;

            this.unauthorizedResponse = null;
            this.differentJson = null;
            this.unprocessableEntity = null;

        } else if (unauthorizedResponse != null) {
            responseType = ResponseType.UNAUTHORIZED;
            this.unauthorizedResponse = unauthorizedResponse;

            this.response = null;
            this.differentJson = null;
            this.unprocessableEntity = null;

        } else if (differentJson != null) {
            responseType = ResponseType.DIFFERENT_JSON;
            this.differentJson = differentJson;

            this.response = null;
            this.unauthorizedResponse = null;
            this.unprocessableEntity = null;
        } else {
            responseType = ResponseType.UNPROCESSABLE_ENTITY;
            this.unprocessableEntity = unprocessableEntity;

            this.response = null;
            this.unauthorizedResponse = null;
            this.differentJson = null;
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

    public @Nullable UnprocessableEntity getUnprocessableEntity() {
        return unprocessableEntity;
    }

    public @NotNull ResponseType getResponseType() {
        return responseType;
    }

    public boolean isSuccessful() {
        return responseType == ResponseType.RESPONSE;
    }

    public boolean isUnauthorized() {
        return responseType == ResponseType.UNAUTHORIZED;
    }

    public boolean isDifferentJson() {
        return responseType == ResponseType.DIFFERENT_JSON;
    }

    public boolean isUnprocessableEntity() {
        return responseType == ResponseType.UNPROCESSABLE_ENTITY;
    }

    @Override
    public @NotNull String toString() {
        return "{" + responseType + ": " + (response != null ? response : unauthorizedResponse != null ? unauthorizedResponse : differentJson) + "}";
    }
}

