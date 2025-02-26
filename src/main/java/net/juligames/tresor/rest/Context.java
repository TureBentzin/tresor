package net.juligames.tresor.rest;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.StringJoiner;

/**
 * @author Ture Bentzin
 * @since 26-02-2025
 */
public record Context(Map<String, Object> values) {

    @Override
    public @NotNull String toString() {
        if (values == null || values.isEmpty()) {
            return "{}";
        }
        StringJoiner joiner = new StringJoiner(", ", "{", "}");
        values.forEach((key, value) -> joiner.add(key + ": " + value));
        return joiner.toString();
    }

}
