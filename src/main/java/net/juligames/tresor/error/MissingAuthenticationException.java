package net.juligames.tresor.error;


import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.rest.ResponseContainer;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 26-02-2025
 */
public class MissingAuthenticationException extends Exception {


    @NotNull
    private final TresorGUI gui;

    public MissingAuthenticationException(@NotNull TresorGUI gui, @NotNull Class<?> controllerClass) {
        super("Attempted to access a protected resource without being authenticated. Controller: " + controllerClass.getSimpleName());
        this.gui = gui;
    }

    public @NotNull TresorGUI getGui() {
        return gui;
    }
}
