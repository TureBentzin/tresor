package net.juligames.tresor.rest;


import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 21-02-2025
 */
public enum HTTPStatus {

    OK("OK", 200),
    CREATED("Created", 201),
    ACCEPTED("Accepted", 202),
    NO_CONTENT("No Content", 204),
    BAD_REQUEST("Bad Request", 400),
    UNAUTHORIZED("Unauthorized", 401),
    FORBIDDEN("Forbidden", 403),
    NOT_FOUND("Not Found", 404),
    METHOD_NOT_ALLOWED("Method Not Allowed", 405),
    CONFLICT("Conflict", 409),
    INTERNAL_SERVER_ERROR("Internal Server Error", 500),
    NOT_IMPLEMENTED("Not Implemented", 501),
    SERVICE_UNAVAILABLE("Service Unavailable", 503)

    //TODO to be continued
    ;
    @NotNull
    final String message;
    final int code;

    HTTPStatus(@NotNull String message, int code) {
        this.message = message;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public @NotNull String getMessage() {
        return message;
    }

    public static @NotNull HTTPStatus fromCode(int code) {
        for (HTTPStatus status : HTTPStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("No HTTPStatus with code " + code);
    }

}
