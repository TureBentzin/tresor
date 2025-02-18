package net.juligames.tresor.lang;

import net.juligames.tresor.TresorGUI;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
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
        List<String> availableMessageSets = Translations.getAvailableMessageSets();

        log.info("Available message sets: {}", availableMessageSets);
    }

    @Test
    void getMessageSet() {
    }

    @Test
    void getDefaultMessageSet() {
        Map<String,String> defaultMessageSet = Translations.getDefaultMessageSet();
        defaultMessageSet.forEach((key, value) -> {
            log.info("Key: {}, Value: {}", key, value);
        });
        assertFalse(defaultMessageSet.isEmpty());
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