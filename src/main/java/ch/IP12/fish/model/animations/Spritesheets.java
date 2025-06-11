package ch.IP12.fish.model.animations;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * List of available Spritesheets
 */
public enum Spritesheets {
    AtPlayerSprite(1,new SpriteAnimation(new ImageView(new Image("assets/plasticBallsBlue.png")), 4, 4, 1, 1, 17, 17, Duration.millis(400))),
    BounceSprite(1,new SpriteAnimation(new ImageView(new Image("assets/plasticBallsBlueGreen.png")), 4, 4, 1, 1, 17, 17, Duration.millis(400))),
    ForkSprite(1,new SpriteAnimation(new ImageView(new Image("assets/plasticBallsBurple.png")), 4, 4, 1, 1, 17, 17, Duration.millis(400))),
    ObstacleSprite(1,new SpriteAnimation(new ImageView(new Image("assets/plasticBallsGreenYellow.png")), 4, 4, 1, 1, 17, 17, Duration.millis(400))),
    SinSprite(1,new SpriteAnimation(new ImageView(new Image("assets/plasticBallsRed.png")), 4, 4, 1, 1, 17, 17, Duration.millis(400))),//to add more obstacle variations, add an entry to the list bellow.

    Player(1,new SpriteAnimation(new ImageView(new Image("assets/player.png")), 8, 8, 0, 0, 92, 41, Duration.millis(1000))),
    Player2(1,new SpriteAnimation(new ImageView(new Image("assets/player2.png")), 8, 8, 0, 0, 92, 41, Duration.millis(1000))),

    easyStarting(3.5,new SpriteAnimation(new ImageView(new Image("assets/openingEasy.png")), 1, 1, 0, 0, 100, 50, Duration.millis(800))),
    easyOpening(3.5,new SpriteAnimation(new ImageView(new Image("assets/openingEasy.png")), 16, 16, 0, 0, 100, 50, Duration.millis(800))),
    easyWashing(3.5,new SpriteAnimation(new ImageView(new Image("assets/washingEasy.png")), 9, 9, 0, 0, 100, 100, Duration.millis(400))),

    mediumStarting(3.5,new SpriteAnimation(new ImageView(new Image("assets/openingMedium.png")), 1, 1, 0, 0, 100, 50, Duration.millis(800))),
    mediumOpening(3.5,new SpriteAnimation(new ImageView(new Image("assets/openingMedium.png")), 16, 16, 0, 0, 100, 50, Duration.millis(800))),
    mediumWashing(3.5,new SpriteAnimation(new ImageView(new Image("assets/washingMedium.png")), 9, 9, 0, 0, 100, 100, Duration.millis(400))),

    hardStarting(3.5,new SpriteAnimation(new ImageView(new Image("assets/openingHard.png")), 1, 1, 0, 0, 100, 50, Duration.millis(800))),
    hardOpening(3.5,new SpriteAnimation(new ImageView(new Image("assets/openingHard.png")), 16, 16, 0, 0, 100, 50, Duration.millis(800))),
    hardWashing(3.5,new SpriteAnimation(new ImageView(new Image("assets/washingHard.png")), 9, 9, 0, 0, 100, 100, Duration.millis(400))),
    ;

    public final double spriteScaling;
    private final SpriteAnimation spriteAnimation;

    Spritesheets(double spriteScaling, SpriteAnimation spriteAnimation) {
        this.spriteScaling = spriteScaling;
        this.spriteAnimation = spriteAnimation;
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
