package net.juligames.tresor.lang;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import net.juligames.tresor.Tresor;
import net.juligames.tresor.views.common.Common;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

// PART of GoodProxy project
public final class Translations {

    private static final @NotNull Map<String, Map<String, String>> MESSAGE_SETS = new HashMap<>();
    public static final @NotNull String DEFAULT_SET = "en";
    private static final @NotNull Logger log = LoggerFactory.getLogger(Translations.class);

    static {
        detectAndLoadMessageSets();
        log.info("Loaded message sets: {}", MESSAGE_SETS.keySet());
    }

    private Translations() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static void detectAndLoadMessageSets() {
        String resourceFolder = "langfiles"; // Directory within src/main/resources
        ClassLoader classLoader = Tresor.class.getClassLoader();

        try {
            java.net.URL url = classLoader.getResource(resourceFolder);
            if (url == null) {
                log.warn("Resource folder '{}' not found", resourceFolder);
                return;
            }

            log.info("Scanning resource folder: {}", url.getPath());

            if (url.getProtocol().equals("file")) {
                // Running from IDE
                File folder = new File(url.getPath());
                if (folder.isDirectory()) {
                    for (String fileName : Objects.requireNonNull(folder.list())) {
                        if (fileName.startsWith("langfile_") && fileName.endsWith(".json")) {
                            String setId = fileName.substring(9, fileName.length() - 5);
                            loadMessageSet(setId);
                        }
                    }
                }
            } else if (url.getProtocol().equals("jar")) {
                // Running from a shaded JAR
                String jarPath = url.getPath().substring(5, url.getPath().indexOf("!"));
                try (java.util.jar.JarFile jarFile = new java.util.jar.JarFile(jarPath)) {
                    java.util.Enumeration<java.util.jar.JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        java.util.jar.JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        if (name.startsWith(resourceFolder + "/langfile_") && name.endsWith(".json")) {
                            String fileName = name.substring(name.lastIndexOf("/") + 1);
                            String setId = fileName.substring(9, fileName.length() - 5);
                            loadMessageSet(setId);
                        }
                    }
                }
            } else {
                log.error("Unsupported protocol: {}", url.getProtocol());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to detect message sets", e);
        }
    }


    private static void loadMessageSet(@NotNull String setId) {
        String fileName = "langfiles/langfile_" + setId + ".json";
        log.info("Attempting to load message set from: {}", fileName);

        try (InputStream inputStream = Translations.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                log.error("Resource not found: {}", fileName);
                throw new FileNotFoundException("Resource not found: " + fileName);
            }

            try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                Gson gson = new Gson();
                Map<String, Object> nestedMap = gson.fromJson(reader, new TypeToken<Map<String, Object>>() {
                }.getType());

                if (nestedMap == null) {
                    log.warn("Empty or invalid JSON in file: {}", fileName);
                    return;
                }

                Map<String, String> flattenedMap = new HashMap<>();
                flattenMap("", nestedMap, flattenedMap);

                MESSAGE_SETS.put(setId, Collections.unmodifiableMap(flattenedMap));
                log.info("Successfully loaded message set: {}", setId);
            }

        } catch (FileNotFoundException e) {
            log.error("Message set file not found: {}", fileName, e);
        } catch (JsonSyntaxException e) {
            log.error("Failed to parse JSON in file: {}", fileName, e);
        } catch (IOException e) {
            log.error("I/O error occurred while loading message set: {}", fileName, e);
        } catch (Exception e) {
            log.error("Unexpected error occurred while loading message set: {}", fileName, e);
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
        return getMessage(key, DEFAULT_SET, false);
    }

    public static @NotNull String getMessage(@NotNull String key, boolean tiny) {
        return getMessage(key, DEFAULT_SET, tiny);
    }

    public static @NotNull String getMessage(@NotNull String key, @NotNull String setId, boolean tiny) {
        // order: set, default, ? (or ?key?)

        return getMessageSet(setId).getOrDefault(key, getDefaultMessageSet().getOrDefault(key, "?"+key+"?"));
    }

    public static @NotNull @Unmodifiable List<String> getAvailableMessageSets() {
        return List.copyOf(MESSAGE_SETS.keySet());
    }

    public static @NotNull @UnmodifiableView Map<String, String> getMessageSet(@NotNull String setId) {
        Map<String, String> messageSet = MESSAGE_SETS.getOrDefault(setId, Collections.emptyMap());
        Map<String, String> defaultSet = MESSAGE_SETS.getOrDefault(DEFAULT_SET, Collections.emptyMap());
        Map<String, String> result = new HashMap<>(defaultSet);
        result.putAll(messageSet);
        return Collections.unmodifiableMap(result);
    }

    public static @NotNull @UnmodifiableView Map<String, String> getDefaultMessageSet() {
        return Collections.unmodifiableMap(MESSAGE_SETS.getOrDefault(DEFAULT_SET, Collections.emptyMap()));
    }

    public static @NotNull Map<String, String> getAllMessages(@NotNull String setId) {
        return MESSAGE_SETS.getOrDefault(setId, Collections.emptyMap());
    }

}
