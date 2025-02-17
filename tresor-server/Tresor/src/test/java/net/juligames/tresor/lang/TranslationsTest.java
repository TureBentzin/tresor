package net.juligames.tresor.lang;

import net.juligames.tresor.TresorGUI;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ture Bentzin
 * @since 17-02-2025
 */
class TranslationsTest {

    private static final @NotNull Logger log = LoggerFactory.getLogger(TranslationsTest.class);

    @Test
    void getMessage() {
    }

    @Test
    void testGetMessage() {
    }

    @Test
    void getAvailableMessageSets() {
        Set<String> availableMessageSets = Translations.getAvailableMessageSets();

        log.info("Available message sets: {}", availableMessageSets);
    }

    @Test
    void getMessageSet() {
    }

    @Test
    void getDefaultMessageSet() {
    }

    @Test
    void getAllMessages() {
    }

    @Test
    void testGetDefaultMessageSet() {

        Translations.getDefaultMessageSet().forEach((key, value) -> {
            log.info("Key: {}, Value: {}", key, value);
        });
    }
}