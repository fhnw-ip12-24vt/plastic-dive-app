package ch.IP12.fish.scoreBoard;

import ch.IP12.fish.fileInterpreters.Logger;
import ch.IP12.fish.model.World;
import ch.IP12.fish.testUtils.WatchTests;
import com.pi4j.Pi4J;
import javafx.application.Platform;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@WatchTests
public class ScoreboardTest {
    private final World world = new World(Pi4J.newAutoContext());
    private final String testFileName = "testScoreboard";

    @BeforeAll
    public static void initJfxRuntime() {
        try {
            Platform.runLater(() -> {});
        } catch (Exception e) {
            Platform.startup(() -> {});
        }
    }

    @AfterEach
    public void clearFiles() {
        Path file = Path.of(testFileName+".json");

        if (Files.exists(file)) {
            try {
                Files.delete(file);
            } catch (IOException e) {
                Logger.getInstance().logError(e.getMessage(), e.getStackTrace());
                throw new RuntimeException(e);
            }
        }
    }

    @BeforeEach
    public void init() {
        try{
            (DataDealer.getInstance()).clearInstance();
        } catch (RuntimeException ignored) {
        }
    }

    @Test
    public void testGetInstance() {
        Scoreboard scoreboard = Scoreboard.getInstance(world, testFileName);
        assertDoesNotThrow(() -> Scoreboard.getInstance());

        Scoreboard scoreboard2 = Scoreboard.getInstance(world, testFileName);
        assertEquals(scoreboard, scoreboard2);

        scoreboard = Scoreboard.getInstance(world);
        assertEquals(scoreboard, scoreboard2);
    }

    @Test
    public void testGetScore() {
        Scoreboard scoreboard = Scoreboard.getInstance(world, testFileName);

        AtomicInteger vals = new AtomicInteger();
        assertDoesNotThrow(() -> vals.set(scoreboard.getList().length));
        assertEquals(0, scoreboard.getList().length);

        DataDealer dataDealer = DataDealer.getInstance();

        for (int i = 0; i < 12; i++) {
            dataDealer.dataStore(i+"", i*50.0);
        }

        ScoreboardEnitity[] scoreboardList = scoreboard.getList();

        assertEquals(dataDealer.getValues().size(), scoreboardList.length);

        for (ScoreboardEnitity scoreboardEnitity : scoreboardList) {
            System.out.println(scoreboardEnitity.name() + ": " + scoreboardEnitity.score());
        }
    }
}
