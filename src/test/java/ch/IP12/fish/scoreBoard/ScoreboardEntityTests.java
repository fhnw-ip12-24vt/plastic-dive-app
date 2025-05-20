package ch.IP12.fish.scoreBoard;

import ch.IP12.fish.testUtils.WatchTests;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WatchTests
public class ScoreboardEntityTests {
    @BeforeAll
    public static void initJfxRuntime() {
        try {
            Platform.runLater(() -> {});
        } catch (Exception e) {
            Platform.startup(() -> {});
        }
    }

    @Test
    public void testScoreboardEntityCreation() {
        assertDoesNotThrow(() -> new ScoreboardEnitity(4000, "hell"));
    }

    @Test
    public void testGetScore() {
        ScoreboardEnitity se = new ScoreboardEnitity(4000, "hello");
        assertEquals(4000, se.getScore());

        se = new ScoreboardEnitity(5000, "world");
        assertEquals(5000, se.getScore());
    }

    @Test
    public void testGetName() {
        ScoreboardEnitity se = new ScoreboardEnitity(4000, "hello");
        assertEquals("hello", se.getName());

        se = new ScoreboardEnitity(5000, "world");
        assertEquals("world", se.getName());
    }
}
