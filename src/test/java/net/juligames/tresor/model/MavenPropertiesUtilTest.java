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
        assertNotNull(artifactId);
    }


    @Test
    void getGitCommit() {
        String gitCommit = ProjectPropertiesUtil.getGitCommit();
        log.info("GitCommit: {}", gitCommit);
        assertNotNull(gitCommit);
    }
}