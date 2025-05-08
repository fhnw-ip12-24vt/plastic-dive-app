package ch.IP12.fish.model.animations;

import ch.IP12.fish.testUtils.WatchTests;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;

@WatchTests
public class SpritesheetTest {

    @BeforeAll
    static void initJfxRuntime() {
        try {
            Platform.runLater(() -> {});
        } catch (Exception e) {
            Platform.startup(() -> {});
        }
    }


}
