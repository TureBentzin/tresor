package net.juligames.tresor.lang;

import net.juligames.tresor.TresorGUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
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
        String message = Translations.getMessage("app.title");
        log.info("Message: {}", message);
        assertNotNull(message);
        assertNotEquals("app.title", message);
    }


    @Test
    void getAvailableMessageSets() {
        List<String> availableMessageSets = Translations.getAvailableMessageSets();

        log.info("Available message sets: {}", availableMessageSets);
        assertFalse(availableMessageSets.isEmpty());

        // Check if the default message set is available
        assertTrue(availableMessageSets.contains(Translations.DEFAULT_SET));
    }


    @Test
    void getDefaultMessageSet() {
        Map<String, String> defaultMessageSet = Translations.getDefaultMessageSet();
        defaultMessageSet.forEach((key, value) -> {
            log.info("Key: {}, Value: {}", key, value);
        });
        assertFalse(defaultMessageSet.isEmpty());
    }

    @Test
    void messageSetIntegrity() {
        @NotNull @Unmodifiable List<String> availableMessageSets = Translations.getAvailableMessageSets();
        Set<String> keys = Translations.getDefaultMessageSet().keySet();
        for (String messageSet : availableMessageSets) {
            log.info("Checking message set: {}", messageSet);
            Map<String, String> messageSetMap = Translations.getMessageSet(messageSet);
            assertFalse(messageSetMap.isEmpty());

            messageSetMap.forEach((key, value) -> {
                assertNotNull(key, "Message set key are not allowed to be null");
                assertNotNull(value, "Message associated with key: " + key + " in message set: " + messageSet + " is null");
            });

            //check for unused keys
            messageSetMap.keySet().forEach(key -> {
                assertTrue(keys.contains(key), "Key: " + key + " in message set: " + messageSet + " is not present in default message set");
            });
        }
    }

}