package ch.IP12.fish.fileInterpreters;

import ch.IP12.fish.model.World;
import ch.IP12.fish.testUtils.WatchTests;
import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import javafx.application.Platform;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@WatchTests
public class LanguageLoaderTest {
    private static World world;
    private static Context pi4j = null;

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
        new Config("testConfig", world);
    }

    @Test
    public void testInstantiationOfLanguagePack() {
        assertDoesNotThrow(() -> new LanguageLoader(world));
        assertTrue(world.getTextMapSize() > 0);
    }

    @Test
    public void testValueStorageOfConfig(){
        if (world.getTextMapSize() == 0) {
            assertDoesNotThrow(() -> new LanguageLoader(world));
            assertTrue(world.getTextMapSize() > 0);
        }

        assertEquals(6, world.getConfigSize());

        assertEquals("lorem ipsum dolor sit amet", world.getTextMapValue("victoryText"));
        assertEquals("lorem ipsum dolor sit amet", world.getTextMapValue("highscoreText"));
        assertEquals("lorem ipsum dolor sit amet", world.getTextMapValue("scanText"));
    }
}
