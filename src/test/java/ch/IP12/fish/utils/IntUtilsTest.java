package ch.IP12.fish.utils;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static ch.IP12.fish.utils.IntUtils.isInRange;
import static ch.IP12.fish.utils.IntUtils.collidesWith;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntUtilsTest {

    @BeforeAll
    public static void initJfxRuntime() {
        try {
            Platform.runLater(() -> {});
        } catch (Exception e) {
            Platform.startup(() -> {});
        }
    }

    @Test
    public void testIsInRange() {
        int value = 50;
        assertTrue(isInRange(value, 0, 100));

        value = 100;
        assertTrue(isInRange(value, 0, 100));

        value = 0;
        assertTrue(isInRange(value, 0, 100));
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
    }

    @Test
    public void testRangeIsInRange() {
        int min = 0;
        int max = 100;

        assertTrue(collidesWith(min, max, 100, 200));
        assertTrue(collidesWith(min, max, 0, 100));
        assertTrue(collidesWith(min, max, -100, 0));
    }

    @Test
    public void testRangeIsOutOfRange() {
        int min = 0;
        int max = 100;

        assertFalse(collidesWith(min, max, 101, 201));
        assertFalse(collidesWith(min, max, -101, -1));
    }
}
