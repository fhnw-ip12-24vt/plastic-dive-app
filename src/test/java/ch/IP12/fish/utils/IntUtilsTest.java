package ch.IP12.fish.utils;

import ch.IP12.fish.fileInterpreters.Logger;
import javafx.application.Platform;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static ch.IP12.fish.utils.IntUtils.isInRange;
import static ch.IP12.fish.utils.IntUtils.isRangeInRange;
import static ch.IP12.fish.utils.StackUtils.getClassName;
import static ch.IP12.fish.utils.StackUtils.getMethodName;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntUtilsTest {
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
    public void testIsInRange() {
        int value = 50;
        assertTrue(isInRange(value, 0, 100));

        value = 100;
        assertTrue(isInRange(value, 0, 100));

        value = 0;
        assertTrue(isInRange(value, 0, 100));

        logger.log("Test passed: " + getMethodName());
    }

    @Test
    public void testOutOfRange() {
        int value = -5;
        assertFalse(isInRange(value, 0, 100));

        value = 101;
        assertFalse(isInRange(value, 0, 100));

        value = Integer.MAX_VALUE;
        assertFalse(isInRange(value, 0, 100));

        value = Integer.MIN_VALUE;
        assertFalse(isInRange(value, 0, 100));

        logger.log("Test passed: " + getMethodName());
    }

    @Test
    public void testRangeIsInRange() {
        int min = 0;
        int max = 100;

        assertTrue(isRangeInRange(min, max, 100, 200));
        assertTrue(isRangeInRange(min, max, 0, 100));
        assertTrue(isRangeInRange(min, max, -100, 0));

        logger.log("Test passed: " + getMethodName());
    }

    @Test
    public void testRangeIsOutOfRange() {
        int min = 0;
        int max = 100;

        assertFalse(isRangeInRange(min, max, 101, 201));
        assertFalse(isRangeInRange(min, max, -101, -1));

        logger.log("Test passed: " + getMethodName());
    }
}
