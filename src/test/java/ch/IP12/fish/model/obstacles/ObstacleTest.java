package ch.IP12.fish.model.obstacles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.IP12.fish.model.World;
import ch.IP12.fish.testUtils.WatchTests;
import org.junit.jupiter.api.*;
import ch.IP12.fish.model.animations.Spritesheets;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;

@WatchTests
public class ObstacleTest {
    private static World world;
    private static GraphicsContext graphicsContext;

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
        graphicsContext = mock(GraphicsContext.class);

        when(world.getWidth()).thenReturn(100.0);
    }

    @Test
    public void instanciateObstacle(){
        assertDoesNotThrow(() -> new Obstacle(0, 0, Spritesheets.Player.getSpriteAnimation(), world));
    }
    
    @Test
    public void instanciateObstacleWithCopyConstructor(){
        assertDoesNotThrow(() -> {
            Obstacle obstacle = new Obstacle(0, 0, Spritesheets.Player.getSpriteAnimation(), world);
            new Obstacle(obstacle);
        });
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
    }

    @Test
    public void drawAnimationTest(){
        Obstacle obstacle = new Obstacle(0, 0, Spritesheets.Player.getSpriteAnimation(), world);
        
        assertDoesNotThrow(() -> obstacle.drawAnimation(graphicsContext));
    }

    @Test
    public void checkOutOfBounds(){
        Obstacle obstacle = new Obstacle(0, 0, Spritesheets.Player.getSpriteAnimation(), world);
        System.out.println(obstacle.getSize());
        assertFalse(obstacle.isOutsideBounds());

        obstacle.setX(-250);
        obstacle.setY(0);

        assertFalse(obstacle.isOutsideBounds());

        obstacle.setX(-251);
        obstacle.setY(0);

        assertTrue(obstacle.isOutsideBounds());

        obstacle.setX(0);
        obstacle.setY(-250);

        assertFalse(obstacle.isOutsideBounds());

        obstacle.setX(0);
        obstacle.setY(-2510);

        assertTrue(obstacle.isOutsideBounds());
    }
}
