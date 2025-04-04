package ch.IP12.fish.model;

import ch.IP12.fish.model.animations.Spritesheets;

public class SignObstacle extends Obstacle {
    private final int maxAngleChange = ((int)(Math.random()*20))+50;
    private boolean waveUp = Math.random() > 0.5;

    public SignObstacle(int x, int y, int speed, double maxX, double maxY, Spritesheets spriteSheet) {
        super(x, y, speed, maxX, maxY, spriteSheet);
    }

    @Override
    protected void adjustDirection() {
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
    }
}
