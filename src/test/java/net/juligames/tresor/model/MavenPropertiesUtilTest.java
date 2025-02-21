package net.juligames.tresor.model;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ture Bentzin
 * @since 17-02-2025
 */
class MavenPropertiesUtilTest {

    private static final @NotNull Logger log = LoggerFactory.getLogger(MavenPropertiesUtilTest.class);


    @Test
    void getArtifactId() {
        String artifactId = ProjectPropertiesUtil.getArtifactId();
        log.info("ArtifactId: {}", artifactId);
        assertNotNull(artifactId, "ArtifactId is null");
        assertFalse(artifactId.isEmpty(), "ArtifactId is empty");
        assertNotEquals("unknown", artifactId, "ArtifactId is unknown");
    }


    @Test
    void getGitCommit() {
        String gitCommit = ProjectPropertiesUtil.getGitCommit();
        log.info("GitCommit: {}", gitCommit);
        assertNotNull(gitCommit, "Unable to find current git commit");
        assertFalse(gitCommit.isEmpty(), "Unable to find current git commit");
        assertNotEquals("unknown", gitCommit, "GitCommit is unknown");
    }

    @Test
    void getGitBranch() {
        String gitBranch = ProjectPropertiesUtil.getGitBranch();
        log.info("GitBranch: {}", gitBranch);
        assertNotNull(gitBranch, "Unable to find current git branch");
        assertFalse(gitBranch.isEmpty(), "Unable to find current git branch");
        assertNotEquals("unknown", gitBranch, "GitBranch is unknown");
    }

    @Test
    void getVersion() {
        String version = ProjectPropertiesUtil.getGitVersion();
        log.info("Version: {}", version);
        assertNotNull(version, "Unable to find current version");
        assertFalse(version.isEmpty(), "Unable to find current version");
        assertNotEquals("unknown", version, "Version is unknown");
    }

    @Test
    void getBuildTime() {
        String buildTime = ProjectPropertiesUtil.getGitBuildTime();
        log.info("BuildTime: {}", buildTime);
        assertNotNull(buildTime, "Unable to find build time");
        assertFalse(buildTime.isEmpty(), "Unable to find build time");
        assertNotEquals("unknown", buildTime, "BuildTime is unknown");
    }

    @Test
    void getBuildUserEmail() {
        String buildUserEmail = ProjectPropertiesUtil.getGitBuildUserEmail();
        log.info("User Email: {}", buildUserEmail);
        assertNotNull(buildUserEmail, "Git user email not set");
        assertFalse(buildUserEmail.isEmpty(), "Git user email not set");
        assertNotEquals("unknown", buildUserEmail, "User Email is unknown");
    }

    @Test
    void getBuildUserName() {
        String buildUserName = ProjectPropertiesUtil.getGitBuildUserName();
        log.info("User Name: {}", buildUserName);
        assertNotNull(buildUserName, "Git user name not set");
        assertFalse(buildUserName.isEmpty(), "Git user name not set");
        assertNotEquals("unknown", buildUserName, "User Name is unknown");
    }


}