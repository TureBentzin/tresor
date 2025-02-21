package net.juligames.tresor.model;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ture Bentzin
 * @since 19-02-2025
 */
public interface AuthenticationModel {

    @Nullable String authenticate(@NotNull String username, @NotNull String password);
}
