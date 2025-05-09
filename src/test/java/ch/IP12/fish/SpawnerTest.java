package ch.IP12.fish;

import ch.IP12.fish.model.Obstacle;
import ch.IP12.fish.model.SinObstacle;
import ch.IP12.fish.model.World;
import ch.IP12.fish.testUtils.WatchTests;
import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import javafx.application.Platform;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@WatchTests
public class SpawnerTest {
    private static World world;
    private static Context pi4j;

    @BeforeAll
    public static void initJfxRuntime() {
        try {
            Platform.runLater(() -> {});
        } catch (Exception e) {
            Platform.startup(() -> {});
        }

        testDataInit();
    }

    private static void testDataInit() {
        if (pi4j != null) {pi4j.shutdown();}
        pi4j = Pi4J.newAutoContext();
        world = new World(pi4j);
    }

    @AfterAll
    public static void cleanup() {
        pi4j.shutdown();
    }

    @Test
    public void spawnerInstantiationTest(){
        assertDoesNotThrow(() -> new Spawner(world));
    }

    @Test
    public void createTest(){
        Spawner spawner = new Spawner(world);

        assertInstanceOf(Obstacle.class, spawner.create(Obstacle.class, 300));
        assertInstanceOf(Obstacle.class, spawner.create(SinObstacle.class, 300));
    }

    @Test
    public void spawnRandomObstacleTest(){
        if (!world.isObstaclesEmpty()) world.clearObstacles();

        Spawner spawner = new Spawner(world);
        spawner.spawnRandom();
        spawner.spawnRandom();

        assertEquals(2, world.getObstacles().size());

        world.getObstacles().forEach(obstacle -> {
           assertInstanceOf(Obstacle.class, obstacle);
        });
    }
}
