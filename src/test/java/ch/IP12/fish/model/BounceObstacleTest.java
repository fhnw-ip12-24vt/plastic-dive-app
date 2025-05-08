package ch.IP12.fish.model;

import static ch.IP12.fish.utils.StackUtils.getClassName;
import static ch.IP12.fish.utils.StackUtils.getMethodName;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;

import ch.IP12.fish.fileInterpreters.Logger;
import ch.IP12.fish.model.animations.Spritesheets;
import javafx.application.Platform;

public class BounceObstacleTest {

    private static Logger logger = Logger.getInstance("testLog");
    private static World world;
    private static Player player;

    @BeforeAll
    static void initJfxRuntime() {
        try {
            Platform.runLater(() -> {});
        } catch (Exception e) {
            Platform.startup(() -> {});
        }

        logger.start();
        logger.log("Starting " + getClassName());
        logger.log("Test data initialized");
    }

    @BeforeEach
    public void initMocks(){
        world = mock(World.class);
        player = mock(Player.class);

        when(player.getX()).thenReturn(100.0);
        when(player.getY()).thenReturn(100.0);
        when(world.getRandomPlayer()).thenReturn(player);
    }

    @Test
    public void instanciateObstacle(){
        assertDoesNotThrow(() -> {
            Obstacle obstacle = new Obstacle(0, 0, Spritesheets.Player.getSpriteAnimation(), world);
            BounceObstacle bounceObstacle = new BounceObstacle(obstacle);
        });

        logger.log("Test passed: " + getMethodName());
    }

    @Test
    public void adjustDirectionTest(){
        Obstacle obstacle = new Obstacle(0, 0, Spritesheets.Player.getSpriteAnimation(), world);
        BounceObstacle bounceObstacle = new BounceObstacle(obstacle);

        // TODO
    }
}
