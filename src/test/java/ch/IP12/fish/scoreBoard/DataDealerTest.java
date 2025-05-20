package ch.IP12.fish.scoreBoard;

import ch.IP12.fish.fileInterpreters.Logger;
import ch.IP12.fish.testUtils.WatchTests;
import javafx.application.Platform;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@WatchTests
@Disabled
public class DataDealerTest {
    private final String fileName = "testScores";

    @BeforeAll
    public static void initJfxRuntime() {
        try {
            Platform.runLater(() -> {});
        } catch (Exception e) {
            Platform.startup(() -> {});
        }
    }

    @AfterEach
    public void clearFiles() {
        Path filePath = Path.of(fileName+".json");

        if (Files.exists(filePath)) {
            try {
                Files.delete(filePath);
            } catch (IOException e) {
                Logger.getInstance().logError(e.getMessage(), e.getStackTrace());
                throw new RuntimeException(e);
            }
        }
    }

    @BeforeEach
    public void init() {
        try{
            (DataDealer.getInstance()).clearInstance();
        } catch (RuntimeException ignored) {
        }
    }

    @Test
    public void testGetInstance() {
        try {
            DataDealer instance = DataDealer.getInstance(fileName);

            DataDealer instance2 = DataDealer.getInstance(fileName);
            assertEquals(instance, instance2);

            instance2 = DataDealer.getInstance();
            assertEquals(instance, instance2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testPutValues() {
        try {
            DataDealer instance = DataDealer.getInstance(fileName);

            for (int i = 0; i < 10; i++){
                int finalI = i;
                assertDoesNotThrow(() -> instance.dataStore(finalI+"", 500L*finalI));
            }

            assertDoesNotThrow(() -> instance.dataStore("11", 500));

            assertEquals(10, instance.getValues().size());

        } catch (IOException e) {
            Logger.getInstance().logError(e.getMessage(), e.getStackTrace());
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetValues() {
        try{
            DataDealer instance = DataDealer.getInstance(fileName);

            Map<String, Long> expectedValues = new HashMap<>();

            for (int i = 0; i < 10; i++){
                int finalI = i;
                expectedValues.merge(finalI+"",500L*finalI , (a, b) -> b);
                assertDoesNotThrow(() -> instance.dataStore(finalI+"", 500L*finalI));
            }

            Map<String, Long> comparisonValues = instance.getValues();

            assertEquals(expectedValues.size(), comparisonValues.size());
            assertEquals(expectedValues, comparisonValues);

        } catch (IOException e) {
            Logger.getInstance().logError(e.getMessage(), e.getStackTrace());
            throw new RuntimeException(e);
        }
    }
}
