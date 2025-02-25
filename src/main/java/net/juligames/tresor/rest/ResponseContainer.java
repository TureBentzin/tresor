package net.juligames.tresor.rest;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

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
    public static @NotNull <T> ResponseContainer<T> failure(@NotNull GenericError genericError) {
        return new ResponseContainer<>(null, genericError, null, null);
    }

    @Contract("_ -> new")
    public static @NotNull <T> ResponseContainer<T> differentJson(@NotNull String differentJson) {
        return new ResponseContainer<>(null, null, differentJson, null);
    }

    @Contract("_ -> new")
    public static @NotNull <T> ResponseContainer<T> unprocessableEntity(@NotNull Set<ServersideProcessingError> unprocessableEntity) {
        return new ResponseContainer<>(null, null, null, unprocessableEntity);
    }


    public enum ResponseType {
        RESPONSE,
        ERROR,
        UNPROCESSABLE_ENTITY,
        DIFFERENT_JSON,
    }

    private final @Nullable T response;

    private final @Nullable GenericError error;

    private final @Nullable String differentJson;

    private final @Nullable Set<ServersideProcessingError> unprocessableEntity;

    private final @NotNull ResponseType responseType;


    private ResponseContainer(@Nullable T response, @Nullable GenericError error, @Nullable String differentJson, @Nullable Set<ServersideProcessingError> unprocessableEntity) {


        if (response != null) {
            responseType = ResponseType.RESPONSE;
            this.response = response;

            this.error = null;
            this.differentJson = null;
            this.unprocessableEntity = null;

        } else if (error != null) {
            responseType = ResponseType.ERROR;
            this.error = error;
            this.response = null;
            this.differentJson = null;
            this.unprocessableEntity = null;

        } else if (differentJson != null) {
            responseType = ResponseType.DIFFERENT_JSON;
            this.differentJson = differentJson;

            this.response = null;
            this.error = null;
            this.unprocessableEntity = null;
        } else {
            responseType = ResponseType.UNPROCESSABLE_ENTITY;
            this.unprocessableEntity = unprocessableEntity;

            this.response = null;
            this.error = null;
            this.differentJson = null;
        }


    }

    public @Nullable String getDifferentJson() {
        return differentJson;
    }

    public @Nullable GenericError getError() {
        return error;
    }

    public @Nullable T getResponse() {
        return response;
    }

    /**
     * @return the serverside processing errors, if any
     */
    public @Nullable Set<ServersideProcessingError> getUnprocessableEntity() {
        return unprocessableEntity;
    }

    public @NotNull ResponseType getResponseType() {
        return responseType;
    }

    public boolean isSuccessful() {
        return responseType == ResponseType.RESPONSE;
    }

    public boolean isUnauthorized() {
        return responseType == ResponseType.ERROR;
    }

    public boolean isDifferentJson() {
        return responseType == ResponseType.DIFFERENT_JSON;
    }

    public boolean isUnprocessableEntity() {
        return responseType == ResponseType.UNPROCESSABLE_ENTITY;
    }

    @Override
    public @NotNull String toString() {
        return "{" + responseType + ": " + (response != null ? response : error != null ? error : differentJson) + "}";
    }
}

