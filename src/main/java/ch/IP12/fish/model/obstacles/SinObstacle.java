package ch.IP12.fish.model.obstacles;

import javafx.scene.paint.Color;

public class SinObstacle extends Obstacle {
    //Randomised wave generation (random change and random start direction)
    private final double maxAngleChange = Math.toRadians((Math.random()*20)+50);
    private boolean waveUp = Math.random() > 0.5;

    //one degree as radian static to save processing time
    private static final double RADIAN_INCREMENT = Math.toRadians(1);

    /**
     * @param obstacle Obstacle to inherit values from
     */
    public SinObstacle(Obstacle obstacle) {
        super(obstacle);
        this.color = Color.YELLOW;
    }

    /**
     * Adjusts direction of this obstacle in line with a Sin and CoSin wave
     */
    @Override
    protected void adjustDirection() {
        double direction = getDirection(); // direction is in radians

        //change angle in direction of wave movement (up ord down)
        if (waveUp) {
            direction -= RADIAN_INCREMENT;
        } else {
            direction += RADIAN_INCREMENT;
        }

        //Base angle for direction
        double centerAngle = Math.PI;

        while (direction > (centerAngle + maxAngleChange) || direction < (centerAngle - maxAngleChange)) {
            if (direction < (centerAngle - maxAngleChange)) {
                direction += RADIAN_INCREMENT;
                waveUp = false;
            } else if (direction > (centerAngle + maxAngleChange)) {
                direction -= RADIAN_INCREMENT;
                waveUp = true;
            }
        }

        setDirection(direction);
    }
}
