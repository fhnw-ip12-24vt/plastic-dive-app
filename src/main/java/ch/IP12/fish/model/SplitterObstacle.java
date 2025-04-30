package ch.IP12.fish.model;

import ch.IP12.fish.Controller;
import ch.IP12.fish.Spawner;

import java.util.List;

public class SplitterObstacle extends Obstacle {
    private final Class<? extends Obstacle> obstacle;
    private final double creationTime = Controller.CURRENTTIMESECONDS();

    public SplitterObstacle(Obstacle obstacle, Class<? extends Obstacle> obstacleToSpawn) {
        super(obstacle);
        this.obstacle = obstacleToSpawn;
    }

    @Override
    public void adjustDirection() {
        if (creationTime + 2 < Controller.CURRENTTIMESECONDS()) {
            Spawner.remove(this);
            split();
        }
    }

    private void split() {
        System.out.println("ASDF");
        Spawner.spawn(obstacle,this.getSpeed());
        Spawner.spawn(obstacle,this.getSpeed());
    }
}
