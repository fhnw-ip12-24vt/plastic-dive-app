package ch.IP12.fish.model;

import ch.IP12.fish.Spawner;

/**
 * Obstacle that splits a set time after its creation
 */
public class SplitterObstacle extends Obstacle {
    private final Class<? extends Obstacle> obstacle;
    private final double creationTime = world.currentTimeSeconds();
    private final Spawner spawner;

    /**
     * @param obstacle Obstacle to inherit values from
     * @param obstacleToSpawn Spawns specified obstacle after a set time
     * @param spawner Spawner that is used to create obstacle
     */
    public SplitterObstacle(Obstacle obstacle, Class<? extends Obstacle> obstacleToSpawn, Spawner spawner) {
        super(obstacle);
        this.obstacle = obstacleToSpawn;
        this.spawner = spawner;
    }

    /**
     * Splits obstacle into two different ones
     */
    @Override
    public void adjustDirection() {
        if (creationTime + 2 < world.currentTimeSeconds()) {
            world.removeObstacle(this);
            split();
        }
    }

    private void split() {
        System.out.println("ASDF");
        spawner.spawn(obstacle,this.getSpeed());
        spawner.spawn(obstacle,this.getSpeed());
    }
}
