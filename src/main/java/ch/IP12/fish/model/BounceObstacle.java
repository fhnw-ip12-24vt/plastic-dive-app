package ch.IP12.fish.model;

import ch.IP12.fish.Controller;
import ch.IP12.fish.model.animations.Spritesheets;
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
