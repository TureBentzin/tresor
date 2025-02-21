package net.juligames.tresor.model;


import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Ture Bentzin
 * @since 19-02-2025
 */
public interface ConfigModel {

    record SanityResult(String message, Type type) {
        enum Type {
            ERROR,
            WARNING,
            ADVISE
        }
    }

    static @NotNull Set<SanityResult> runSanityChecks(@NotNull ConfigModel config) {
        Set<SanityResult> results = new HashSet<>();
        if (config.getServerBlacklistRegex().isEmpty() && config.getServerWhitelistRegex().isEmpty()) {
            results.add(new SanityResult("No server blacklist or whitelist regex defined", SanityResult.Type.WARNING));
        }

        if (config.getServerBlacklistRegex().stream().anyMatch(String::isBlank)) {
            results.add(new SanityResult("Blank server blacklist regex found", SanityResult.Type.ERROR));
        }

        if (config.getServerWhitelistRegex().stream().anyMatch(String::isBlank)) {
            results.add(new SanityResult("Blank server whitelist regex found", SanityResult.Type.ERROR));
        }

        if (config.getServerBlacklistRegex().stream().anyMatch(s -> config.getServerWhitelistRegex().contains(s))) {
            results.add(new SanityResult("Server in blacklist is also in whitelist", SanityResult.Type.ERROR));
        }

        if (config.getServerWhitelistRegex().size() > 10) {
            results.add(new SanityResult("Long server whitelist - consider using more regex", SanityResult.Type.ADVISE));
        }

        return results;
    }

    @NotNull String defaultServer();

    /**
     * Get the server blacklist regex.
     * If a server matches this regex, it is not allowed.
     *
     * @return the server blacklist regex
     */
    @NotNull Set<String> getServerBlacklistRegex();

    /**
     * Get the server whitelist regex.
     * If this is empty, all servers are allowed (except those in the blacklist) are allowed.
     *
     * @return the server whitelist regex
     */
    @NotNull Set<String> getServerWhitelistRegex();
}
