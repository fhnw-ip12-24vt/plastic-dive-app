package ch.IP12.fish.scoreboard;

import ch.IP12.fish.scoreBoard.DataDealer;
import ch.IP12.fish.testUtils.WatchTests;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@WatchTests
public class DataDealerTest {
    @BeforeAll
    public static void initJfxRuntime() {
        try {
            Platform.runLater(() -> {});
        } catch (Exception e) {
            Platform.startup(() -> {});
        }
    }

    @Test
    public void testGetInstance() {
        try {
            DataDealer instance = DataDealer.getInstance("testScores");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
