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
    }


    @Test
    void getGitCommit() {
        String gitCommit = ProjectPropertiesUtil.getGitCommit();
        log.info("GitCommit: {}", gitCommit);
        assertNotNull(gitCommit, "Unable to find current git commit");
    }

    @Test
    void getGitBranch() {
        String gitBranch = ProjectPropertiesUtil.getGitBranch();
        log.info("GitBranch: {}", gitBranch);
        assertNotNull(gitBranch, "Unable to find current git branch");
    }

    @Test
    void getVersion() {
        String version = ProjectPropertiesUtil.getGitVersion();
        log.info("Version: {}", version);
        assertNotNull(version, "Unable to find current version");
    }

    @Test
    void getBuildTime() {
        String buildTime = ProjectPropertiesUtil.getGitBuildTime();
        log.info("BuildTime: {}", buildTime);
        assertNotNull(buildTime, "Unable to find build time");
    }

    @Test
    void getBuildUserEmail() {
        String buildUserEmail = ProjectPropertiesUtil.getGitBuildUserEmail();
        log.info("User Email: {}", buildUserEmail);
        assertNotNull(buildUserEmail, "Git user email not set");
    }

    @Test
    void getBuildUserName() {
        String buildUserName = ProjectPropertiesUtil.getGitBuildUserName();
        log.info("User Name: {}", buildUserName);
        assertNotNull(buildUserName, "Git user name not set");
    }


}