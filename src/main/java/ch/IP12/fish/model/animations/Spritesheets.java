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
    Player(1,new SpriteAnimation(new ImageView(new Image("assets/player.png")), 8, 8, 0, 0, 92, 41, Duration.millis(1000))),
    Player2(1,new SpriteAnimation(new ImageView(new Image("assets/player.png")), 8, 8, 0, 0, 92, 41, Duration.millis(1000))),
    AtPlayerSprite(1,new SpriteAnimation(new ImageView(new Image("assets/plasticBallsBlue.png")), 4, 4, 1, 1, 17, 17, Duration.millis(200))),
    BounceSprite(1,new SpriteAnimation(new ImageView(new Image("assets/plasticBallsBlueGreen.png")), 4, 4, 1, 1, 17, 17, Duration.millis(200))),
    ForkSprite(1,new SpriteAnimation(new ImageView(new Image("assets/plasticBallsBurple.png")), 4, 4, 1, 1, 17, 17, Duration.millis(200))),
    ObstacleSprite(1,new SpriteAnimation(new ImageView(new Image("assets/plasticBallsGreenYellow.png")), 4, 4, 1, 1, 17, 17, Duration.millis(200))),
    SinSprite(1,new SpriteAnimation(new ImageView(new Image("assets/plasticBallsRed.png")), 4, 4, 1, 1, 17, 17, Duration.millis(200))),
    easyDifficultyAnimation(3.5,new SpriteAnimation(new ImageView(new Image("assets/washingMachineEasy.png")), 9, 9, 0, 0, 100, 100, Duration.millis(400))),
    mediumDifficultyAnimation(3.5,new SpriteAnimation(new ImageView(new Image("assets/washingMachineEasy.png")), 9, 9, 0, 0, 100, 100, Duration.millis(400))),
    hardDifficultyAnimation(3.5,new SpriteAnimation(new ImageView(new Image("assets/washingMachineEasy.png")), 9, 9, 0, 0, 100, 100, Duration.millis(400))),
    ;

    public final double spriteScaling;
    private final SpriteAnimation spriteAnimation;

    Spritesheets(double spriteScaling, SpriteAnimation spriteAnimation) {
        this.spriteScaling = spriteScaling;
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
