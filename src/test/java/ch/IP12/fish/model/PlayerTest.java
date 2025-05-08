package ch.IP12.fish.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;

import ch.IP12.fish.components.JoystickAnalog;
import ch.IP12.fish.model.animations.Spritesheets;
import javafx.application.Platform;

public class PlayerTest {

    private static JoystickAnalog joystick;
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
        joystick = mock(JoystickAnalog.class);
        world = mock(World.class);
    }

    @Test
    public void instanciatePlayer(){
        assertDoesNotThrow(() -> {
            Player player = new Player(0, 0, 0, Spritesheets.Player.getSpriteAnimation(), joystick, world);
        });
    }

    @Test
    public void getterSetterTest(){
        Player player = new Player(0, 0, 0, Spritesheets.Player.getSpriteAnimation(), joystick, world);
        player.setX(1);
        player.setY(2);
        player.setSpeed(3);
        player.setDirection(4);

        assertEquals(1, player.getX());
        assertEquals(2, player.getY());
        assertEquals(3, player.getSpeed());
        assertEquals(4, player.getDirection());
    }

    @Test
    public void getterReturnsCorrectValueAfterInstantiation(){
        Player player = new Player(11, 22, 33, Spritesheets.Player.getSpriteAnimation(), joystick, world);

        assertEquals(11 - Spritesheets.Player.getSpriteAnimation().getWidth() * Spritesheets.spriteScaling, player.getX());
        assertEquals(22, player.getY());
        assertEquals(33, player.getSpeed());
        assertEquals(world, player.getWorld());
    }

    @Test
    public void movePlayer(){
        when(world.getHeight()).thenReturn(1000.0);
        when(world.getWidth()).thenReturn(1000.0);
        Player player = new Player(0, 0, 1, Spritesheets.Player.getSpriteAnimation(), joystick, world);

        player.setDirection(180);
        player.move(0, 5);

        assertEquals(-56, player.getX(), 3);
        assertEquals(0, player.getY(), 3);

        player.setDirection(90);
        player.move(1, 5);

        assertEquals(-56, player.getX(), 3);
        assertEquals(4.45, player.getY(), 3);
    }
    
}
