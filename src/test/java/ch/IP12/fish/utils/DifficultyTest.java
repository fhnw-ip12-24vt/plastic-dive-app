package ch.IP12.fish.utils;

import ch.IP12.fish.testUtils.WatchTests;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WatchTests
public class DifficultyTest {

    @BeforeAll
    public static void initJfxRuntime() {
        try {
            Platform.runLater(() -> {});
        } catch (Exception e) {
            Platform.startup(() -> {});
        }
    }

    @Test
    public void testDifficulty() {
        Difficulty phase = Difficulty.Easy;
        assertEquals(phase, Difficulty.Easy);

        phase = Difficulty.Hard;
        assertEquals(phase, Difficulty.Hard);
    }

    @Test
    public void testGetDifficulty() {
        Difficulty phase = Difficulty.getDifficulty(5181539527921L);
        assertEquals(Difficulty.Easy, phase);

        phase = Difficulty.getDifficulty(7751064387955L);
        assertEquals(Difficulty.Hard, phase);
    }
}
