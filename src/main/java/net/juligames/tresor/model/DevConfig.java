package net.juligames.tresor.model;


import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author Ture Bentzin
 * @since 19-02-2025
 */
public class DevConfig implements ConfigModel {

    private static final @NotNull DevConfig instance = new DevConfig();

    private DevConfig() {
    }

    public static @NotNull DevConfig getInstance() {
        return instance;
    }

    @Override
    public @NotNull String defaultServer() {
        return "https://bank.staging.befatorinc.de";
    }

    @Override
    public @NotNull Set<String> getServerBlacklistRegex() {
        return Set.of();
    }

    @Override
    public @NotNull Set<String> getServerWhitelistRegex() {
        return Set.of(".*");
    }

}
