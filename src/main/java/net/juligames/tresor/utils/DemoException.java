package net.juligames.tresor.utils;


import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 21-02-2025
 */
public class DemoException extends RuntimeException {

    public DemoException(@NotNull String message) {
        super(message);
    }

    public DemoException(@NotNull String message, @NotNull Throwable cause) {
        super(message, cause);
    }
}
