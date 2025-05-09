package ch.IP12.fish.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;
import ch.IP12.fish.model.animations.Spritesheets;
import ch.IP12.fish.testUtils.WatchTests;
import javafx.application.Platform;

@WatchTests
public class SinObstacleTest {

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
            SinObstacle sinObstacle = new SinObstacle(obstacle);
        });
    }

    @Test
    public void adjustDirectionTest(){
        Obstacle obstacle = new Obstacle(0, 0, Spritesheets.Player.getSpriteAnimation(), world);
        SinObstacle sinObstacle = new SinObstacle(obstacle);

        assertEquals(Math.PI, sinObstacle.getDirection());

        sinObstacle.adjustDirection();

        assertEquals(Math.abs(Math.toRadians(1)), Math.abs(sinObstacle.getDirection()), 4);

        sinObstacle.adjustDirection();
        boolean condition = Math.abs(Math.toRadians(1) * 2) - Math.abs(sinObstacle.getDirection()) <= 0.1 || Math.abs(sinObstacle.getDirection()) <= 0.1;

        assertTrue(condition);
    }
}
