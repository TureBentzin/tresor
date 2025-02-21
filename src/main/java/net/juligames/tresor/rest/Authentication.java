package net.juligames.tresor.rest;


/**
 * @author Ture Bentzin
 * @since 21-02-2025
 */
public record Authentication(String username, String password) {
    public Authentication {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
    }

}
