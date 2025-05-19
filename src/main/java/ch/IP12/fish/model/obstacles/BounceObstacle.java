package ch.IP12.fish.model.obstacles;

import javafx.scene.paint.Color;

public class BounceObstacle extends Obstacle {
    /**
     * @param obstacle Obstacle to inherit values from
     */
    public BounceObstacle(Obstacle obstacle) {
        super(obstacle);
        setDirection(Math.toRadians((Math.random() * 120) + 120));
        this.color = Color.BURLYWOOD;
    }

    /**
     * Obstacle will bounce if it is at or over the edge
     */
    protected void adjustDirection() {
        if ((getY() + getSize()) >= getMaxY() || getY() <= 0) {
            setDirection(-(getDirection()));
        }
    }
}
