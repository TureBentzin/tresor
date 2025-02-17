package net.juligames.tresor.model;

import org.jetbrains.annotations.NotNull;

import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class ProjectPropertiesUtil {

    private static final @NotNull Properties properties = new Properties();

    static {
        try (InputStream input = ProjectPropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
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

    public static @NotNull String getGitCommit() {
        return properties.getProperty("project.git.commit", "unknown");
    }

    public static @NotNull String getGitVersion() {
        return properties.getProperty("project.git.version", "unknown");
    }

    public static @NotNull String getGitBranch() {
        return properties.getProperty("project.git.branch", "unknown");
    }

    public static @NotNull String getGitBuildTime() {
        return properties.getProperty("project.git.build.time", "unknown");
    }

    public static @NotNull String getGitBuildUserEmail() {
        return properties.getProperty("project.git.build.user.email", "unknown");
    }

    public static @NotNull String getGitBuildUserName() {
        return properties.getProperty("project.git.build.user.name", "unknown");
    }

    // Optional: Print all properties
    public static void printAllProperties() {
        System.out.println("Artifact ID: " + getArtifactId());
        System.out.println("Git Commit: " + getGitCommit());
        System.out.println("Git Version: " + getGitVersion());
        System.out.println("Git Branch: " + getGitBranch());
        System.out.println("Git Build Time: " + getGitBuildTime());
        System.out.println("Git Build User Email: " + getGitBuildUserEmail());
        System.out.println("Git Build User Name: " + getGitBuildUserName());
    }
}
