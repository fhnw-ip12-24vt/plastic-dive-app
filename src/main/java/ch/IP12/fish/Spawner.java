package ch.IP12.fish;

import ch.IP12.fish.model.*;
import ch.IP12.fish.model.animations.Spritesheets;

import java.util.*;

public class Spawner {
    private interface factoryPattern{
        Obstacle create(Obstacle obstacle,Object... params);
    }

    private static final Map<Class<? extends Obstacle>, factoryPattern> factories = new HashMap<>();

    static {
        // Register factories for each obstacle type
        registerFactory(Obstacle.class, (obstacle,params) -> new Obstacle(obstacle));
        registerFactory(AtPlayerObstacle.class, (obstacle,params) -> new AtPlayerObstacle(obstacle, (Player) params[0]));
        registerFactory(BounceObstacle.class, (obstacle,params) -> new BounceObstacle(obstacle));
        registerFactory(SinObstacle.class, (obstacle,params) -> new SinObstacle(obstacle));
        //registerFactory(SpliterObstacle.class, (obstacle,params) -> new SpliterObstacle(obstacle, (List<Obstacle>) params[1]));
    }

    private static void registerFactory(Class<? extends Obstacle> clazz, factoryPattern pattern) {
        factories.put(clazz, pattern);
    }

    public static <T extends Obstacle> T spawn(Class<T> obstacleClass, double speed, Object... params) {
        factoryPattern factory = factories.get(obstacleClass);
        if (factory == null) {
            throw new IllegalArgumentException("No factory registered for " + obstacleClass.getName());
        }
        Obstacle obstacleBase = new Obstacle(App.WIDTH, (int) ((Math.random() * (App.HEIGHT))), speed, App.WIDTH, App.HEIGHT, Spritesheets.getRandomSpritesheet());
        return obstacleClass.cast(factory.create(obstacleBase, params));
    }

    int money = 0;
    int moneyIncrement = 5;
    int priceScalar = 1;

    Random rand = new Random();
    List<Class<? extends Obstacle>> classes;
    List<Obstacle> obstacles;
    Spawner(List<Obstacle> obstacles) {
        this.obstacles = obstacles;
        classes = new ArrayList<>();
        classes.addAll(factories.keySet());
    }

    void spawnRandom(Player player) {
        obstacles.add(spawn(classes.get(rand.nextInt(classes.size())), 300, player, player, player));
    }
}
