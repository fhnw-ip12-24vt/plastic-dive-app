package ch.IP12.fish.model;

import ch.IP12.fish.Spawner;

public class SplitterObstacle extends Obstacle {
    private final Class<? extends Obstacle> obstacle;
    private final double creationTime = world.currentTimeSeconds();
    private final Spawner spawner;

    public SplitterObstacle(Obstacle obstacle, Class<? extends Obstacle> obstacleToSpawn, Spawner spawner) {
        super(obstacle);
        this.obstacle = obstacleToSpawn;
        this.spawner = spawner;
    }

    @Override
    public void adjustDirection() {
        if (creationTime + 2 < world.currentTimeSeconds()) {
            spawner.remove(this);
            split();
        }
    }

    private void split() {
        System.out.println("ASDF");
        spawner.spawn(obstacle,this.getSpeed());
        spawner.spawn(obstacle,this.getSpeed());
    }
}
