package net.juligames.tresor.controller;


import net.juligames.tresor.TresorGUI;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 27-02-2025
 */
public class AbstractController {
    protected final @NotNull TresorGUI gui;

    public AbstractController(@NotNull TresorGUI gui) {
        this.gui = gui;
    }

    protected final @NotNull String getJwtOrEmpty() {
        return gui.getAuthenticationController().getJwt().orElseThrow();
    }

}
