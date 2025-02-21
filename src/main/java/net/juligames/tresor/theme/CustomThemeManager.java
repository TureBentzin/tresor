package net.juligames.tresor.theme;


import com.googlecode.lanterna.bundle.LanternaThemes;
import com.googlecode.lanterna.graphics.PropertyTheme;
import com.googlecode.lanterna.graphics.Theme;
import com.googlecode.lanterna.gui2.AbstractTextGUI;
import net.juligames.tresor.Tresor;
import net.juligames.tresor.lang.Translations;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ture Bentzin
 * @since 21-02-2025
 */
public class CustomThemeManager {

    private static final @NotNull Logger log = LoggerFactory.getLogger(CustomThemeManager.class);

    private static final @NotNull ConcurrentHashMap<String, Theme> REGISTERED_THEMES = new ConcurrentHashMap<>();


    /**
     * This finds all themes in the classpath (themes/*.properties) and registers them with this class. This method should be called once at
     * the start of the application to ensure all themes are available.
     */
    public static void findAndRegisterThemes() {

        String resourceFolder = "theme"; // Directory within src/main/resources
        ClassLoader classLoader = Tresor.class.getClassLoader();

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
                    if (fileName.endsWith(".properties")) {
                        String name = fileName.substring(0, fileName.length() - 11);

                        Properties properties = new Properties();
                        try (InputStream input = new FileInputStream(new File(folder, fileName))) {
                            properties.load(input);
                        } catch (IOException e) {
                            log.error("Failed to load theme properties from file '{}'", fileName, e);
                            continue;
                        }
                        registerPropTheme(name, properties);
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
                    String entryName = entry.getName();

                    if (entryName.startsWith(resourceFolder) && entryName.endsWith(".properties")) {
                        String fileName = entryName.substring(entryName.lastIndexOf("/") + 1);
                        String name = fileName.substring(0, fileName.length() - 11);

                        Properties properties = new Properties();
                        try (InputStream input = jarFile.getInputStream(entry)) {
                            properties.load(input);
                        } catch (IOException e) {
                            log.error("Failed to load theme properties from file '{}'", fileName, e);
                            continue;
                        }
                        registerPropTheme(name, properties);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            log.error("Unsupported protocol: {}", url.getProtocol());
        }

        log.info("Registered themes: {}", REGISTERED_THEMES.keySet());
    }

    /**
     * Identifies the theme by its name and returns it. If the theme is not found, the default theme is returned.
     *
     * @param theme The theme to identify
     * @return The theme
     */
    public static @Nullable String identify(@NotNull Theme theme) {
        for (String name : REGISTERED_THEMES.keySet()) {
            if (REGISTERED_THEMES.get(name).equals(theme)) {
                return name;
            }
        }
        return null;
    }


    /**
     * Returns a collection of all themes registered with this class, by their name. To get the associated {@link Theme}
     * object, please use {@link #getRegisteredTheme(String)}.
     *
     * @return Collection of theme names
     */
    @Contract(" -> new")
    public static @NotNull Collection<String> getRegisteredThemes() {
        return new ArrayList<>(REGISTERED_THEMES.keySet());
    }

    /**
     * Returns the {@link Theme} registered with this class under {@code name}, or {@code null} if there is no such
     * registration.
     *
     * @param name Name of the theme to retrieve
     * @return {@link Theme} registered with the supplied name, or {@code null} if none
     */
    public static @NotNull Theme getRegisteredTheme(@NotNull String name) {
        return REGISTERED_THEMES.get(name);
    }

    /**
     * Registers a {@link Theme} with this class under a certain name so that calling
     * {@link #getRegisteredTheme(String)} on that name will return this theme and calling
     * {@link #getRegisteredThemes()} will return a collection including this name.
     *
     * @param name  Name to register the theme under
     * @param theme Theme to register with this name
     */
    public static void registerTheme(@NotNull String name, @NotNull Theme theme) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        Theme result = REGISTERED_THEMES.putIfAbsent(name, theme);
        if (result != null && result != theme) {
            throw new IllegalArgumentException("There is already a theme registered with the name '" + name + "'");
        }
    }

    private static void registerPropTheme(@NotNull String name, @NotNull Properties properties) {
        log.info("Registering theme: {}", name);
        registerTheme(name, new PropertyTheme(properties, false));
    }


    private CustomThemeManager() {
        throw new IllegalStateException("Utility class");
    }

}
