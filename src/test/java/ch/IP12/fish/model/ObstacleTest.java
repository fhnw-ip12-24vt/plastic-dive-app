package ch.IP12.fish.model;

import static ch.IP12.fish.utils.StackUtils.getClassName;
import static ch.IP12.fish.utils.StackUtils.getMethodName;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.IP12.fish.fileInterpreters.Logger;
import org.junit.jupiter.api.*;
import ch.IP12.fish.model.animations.Spritesheets;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;

public class ObstacleTest {
    private static Logger logger = Logger.getInstance("testLog");
    private static World world;
    private static GraphicsContext graphicsContext;

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

    @AfterAll
    static void endLog(){
        logger.end();
    }

    @BeforeEach
    public void initMocks(){
        world = mock(World.class);
        graphicsContext = mock(GraphicsContext.class);

        when(world.getWidth()).thenReturn(100.0);
    }

    @Test
    public void instanciateObstacle(){
        assertDoesNotThrow(() -> {
            Obstacle obstacle = new Obstacle(0, 0, Spritesheets.Player.getSpriteAnimation(), world);
        });

        logger.log("Test passed: " + getMethodName());
    }
    
    @Test
    public void instanciateObstacleWithCopyConstructor(){
        assertDoesNotThrow(() -> {
            Obstacle obstacle = new Obstacle(0, 0, Spritesheets.Player.getSpriteAnimation(), world);
            Obstacle obstacleCopy = new Obstacle(obstacle);
        });

        logger.log("Test passed: " + getMethodName());
    }

    @Test
    public void updateTest(){
        Obstacle obstacle = new Obstacle(0, 0, Spritesheets.Player.getSpriteAnimation(), world);

        assertDoesNotThrow(() -> {
            obstacle.update(0);
            obstacle.update(1);
            obstacle.update(2);
            obstacle.update(999);
        });

        logger.log("Test passed: " + getMethodName());
    }

    @Test
    public void drawAnimationTest(){
        Obstacle obstacle = new Obstacle(0, 0, Spritesheets.Player.getSpriteAnimation(), world);
        
        assertDoesNotThrow(() -> {
            obstacle.drawAnimation(graphicsContext);
        });

        logger.log("Test passed: " + getMethodName());
    }

    @Test
    public void checkOutOfBounds(){
        Obstacle obstacle = new Obstacle(0, 0, Spritesheets.Player.getSpriteAnimation(), world);

        assertEquals(false, obstacle.isOutsideBounds());

        obstacle.setX(-250);
        obstacle.setY(0);

        assertEquals(false, obstacle.isOutsideBounds());

        obstacle.setX(-251);
        obstacle.setY(0);

        assertEquals(true, obstacle.isOutsideBounds());

        obstacle.setX(0);
        obstacle.setY(-250);

        assertEquals(false, obstacle.isOutsideBounds());

        obstacle.setX(0);
        obstacle.setY(-2510);

        assertEquals(true, obstacle.isOutsideBounds());

        logger.log("Test passed: " + getMethodName());
    }
}
