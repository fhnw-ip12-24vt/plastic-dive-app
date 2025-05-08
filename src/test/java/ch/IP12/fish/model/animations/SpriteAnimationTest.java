package ch.IP12.fish.model.animations;

import ch.IP12.fish.testUtils.WatchTests;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@WatchTests()
public class SpriteAnimationTest {
    private static ImageView imageView;

    @BeforeAll
    static void initJfxRuntime() {
        try {
            Platform.runLater(() -> {});
        } catch (Exception e) {
            Platform.startup(() -> {});
        }

        imageView = new ImageView("https://upload.wikimedia.org/wikipedia/commons/7/73/The_Horse_in_Motion.jpg");
    }

    @Test
    public void instantiationTest() {
        assertDoesNotThrow(() -> new SpriteAnimation(imageView, 0,0,0,0,0,0, Duration.millis(10)));
    }

    @Test
    public void getWidthTest() {
        SpriteAnimation sa = new SpriteAnimation(imageView, 1,1,0,0,10,10, Duration.millis(10));
        assertEquals(10, sa.getWidth());

        sa = new SpriteAnimation(imageView, 1,1,0,0,15,10, Duration.millis(10));
        assertEquals(15, sa.getWidth());

        sa = new SpriteAnimation(imageView, 1,1,0,0,1,20, Duration.millis(10));
        assertEquals(1, sa.getWidth());
    }

    @Test
    public void getHeightTest() {
        SpriteAnimation sa = new SpriteAnimation(imageView, 1,1,0,0,10,10, Duration.millis(10));
        assertEquals(10, sa.getHeight());

        sa = new SpriteAnimation(imageView, 1,1,0,0,10,15, Duration.millis(10));
        assertEquals(15, sa.getHeight());

        sa = new SpriteAnimation(imageView, 1,1,0,0,20,1, Duration.millis(10));
        assertEquals(1, sa.getHeight());
    }

    @Test
    public void getImageviewTest() {
        ImageView imgV = new ImageView("https://upload.wikimedia.org/wikipedia/commons/7/73/The_Horse_in_Motion.jpg");
        SpriteAnimation sa = new SpriteAnimation(imgV, 1,1,0,0,10,10, Duration.millis(10));

        assertEquals(imgV.getViewport(), sa.getImageView().getViewport());
    }

    @Test
    public void interpolationInvalidValueTest() {
        ImageView imgV = new ImageView("playerSpritesheets/player.png");
        SpriteAnimation sa = new SpriteAnimation(imgV, 0, 2, 0, 1, 16, 7, Duration.millis(200));

        sa.interpolate(1);
        assertEquals(imgV.getViewport(), sa.getImageView().getViewport());

        sa = new SpriteAnimation(imgV, 2, 0, 0, 1, 16, 7, Duration.millis(200));

        sa.interpolate(1);
        assertEquals(imgV.getViewport(), sa.getImageView().getViewport());

        sa = new SpriteAnimation(imgV, 2, 2, 0, 1, 0, 7, Duration.millis(200));

        sa.interpolate(1);
        assertEquals(imgV.getViewport(), sa.getImageView().getViewport());

        sa = new SpriteAnimation(imgV, 2, 2, 0, 1, 16, 0, Duration.millis(200));

        sa.interpolate(1);
        assertEquals(imgV.getViewport(), sa.getImageView().getViewport());
    }

    @Test
    public void interpolationTest() {
        ImageView imgV = new ImageView("playerSpritesheets/player.png");
        SpriteAnimation sa = new SpriteAnimation(imgV, 2, 2, 0, 1, 16, 7, Duration.millis(200));

        sa.interpolate(1);
        assertEquals(imgV.getViewport(), sa.getImageView().getViewport());
    }
}
