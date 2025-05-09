package ch.IP12.fish.model;

import ch.IP12.fish.Spawner;
import javafx.scene.paint.Color;

public class ForkObstacle extends Obstacle {
    private final double creationTime = world.currentTimeSeconds();

    public ForkObstacle(Obstacle obstacle) {
        super(obstacle);
        this.color = Color.PURPLE;
    }

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
        obstacle1.setDirection(this.getDirection() + 0.7);
        world.getObstacles().add(obstacle1);
        Obstacle obstacle2 = new Obstacle(this);
        obstacle2.setDirection(this.getDirection() - 0.7);
        world.getObstacles().add(obstacle2);
        world.removeObstacle(this);
    }
}
