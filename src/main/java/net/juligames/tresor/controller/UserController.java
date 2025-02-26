package net.juligames.tresor.controller;


import net.juligames.tresor.TresorGUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * @author Ture Bentzin
 * @since 26-02-2025
 */
public class UserController {

    private final @NotNull TresorGUI gui;

    public UserController(@NotNull TresorGUI gui) {
        this.gui = gui;
    }

    public @Range(from = 0, to = Integer.MAX_VALUE) int getBalance() {
        //TODO
    }
}
