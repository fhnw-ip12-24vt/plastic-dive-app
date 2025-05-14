package ch.IP12.fish.model.obstacles;

import javafx.scene.paint.Color;

/**
 * Obstacle that forks into two different ones after a set amount of time since it creation
 */
public class ForkObstacle extends Obstacle {
    private final double creationTime = world.currentTimeSeconds();

    /**
     * @param obstacle Obstacle object to inherit values from
     */
    public ForkObstacle(Obstacle obstacle) {
        super(obstacle);
        this.color = Color.PURPLE;
    }

    /**
     * Splits obstacle into two new regular obstacles with the same colour as this after 2 seconds
     */
    @Override
    public void adjustDirection() {
        if (creationTime + 2 < world.currentTimeSeconds()) {
            split();
        }
    }

    private void split() {
        Obstacle base = new Obstacle(this);
        base.color = Color.RED;
        Obstacle obstacle1 = new Obstacle(this);
        obstacle1.setDirection(this.getDirection() + 0.2);
        world.getObstacles().add(obstacle1);
        Obstacle obstacle2 = new Obstacle(this);
        obstacle2.setDirection(this.getDirection() - 0.2);
        world.getObstacles().add(obstacle2);
        world.removeObstacle(this);
    }
}
