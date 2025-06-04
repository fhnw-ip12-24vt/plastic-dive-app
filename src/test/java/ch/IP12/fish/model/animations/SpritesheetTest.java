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
    }

    @Test
    public void animationUniquenessTest() {
        SpriteAnimation spriteAnimation1 = Spritesheets.AtPlayerSprite.getSpriteAnimation();

        SpriteAnimation spriteAnimation2 = Spritesheets.AtPlayerSprite.getSpriteAnimation();
        assertNotEquals(spriteAnimation1, spriteAnimation2);

        spriteAnimation1 = Spritesheets.SinSprite.getSpriteAnimation();
        spriteAnimation2 = Spritesheets.SinSprite.getSpriteAnimation();
        assertNotEquals(spriteAnimation1, spriteAnimation2);
    }
}
