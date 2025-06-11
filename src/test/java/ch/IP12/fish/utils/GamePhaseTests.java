package ch.IP12.fish.utils;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GamePhaseTests {

    @BeforeAll
    public static void initJfxRuntime() {
        try {
            Platform.runLater(() -> {});
        } catch (Exception e) {
            Platform.startup(() -> {});
        }
    }

    @Test
    public void testGamePhase() {
        GamePhase phase = GamePhase.Start;
        assertEquals(phase, GamePhase.Start);

        phase = GamePhase.End;
        assertEquals(phase, GamePhase.End);
    }

    @Test
    public void testGamePhaseNext() {
        GamePhase phase = GamePhase.Start;
        phase = phase.next();

        assertEquals(GamePhase.StartingAnimation, phase);

        phase = phase.next();
        assertEquals(GamePhase.Running, phase);

        phase = GamePhase.HighScore.next();
        assertEquals(GamePhase.Start, phase);
    }
}
