package net.juligames.tresor.model;

import org.jetbrains.annotations.NotNull;

import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class MavenPropertiesUtil {

    private static final @NotNull Properties properties = new Properties();

    static {
        try (InputStream input = MavenPropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);
            } else {
                throw new IOException("application.properties file not found in classpath");
            }
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Failed to load Maven properties: " + e.getMessage());
        }
    }

    public static @NotNull String getArtifactId() {
        return properties.getProperty("project.artifactId", "unknown");
    }

    public static @NotNull String getBuildNumber() {
        return properties.getProperty("project.build.number", "unknown");
    }

    public static @NotNull String getGitCommit() {
        return properties.getProperty("project.git.commit", "unknown");
    }
}
