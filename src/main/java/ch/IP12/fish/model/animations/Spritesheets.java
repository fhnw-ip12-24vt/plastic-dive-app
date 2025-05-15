package ch.IP12.fish.model.animations;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Random;

/**
 * List of available Spritesheets
 */
public enum Spritesheets {
    //to add more obstacle variations add an entry to the list bellow.
    Player(new SpriteAnimation(new ImageView(new Image("playerSpritesheets/player.png")), 2, 2, 0, 1, 16, 7, Duration.millis(200))),
    SmallCluster(new SpriteAnimation(new ImageView(new Image("https://upload.wikimedia.org/wikipedia/commons/7/73/The_Horse_in_Motion.jpg")), 0, 0, 0, 0, 20, 20, Duration.millis(200))),
    MediumCluster(new SpriteAnimation(new ImageView(new Image("https://upload.wikimedia.org/wikipedia/commons/7/73/The_Horse_in_Motion.jpg")), 0, 0, 0, 0, 35, 35, Duration.millis(200))),
    LargeCluster(new SpriteAnimation(new ImageView(new Image("https://upload.wikimedia.org/wikipedia/commons/7/73/The_Horse_in_Motion.jpg")), 0, 0, 0, 0, 50, 50, Duration.millis(200))),
    easyDifficultyAnimation(new SpriteAnimation(new ImageView(new Image("https://upload.wikimedia.org/wikipedia/commons/7/73/The_Horse_in_Motion.jpg")), 3, 3, 0, 0, 100, 100, Duration.millis(200))),
    mediumDifficultyAnimation(new SpriteAnimation(new ImageView(new Image("https://upload.wikimedia.org/wikipedia/commons/7/73/The_Horse_in_Motion.jpg")), 0, 0, 0, 0, 50, 50, Duration.millis(200))),
    hardDifficultyAnimation(new SpriteAnimation(new ImageView(new Image("https://upload.wikimedia.org/wikipedia/commons/7/73/The_Horse_in_Motion.jpg")), 0, 0, 0, 0, 50, 50, Duration.millis(200))),
    ;

    public static final double spriteScaling = 3.5;
    private final SpriteAnimation spriteAnimation;

    Spritesheets(SpriteAnimation spriteAnimation) {
        this.spriteAnimation = spriteAnimation;
    }

    /**
     * Gets a random obstacle animation and sprite sheet.
     *
     * @return Obstacle sprite sheet.
     */
    public static SpriteAnimation getRandomAnimation() {
        if (values().length == 1) {
            return values()[0].getSpriteAnimation();
        }
        //random number based on the size of the sprite sheet list.
        int randInt = new Random().nextInt(1, values().length);
        return values()[randInt].getSpriteAnimation();
    }

    /**
     * Returns new instance of animation object for selected sprite sheet.
     *
     * @return Sprite animation object.
     */
    public SpriteAnimation getSpriteAnimation() {
        return new SpriteAnimation(spriteAnimation);
    }
}
