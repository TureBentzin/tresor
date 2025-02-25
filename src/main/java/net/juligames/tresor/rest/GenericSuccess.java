package net.juligames.tresor.rest;


/**
 * @author Ture Bentzin
 * @since 25-02-2025
 */
public record GenericSuccess(int status) {
    public GenericSuccess {
        if (status < 200 || status > 299) {
            throw new IllegalArgumentException("Status must be between 200 and 299");
        }
    }
}
