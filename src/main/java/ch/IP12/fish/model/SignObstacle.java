package ch.IP12.fish.model;

import ch.IP12.fish.model.animations.Spritesheets;

public class SignObstacle extends Obstacle {
    private final double maxAngleChange = Math.toRadians((Math.random()*20)+50);
    private boolean waveUp = Math.random() > 0.5;
    private static final double RADIAN_INCREMENT = Math.toRadians(1);

    public SignObstacle(int x, int y, int speed, double maxX, double maxY, Spritesheets spriteSheet) {
        super(x, y, speed, maxX, maxY, spriteSheet);
    }

    @Override
    protected void adjustDirection() {
        double direction = this.direction; // direction is in radians

        if (waveUp) {
            direction -= RADIAN_INCREMENT; // subtract small radian step
        } else {
            direction += RADIAN_INCREMENT; // add small radian step
        }

        double centerAngle = Math.PI; // 180 degrees in radians

        while (direction > (centerAngle + maxAngleChange) || direction < (centerAngle - maxAngleChange)) {
            if (direction < (centerAngle - maxAngleChange)) {
                direction += RADIAN_INCREMENT;
                waveUp = false;
            } else if (direction > (centerAngle + maxAngleChange)) {
                direction -= RADIAN_INCREMENT;
                waveUp = true;
            }
        }

        this.direction = direction;
    }
}
