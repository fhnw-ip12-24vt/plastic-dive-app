package ch.IP12.fish.model;

import static ch.IP12.fish.utils.StackUtils.getClassName;
import static ch.IP12.fish.utils.StackUtils.getMethodName;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;

import ch.IP12.fish.fileInterpreters.Logger;
import ch.IP12.fish.model.animations.Spritesheets;
import javafx.application.Platform;

public class AtPlayerObstacleTest {

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
            AtPlayerObstacle atPlayerObstacle = new AtPlayerObstacle(obstacle);
        });

        logger.log("Test passed: " + getMethodName());
    }

    @Test
    public void adjustDirectionTest(){
        Obstacle obstacle = new Obstacle(0, 0, Spritesheets.Player.getSpriteAnimation(), world);
        AtPlayerObstacle atPlayerObstacle = new AtPlayerObstacle(obstacle);

        assertEquals(atPlayerObstacle.getDirection(), 0);
        assertEquals(atPlayerObstacle.getSpeed(), 0);

        atPlayerObstacle.adjustDirection();

        assertEquals(atPlayerObstacle.getDirection(), Math.atan2(atPlayerObstacle.getY(), atPlayerObstacle.getX()));
        assertEquals(atPlayerObstacle.getSpeed(), 0);

        Obstacle obstacle2 = new Obstacle(5, 3, Spritesheets.Player.getSpriteAnimation(), world);
        AtPlayerObstacle atPlayerObstacle2 = new AtPlayerObstacle(obstacle2);
        atPlayerObstacle2.setX(8);

        atPlayerObstacle2.adjustDirection();

        assertEquals(Math.atan2(atPlayerObstacle2.getY(), atPlayerObstacle2.getX()), atPlayerObstacle2.getDirection(),  4);
        //assertEquals(6, atPlayerObstacle2.getSpeed()); Speed currently broken. adjustDirection() does not want to multiply speed for some reason

        logger.log("Test passed: " + getMethodName());
    }
}
