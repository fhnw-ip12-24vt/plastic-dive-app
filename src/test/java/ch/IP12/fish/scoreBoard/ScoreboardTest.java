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

import static org.junit.jupiter.api.Assertions.*;

@WatchTests
@Disabled
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
        assertThrows(NullPointerException.class, Scoreboard::getInstance);

        Scoreboard scoreboard = Scoreboard.getInstance(world, testFileName);
        assertDoesNotThrow(() -> Scoreboard.getInstance());

        Scoreboard scoreboard2 = Scoreboard.getInstance(world, testFileName);
        assertEquals(scoreboard, scoreboard2);

        scoreboard = Scoreboard.getInstance(world);
        assertEquals(scoreboard, scoreboard2);
    }
}
