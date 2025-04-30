package ch.IP12.fish;

import ch.IP12.fish.model.*;
import ch.IP12.fish.model.animations.Spritesheets;

import java.util.*;

public class Spawner {

    private interface factoryPattern {
        Obstacle create(Obstacle obstacle, Object... params);
    }

    private static final Map<Class<? extends Obstacle>, factoryPattern> factories = new HashMap<>();

    static {
        // Register factories for each obstacle type
        registerFactory(Obstacle.class, (obstacle, params) -> new Obstacle(obstacle));
        registerFactory(AtPlayerObstacle.class, (obstacle, params) -> new AtPlayerObstacle(obstacle, (Player) params[1]));
        registerFactory(BounceObstacle.class, (obstacle, params) -> new BounceObstacle(obstacle));
        registerFactory(SinObstacle.class, (obstacle, params) -> new SinObstacle(obstacle));
        registerFactory(SplitterObstacle.class, (obstacle, params) -> new SplitterObstacle(obstacle, (Class<? extends Obstacle>) params[0]));
    }

    static Random rand = new Random();
    static List<Class<? extends Obstacle>> classes = new ArrayList<>(factories.keySet());

    private Spawner() {
    }

    private static void registerFactory(Class<? extends Obstacle> clazz, factoryPattern pattern) {
        factories.put(clazz, pattern);
    }

    public static <T extends Obstacle> T create(Class<T> obstacleClass, double speed, Object... params) {
        factoryPattern factory = factories.get(obstacleClass);
        if (factory == null) {
            throw new IllegalArgumentException("No factory registered for " + obstacleClass.getName());
        }
        Obstacle obstacleBase = new Obstacle(App.WIDTH, rand.nextInt(App.WIDTH), speed, App.WIDTH, App.HEIGHT, Spritesheets.getRandomSpritesheet());
        System.out.println(obstacleClass.getName());
        return obstacleClass.cast(factory.create(obstacleBase, params));
    }

    public static void spawnRandom(Player player) {
        obstacles.add(create(classes.get(rand.nextInt(classes.size())), 300, classes.get(rand.nextInt(classes.size()-1)), player));
    }

    public static <T extends Obstacle> void spawn(Class<T> obstacleClass, double speed, Object... params) {
        obstacles.add(create(obstacleClass, speed, classes.get(rand.nextInt(classes.size())), params));
    }

    public static void remove(List<Obstacle> deletionList) {
        obstacles.removeAll(deletionList);
    }

    public static void remove(Obstacle obstacle) {
        obstacles.remove(obstacle);
    }
}
