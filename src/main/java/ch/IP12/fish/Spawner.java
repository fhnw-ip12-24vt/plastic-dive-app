package ch.IP12.fish;

import ch.IP12.fish.model.*;
import ch.IP12.fish.model.animations.Spritesheets;

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

    Random rand = new Random();
    List<Class<? extends Obstacle>> classes = new ArrayList<>(this.factories.keySet());

    public Spawner(World world) {
        this.world = world;
    }

    private void registerFactory(Class<? extends Obstacle> clazz, factoryPattern pattern) {
        factories.put(clazz, pattern);
    }

    public <T extends Obstacle> T create(Class<T> obstacleClass, double speed, Object... params) {
        factoryPattern factory = factories.get(obstacleClass);
        if (factory == null) {
            throw new IllegalArgumentException("No factory registered for " + obstacleClass.getName());
        }
        Obstacle obstacleBase = new Obstacle(rand.nextDouble((int)world.getWidth()), speed, Spritesheets.getRandomAnimation(), world);
        System.out.println(obstacleClass.getName());
        return obstacleClass.cast(factory.create(obstacleBase, params));
    }

    public void spawnRandom() {
        world.getObstacles().add(create(classes.get(rand.nextInt(classes.size())), 300));
    }
}
