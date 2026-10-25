package tn.nightbeam.chronofall.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ConfigTest {
    @TempDir
    Path dir;

    @Test
    void writesDefaultsWithComments() throws Exception {
        ChronoConfig.load(dir);
        String text = Files.readString(dir.resolve(ChronoConfig.FILE), StandardCharsets.UTF_8);
        assertTrue(text.startsWith("// Chronofall."));
        assertTrue(text.contains("\"chance_per_minute\": 0.04"));
        assertTrue(text.contains("// Rift Gates"));
        assertEquals(0.04, ChronoConfig.get().rifts().chancePerMinute());
        assertEquals(40, ChronoConfig.get().gates().chargeTicks());
    }

    @Test
    void keepsUserValuesAndClampsBadOnes() throws Exception {
        Files.writeString(dir.resolve(ChronoConfig.FILE), """
                // a comment the user wrote
                {"rifts": {"enabled": false, "chance_per_minute": 7, "min_distance": 60, "max_distance": 30, "max_sentinels": -2},
                 "plasma_cutter_range": "far", "extra": 1}
                """);
        ChronoConfig.load(dir);
        ChronoConfig.Values v = ChronoConfig.get();
        assertFalse(v.rifts().enabled());
        assertEquals(1.0, v.rifts().chancePerMinute(), "clamped to 1");
        assertEquals(60, v.rifts().minDistance());
        assertEquals(60, v.rifts().maxDistance(), "max never below min");
        assertEquals(0, v.rifts().maxSentinels());
        assertEquals(12, v.cutterRange(), "a non-number falls back to the default");
        String text = Files.readString(dir.resolve(ChronoConfig.FILE), StandardCharsets.UTF_8);
        assertTrue(text.contains("\"extra\": 1"), "unknown keys are kept");
        assertTrue(text.contains("\"enabled\": false"));
    }

    @Test
    void brokenFileIsLeftAlone() throws Exception {
        Files.writeString(dir.resolve(ChronoConfig.FILE), "[1, 2");
        ChronoConfig.load(dir);
        assertEquals("[1, 2", Files.readString(dir.resolve(ChronoConfig.FILE)));
        assertTrue(ChronoConfig.get().rifts().enabled());
    }
}
