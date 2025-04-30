package ch.IP12.fish.model;

import javafx.scene.paint.Color;

public class BounceObstacle extends Obstacle {
    public BounceObstacle(Obstacle obstacle) {
        super(obstacle);
        setDirection(Math.toRadians((Math.random() * 120) + 120));
        this.color = Color.BURLYWOOD;
    }

    protected void adjustDirection() {
        if ((getY() + getHeight()) >= getMaxY() || getY() <= 0) {
            setDirection(-(getDirection()));
        }
    }
}
