package ch.IP12.fish.model.obstacles;

import ch.IP12.fish.model.animations.Spritesheets;

public class BounceObstacle extends Obstacle {
    /**
     * @param obstacle Obstacle to inherit values from
     */
    public BounceObstacle(Obstacle obstacle) {
        super(obstacle);
        setAnimation(Spritesheets.BounceSprite.getSpriteAnimation());
        setDirection(Math.toRadians((Math.random() * 120) + 120));
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
