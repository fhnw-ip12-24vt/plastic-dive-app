package ch.IP12.fish.model;

import ch.IP12.fish.model.animations.SpriteAnimation;
import ch.IP12.fish.model.animations.Spritesheets;

public class Obstacle extends Moveable{
    //randomizes whether the obstacles go up or down first
    private final int maxAngleChange = ((int)(Math.random()*20))+50;
    private boolean waveUp = Math.random() > 0.5;

    public Obstacle(int x, int y, int speed, double maxX, double maxY, Spritesheets spriteSheet) {
        this(x, y, speed, maxX, maxY, spriteSheet.getSpriteAnimation());
    }

    public Obstacle(int x, int y, int speed, double maxX, double maxY, SpriteAnimation spriteAnimation) {
        super(x, y, speed, maxX, maxY, spriteAnimation,((Math.random()+0.5)*2));
        //forces the Obstacle to move to the left side of the screen.
        direction = 180;
    }

    @Override
    public void update(double deltaTime, double strength, boolean playerMove){
        //forces the direction of the obstacle to change over time
        if (waveUp){
            direction--;
        } else {
            direction++;
        }
        while(direction > (180+maxAngleChange) || direction < (180-maxAngleChange)){
            if (direction < (180-maxAngleChange)){
                direction++;
                waveUp = false;
            }
            if (direction > (180+maxAngleChange)){
                direction--;
                waveUp = true;
            }
        }
        move(strength);
    }
}
