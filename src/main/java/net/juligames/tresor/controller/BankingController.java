package net.juligames.tresor.controller;


import net.juligames.tresor.TresorGUI;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * @author Ture Bentzin
 * @since 19-02-2025
 */
public class BankingController extends AbstractController {

    public BankingController(@NotNull TresorGUI gui) {
        super(gui);
    }

    @Blocking
    public @NotNull String getCurrency() {
        return "STC"; //STEAMCOIN$
    }

    @Blocking
    public @NotNull String getMOTD() {
        return "Welcome to the Tresor Banking System!";
    }
}
