package ch.IP12.fish.scoreBoard;

import ch.IP12.fish.testUtils.WatchTests;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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

        System.out.println(comparer.compare(se2, se1));

        assertTrue(comparer.compare(se2, se1) > 0);
        assertFalse(comparer.compare(se2, se1) <= 0);
    }

    @Test
    public void testCompareLessThan() {
        ScoreboardEnitity se1 = new ScoreboardEnitity(400, "1");
        ScoreboardEnitity se2 = new ScoreboardEnitity(500, "2");

        System.out.println(comparer.compare(se2, se1));

        assertTrue(comparer.compare(se2, se1) < 0);
        assertFalse(comparer.compare(se2, se1) >= 0);
    }

    @Test
    public void testCompareEqual() {
        ScoreboardEnitity se1 = new ScoreboardEnitity(400, "1");
        ScoreboardEnitity se2 = new ScoreboardEnitity(400, "2");

        assertEquals(0, comparer.compare(se1, se2));
        assertFalse(comparer.compare(se1, se2) != 0);
    }

    @Test
    public void testSortUtility() {
        List<ScoreboardEnitity> entities = new ArrayList<>(List.of(new ScoreboardEnitity(600, "1"), new ScoreboardEnitity(200, "2"), new ScoreboardEnitity(700, "3"), new ScoreboardEnitity(100, "4")));

        long highestVal = 0;
        for (ScoreboardEnitity entity : entities) {
            if (highestVal < entity.getScore())
                highestVal = entity.getScore();
        }

        assertNotEquals(highestVal, entities.getFirst().getScore());

        entities.sort(comparer);

        highestVal = 0;
        int i = 1;
        for (ScoreboardEnitity entity : entities) {
            if (highestVal < entity.getScore())
                highestVal = entity.getScore();
            System.out.println(i+". "+entity.getName()+": "+entity.getScore());
        }

        assertEquals(highestVal, entities.getFirst().getScore());
    }
}
