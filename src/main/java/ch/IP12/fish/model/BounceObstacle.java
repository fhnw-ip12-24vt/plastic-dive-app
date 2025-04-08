package ch.IP12.fish.model;

import ch.IP12.fish.Controller;
import ch.IP12.fish.model.animations.Spritesheets;

public class BounceObstacle extends Obstacle {

    public BounceObstacle(int x, int y, int speed, double maxX, double maxY, Spritesheets spriteSheet) {
        super(x, y, speed, maxX, maxY, spriteSheet);
        setDirection(Math.toRadians((Math.random() * 160) + 100));
    }
    protected void adjustDirection() {
        if ((getY() + getHeight()) >= getMaxY() || getY() <= 0) {
            setDirection(-(getDirection()));
        }
    }
}
