package ch.IP12.fish.model.obstacles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.IP12.fish.model.Player;
import ch.IP12.fish.model.World;
import org.junit.jupiter.api.*;
import ch.IP12.fish.model.animations.Spritesheets;
import ch.IP12.fish.testUtils.WatchTests;
import javafx.application.Platform;

@WatchTests
public class ForkObstacleTest {

    private static World world;

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
        Player player = mock(Player.class);

        when(player.getX()).thenReturn(100.0);
        when(player.getY()).thenReturn(100.0);
        when(world.getRandomPlayer()).thenReturn(player);
        when(world.currentTimeSeconds()).thenReturn(999.9);
    }

    @Test
    public void instanciateObstacle(){
        assertDoesNotThrow(() -> {
            Obstacle obstacle = new Obstacle(0, 0, Spritesheets.Player.getSpriteAnimation(), world);
            new ForkObstacle(obstacle);
        });
    }

    @Test
    public void adjustDirectionTest(){
        Obstacle obstacle = new Obstacle(0, 0, Spritesheets.Player.getSpriteAnimation(), world);
        ForkObstacle splitterObstacle = new ForkObstacle(obstacle);

        assertDoesNotThrow(splitterObstacle::adjustDirection);
    }
}
