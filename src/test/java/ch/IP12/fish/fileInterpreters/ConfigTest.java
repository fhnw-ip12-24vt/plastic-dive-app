package ch.IP12.fish.fileInterpreters;

import ch.IP12.fish.model.World;
import ch.IP12.fish.testUtils.WatchTests;
import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import javafx.application.Platform;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@WatchTests
public class ConfigTest {
    private static World world;
    private static Context pi4j = null;
    private static final String confName = "configTestFile";

    @BeforeAll
    public static void initJfxRuntime() {
        try {
            Platform.runLater(() -> {});
        } catch (Exception e) {
            Platform.startup(() -> {});
        }

        testDataInit();
    }

    static void testDataInit() {
        if (pi4j != null) {pi4j.shutdown();}
        pi4j = Pi4J.newAutoContext();
        world = new World(pi4j);
    }

    @AfterAll
    public static void cleanupTestFiles() {
        Path path = Path.of(confName).toAbsolutePath();

        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void testInstantiationOfConfigWhenFileDoesNotExists() {
        cleanupTestFiles();

        assertDoesNotThrow(() -> new Config(confName, world));
        assertTrue(Files.exists(Path.of(confName)));
    }

    @Test
    public void testCallOfConfigWhenFileExists() {
        testDataInit();
        cleanupTestFiles();

        assertDoesNotThrow(() -> new Config(confName, world));
        assertTrue(Files.exists(Path.of(confName)));
    }

    @Test
    public void testValueStorageOfConfig(){
        cleanupTestFiles();

        assertDoesNotThrow(() -> new Config(confName, world));
        assertTrue(Files.exists(Path.of(confName)));

        assertEquals(6, world.getConfigSize());

        assertEquals("Test simple", world.getConfigValue("testVal1"));
        assertEquals("Multiline\ntest", world.getConfigValue("testVal2"));
        assertEquals("test multiline empty\n", world.getConfigValue("testVal3"));
    }
}
