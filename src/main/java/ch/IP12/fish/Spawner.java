package ch.IP12.fish;

import ch.IP12.fish.model.*;
import ch.IP12.fish.model.animations.Spritesheets;

import java.util.*;

public class Spawner {
    private interface factoryPattern{
        Obstacle create(double x, double y, double speed, double maxX, double maxY, Spritesheets spritesheets,Object... params);
    }

    private static final Map<Class<? extends Obstacle>, factoryPattern> factories = new HashMap<>();

    static {
        // Register factories for each obstacle type
        registerFactory(Obstacle.class, (x,y,speed,maxX,maxY,spritesheets,params) -> new Obstacle(x, y,speed,maxX,maxY,spritesheets));
        registerFactory(AtPlayerObstacle.class, (x, y, speed, maxX, maxY, spritesheets, params) -> new AtPlayerObstacle(x, y,speed/2,maxX,maxY,spritesheets,(Player) params[0]));
        registerFactory(BounceObstacle.class, (x,y,speed,maxX,maxY,spritesheets,params) -> new BounceObstacle(x, y,speed,maxX,maxY,spritesheets));
        registerFactory(SinObstacle.class, (x,y,speed,maxX,maxY,spritesheets,params) -> new SinObstacle(x, y,speed,maxX,maxY,spritesheets));
    }

    private static void registerFactory(Class<? extends Obstacle> clazz, factoryPattern pattern) {
        factories.put(clazz, pattern);
    }

    public static <T extends Obstacle> T spawn(Class<T> obstacleClass, double speed, Object... params) {
        factoryPattern factory = factories.get(obstacleClass);
        if (factory == null) throw new IllegalArgumentException("No factory registered for " + obstacleClass.getName());
        return obstacleClass.cast(factory.create(App.WIDTH, (int) ((Math.random() * (App.HEIGHT))), speed, App.WIDTH, App.HEIGHT, Spritesheets.getRandomSpritesheet(), params));
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
        obstacles.add(spawn(classes.get(rand.nextInt(classes.size())), 300, player));
    }
}
