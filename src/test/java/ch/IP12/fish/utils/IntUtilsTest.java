package ch.IP12.fish.utils;

import ch.IP12.fish.model.Player;
import ch.IP12.fish.model.World;
import ch.IP12.fish.model.animations.Spritesheets;
import ch.IP12.fish.model.obstacles.Obstacle;
import ch.IP12.fish.testUtils.WatchTests;
import com.pi4j.Pi4J;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static ch.IP12.fish.utils.IntUtils.collidesWith;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WatchTests
public class IntUtilsTest {
    World world = new World(Pi4J.newAutoContext());
    Player player = new Player(0,0,0, Spritesheets.Player.getSpriteAnimation(),null, world);
    Obstacle obstacle = new Obstacle(0,0, world);

    @BeforeAll
    public static void initJfxRuntime() {
        try {
            Platform.runLater(() -> {});
        } catch (Exception e) {
            Platform.startup(() -> {});
        }
    }

    @Test
    public void testCollidesWith() {
        assertFalse(collidesWith(player,obstacle));
        obstacle.setX(-100);
        assertTrue(collidesWith(player,obstacle));
        obstacle.setX(0);
        assertFalse(collidesWith(player,obstacle));
        obstacle.setY(500);
        obstacle.setY(-100);
        assertFalse(collidesWith(player,obstacle));
    }
}
