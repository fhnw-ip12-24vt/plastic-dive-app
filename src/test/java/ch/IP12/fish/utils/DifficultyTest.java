package ch.IP12.fish.utils;

import ch.IP12.fish.fileInterpreters.Logger;
import javafx.application.Platform;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static ch.IP12.fish.utils.StackUtils.getClassName;
import static ch.IP12.fish.utils.StackUtils.getMethodName;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DifficultyTest {
    private static Logger logger = Logger.getInstance("testLog");

    @BeforeAll
    public static void initJfxRuntime() {
        try {
            Platform.runLater(() -> {});
        } catch (Exception e) {
            Platform.startup(() -> {});
        }

        logger.start();
        logger.log("Starting " + getClassName());

        logger.log("Test data initialized");
    }

    @AfterAll
    static void endLog(){
        logger.end();
    }

    @Test
    public void testDifficulty() {
        Difficulty phase = Difficulty.Easy;
        assertEquals(phase, Difficulty.Easy);

        phase = Difficulty.Hard;
        assertEquals(phase, Difficulty.Hard);
        logger.log("Test passed: " + getMethodName());
    }

    @Test
    public void testGetDifficulty() {
        Difficulty phase = Difficulty.getDifficulty(7624841656535L);
        assertEquals(Difficulty.Easy, phase);

        phase = Difficulty.getDifficulty(7751064387955L);
        assertEquals(Difficulty.Hard, phase);
        logger.log("Test passed: " + getMethodName());
    }
}
