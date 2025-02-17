package net.juligames.tresor.lang;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

// PART of GoodProxy project
public final class Translations {

    private static final @NotNull Map<String, Map<String, String>> MESSAGE_SETS = new HashMap<>();
    private static final @NotNull String DEFAULT_SET = "en";

    static {
        detectAndLoadMessageSets();
    }

    private Translations() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static void detectAndLoadMessageSets() {
        ClassLoader classLoader = Translations.class.getClassLoader();
        try {
            Enumeration<java.net.URL> resources = classLoader.getResources("");
            while (resources.hasMoreElements()) {
                java.net.URL resource = resources.nextElement();
                java.io.File dir = new java.io.File(resource.getPath());
                if (dir.isDirectory()) {
                    for (String fileName : Objects.requireNonNull(dir.list())) {
                        if (fileName.startsWith("langfile_") && fileName.endsWith(".json")) {
                            String setId = fileName.substring(9, fileName.length() - 5);
                            loadMessageSet(setId);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to detect message sets", e);
        }
    }

    private static void loadMessageSet(@NotNull String setId) {
        String fileName = "langfile_" + setId + ".json";
        try (InputStream inputStream = Translations.class.getClassLoader().getResourceAsStream(fileName);
             InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8)) {

            Gson gson = new Gson();
            Map<String, Object> nestedMap = gson.fromJson(reader, new TypeToken<Map<String, Object>>() {
            }.getType());
            Map<String, String> flattenedMap = new HashMap<>();

            flattenMap("", nestedMap, flattenedMap);

            MESSAGE_SETS.put(setId, Collections.unmodifiableMap(flattenedMap));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load message set: " + setId, e);
        }
    }

    private static void flattenMap(@NotNull String prefix, Map<String, Object> nestedMap, @NotNull Map<String, String> flattenedMap) {
        for (Map.Entry<String, Object> entry : nestedMap.entrySet()) {
            String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map) {
                flattenMap(key, (Map<String, Object>) value, flattenedMap);
            } else {
                flattenedMap.put(key, value.toString());
            }
        }
    }

    public static @NotNull String getMessage(@NotNull String key) {
        return getMessage(key, DEFAULT_SET);
    }

    public static @NotNull String getMessage(@NotNull String key, @NotNull String setId) {
        return MESSAGE_SETS.getOrDefault(setId, Collections.emptyMap()).getOrDefault(key, key);
    }

    public static @NotNull Set<String> getAvailableMessageSets() {
        return MESSAGE_SETS.keySet();
    }

    public static @NotNull @UnmodifiableView Map<String, String> getMessageSet(@NotNull String setId) {
        return Collections.unmodifiableMap(MESSAGE_SETS.getOrDefault(setId, Collections.emptyMap()));
    }

    public static @NotNull @UnmodifiableView Map<String, String> getDefaultMessageSet() {
        return Collections.unmodifiableMap(MESSAGE_SETS.getOrDefault(DEFAULT_SET, Collections.emptyMap()));
    }

    public static @NotNull Map<String, String> getAllMessages(@NotNull String setId) {
        return MESSAGE_SETS.getOrDefault(setId, Collections.emptyMap());
    }
}
