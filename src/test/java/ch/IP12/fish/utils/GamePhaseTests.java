package ch.IP12.fish.utils;

import ch.IP12.fish.fileInterpreters.Logger;
import javafx.application.Platform;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static ch.IP12.fish.utils.StackUtils.getClassName;
import static ch.IP12.fish.utils.StackUtils.getMethodName;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GamePhaseTests {
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
    public void testGamePhase() {
        GamePhase phase = GamePhase.Start;
        assertEquals(phase, GamePhase.Start);

        phase = GamePhase.End;
        assertEquals(phase, GamePhase.End);
        logger.log("Test passed: " + getMethodName());
    }

    @Test
    public void testGamePhaseNext() {
        GamePhase phase = GamePhase.Start;
        phase = phase.next();

        assertEquals(phase, GamePhase.StartingAnimation);

        phase = phase.next();
        assertEquals(phase, GamePhase.Running);

        phase = GamePhase.HighScore.next();
        assertEquals(phase, GamePhase.Start);
        logger.log("Test passed: " + getMethodName());
    }
}
