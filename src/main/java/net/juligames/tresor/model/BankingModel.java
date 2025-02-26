package net.juligames.tresor.model;


import org.jetbrains.annotations.NotNull;

import java.net.URL;

import static net.juligames.tresor.rest.RESTCaller.createURL;

/**
 * @author Ture Bentzin
 * @since 26-02-2025
 */
public class BankingModel extends Model {
    public BankingModel(@NotNull String host) {
        super(host);
    }


    public @NotNull String getCurrency() {
        final URL url = createURL(getHost(), "/api/v1/banking/currency");

        //TODO api does not support this yet
        return "STC";
    }

    public @NotNull String getMOTD() {
        final URL url = createURL(getHost(), "/api/v1/motd");

        //TODO api does not support this yet
        return "Welcome to the Tresor Banking System!";
    }

    public @NotNull String getBankName() {
        final URL url = createURL(getHost(), "/api/v1/banking/name");

        //TODO api does not support this yet
        return "Tresor Bank";
    }
}
