package ch.IP12.fish.model.obstacles;

import ch.IP12.fish.model.Player;
import javafx.scene.paint.Color;

public class AtPlayerObstacle extends Obstacle {
    private final double creationTime = world.currentTimeSeconds();
    private boolean hasChanged;
    private final Player player;

    /**
     * @param obstacle Obstacle to inherit values from
     */
    public AtPlayerObstacle(Obstacle obstacle) {
        super(obstacle);
        this.player = world.getRandomPlayer();
        this.color = Color.RED;
        this.setSpeed(this.getSpeed()/2);
    }

    /**
     * Adjusts direction to be in direction of a player's position once, after a set amount of time has passed
     */
    @Override
    protected void adjustDirection() {
        if (creationTime + 2 < world.currentTimeSeconds() && !hasChanged) {
            double x = player.getX() - getX();
            double y = player.getY() - getY();
            setDirection(Math.atan2(y, x));
            setSpeed(getSpeed()*2);
            hasChanged = true;
        }
    }
}
