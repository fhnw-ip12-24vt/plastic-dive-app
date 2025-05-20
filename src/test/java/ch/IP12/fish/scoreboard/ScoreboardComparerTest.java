package ch.IP12.fish.scoreboard;

import ch.IP12.fish.scoreBoard.ScoreboardComparer;
import ch.IP12.fish.scoreBoard.ScoreboardEnitity;
import ch.IP12.fish.testUtils.WatchTests;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@WatchTests
public class ScoreboardComparerTest {
    private final ScoreboardComparer comparer = new ScoreboardComparer();

    @BeforeAll
    public static void initJfxRuntime() {
        try {
            Platform.runLater(() -> {});
        } catch (Exception e) {
            Platform.startup(() -> {});
        }
    }

    @Test
    public void testCompareMoreThan() {
        ScoreboardEnitity se1 = new ScoreboardEnitity(600, "1");
        ScoreboardEnitity se2 = new ScoreboardEnitity(500, "2");

        System.out.println(comparer.compare(se1, se2));

        assertTrue(comparer.compare(se1, se2) > 0);
        assertFalse(comparer.compare(se1, se2) <= 0);
    }

    @Test
    public void testCompareLessThan() {
        ScoreboardEnitity se1 = new ScoreboardEnitity(400, "1");
        ScoreboardEnitity se2 = new ScoreboardEnitity(500, "2");

        System.out.println(comparer.compare(se1, se2));

        assertTrue(comparer.compare(se1, se2) < 0);
        assertFalse(comparer.compare(se1, se2) >= 0);
    }

    @Test
    public void testCompareEqual() {
        ScoreboardEnitity se1 = new ScoreboardEnitity(400, "1");
        ScoreboardEnitity se2 = new ScoreboardEnitity(400, "2");

        assertEquals(0, comparer.compare(se1, se2));
        assertFalse(comparer.compare(se1, se2) != 0);
    }
}
