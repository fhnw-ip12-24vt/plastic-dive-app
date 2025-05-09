package ch.IP12.fish.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.IP12.fish.testUtils.WatchTests;
import org.junit.jupiter.api.*;

import ch.IP12.fish.model.animations.Spritesheets;
import javafx.application.Platform;

@WatchTests
public class BounceObstacleTest {
    private static World world;
    private static Player player;

    @BeforeAll
    static void initJfxRuntime() {
        try {
            Platform.runLater(() -> {});
        } catch (Exception e) {
            Platform.startup(() -> {});
        }
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
    }

    @Test
    public void adjustDirectionTest(){
        Obstacle obstacle = new Obstacle(0, 0, Spritesheets.Player.getSpriteAnimation(), world);
        BounceObstacle bounceObstacle = new BounceObstacle(obstacle);

        assertTrue(bounceObstacle.getDirection() >= 0 && bounceObstacle.getDirection() < 6.3);

        bounceObstacle.adjustDirection();

        assertTrue(bounceObstacle.getDirection() <= 0 && bounceObstacle.getDirection() > -6.3);

        bounceObstacle.adjustDirection();

        assertTrue(bounceObstacle.getDirection() >= 0 && bounceObstacle.getDirection() < 6.3);
    }
}
