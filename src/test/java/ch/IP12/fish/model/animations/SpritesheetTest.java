package ch.IP12.fish.model.animations;

import ch.IP12.fish.testUtils.WatchTests;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@WatchTests
public class SpritesheetTest {

    @BeforeAll
    static void initJfxRuntime() {
        try {
            Platform.runLater(() -> {});
        } catch (Exception e) {
            Platform.startup(() -> {});
        }
    }

    @Test
    public void getSpriteAnimationTest() {
        assertInstanceOf(SpriteAnimation.class, Spritesheets.Player.getSpriteAnimation());
        assertInstanceOf(SpriteAnimation.class, Spritesheets.getRandomAnimation());
    }

    @Test
    public void animationUniquenessTest() {
        SpriteAnimation spriteAnimation1 = Spritesheets.LargeCluster.getSpriteAnimation();

        SpriteAnimation spriteAnimation2 = Spritesheets.SmallCluster.getSpriteAnimation();
        assertNotEquals(spriteAnimation1, spriteAnimation2);

        spriteAnimation1 = Spritesheets.SmallCluster.getSpriteAnimation();
        assertNotEquals(spriteAnimation1, spriteAnimation2);

        spriteAnimation1 = Spritesheets.LargeCluster.getSpriteAnimation();
        spriteAnimation2 = Spritesheets.LargeCluster.getSpriteAnimation();
        assertNotEquals(spriteAnimation1, spriteAnimation2);
    }
}
