package ch.IP12.fish;

import ch.IP12.fish.model.*;
import ch.IP12.fish.model.animations.Spritesheets;
import ch.IP12.fish.model.obstacles.*;

import java.util.*;

public class Spawner {
    private final World world;

    private interface factoryPattern {
        Obstacle create(Obstacle obstacle, Object... params);
    }

    private final Map<Class<? extends Obstacle>, factoryPattern> factories = new HashMap<>();
    {
        // Register factories for each obstacle type
        registerFactory(Obstacle.class, (obstacle, params) -> new Obstacle(obstacle));
        registerFactory(AtPlayerObstacle.class, (obstacle, params) -> new AtPlayerObstacle(obstacle));
        registerFactory(BounceObstacle.class, (obstacle, params) -> new BounceObstacle(obstacle));
        registerFactory(SinObstacle.class, (obstacle, params) -> new SinObstacle(obstacle));
        registerFactory(ForkObstacle.class, (obstacle, params) -> new ForkObstacle(obstacle));
    }

    private final Random rand = new Random();
    private final List<Class<? extends Obstacle>> classes = new ArrayList<>(this.factories.keySet());

    /**
     * @param world DTO (Data Transfer Object)
     */
    public Spawner(World world) {
        this.world = world;
    }

    private void registerFactory(Class<? extends Obstacle> clazz, factoryPattern pattern) {
        factories.put(clazz, pattern);
    }

    /**
     * @param obstacleClass Class of Obstacle type to create
     * @param speed Obstacle's speed
     * @param params any other required params for the obstacle to be created
     * @return New instance of provided Obstacle
     * @param <T> Class that must extend Obstacle
     */
    public <T extends Obstacle> T create(Class<T> obstacleClass, double speed, Object... params) {
        int spawnAreaBoundaryLimits = 150;
        factoryPattern factory = factories.get(obstacleClass);
        if (factory == null) {
            throw new IllegalArgumentException("No factory registered for " + obstacleClass.getName());
        }
        Obstacle obstacleBase = new Obstacle(rand.nextDouble(1, (((int)world.getHeight())-(spawnAreaBoundaryLimits-1))), speed, world);
        return obstacleClass.cast(factory.create(obstacleBase, params));
    }

    /**
     * Spawns random Obstacle from the list of registered factory functions
     */
    public void spawnRandom() {
        world.getObstacles().add(create(classes.get(rand.nextInt(classes.size())), rand.nextDouble(200,300)));
    }
}
