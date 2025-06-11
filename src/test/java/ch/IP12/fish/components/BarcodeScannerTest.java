package ch.IP12.fish.components;

import ch.IP12.fish.model.World;
import ch.IP12.fish.testUtils.WatchTests;
import ch.IP12.fish.utils.Difficulty;
import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@WatchTests
public class BarcodeScannerTest {
    private static Context pi4j;
    private static World world;
    private Scene scene;

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

    @AfterEach
    public void clearScene(){
        scene = null;
    }

    private void simulateKeySequence(String charSequence) {
        for (char c : charSequence.toCharArray()) {
            simulateKeyPress(KeyCode.getKeyCode(c+""));
        }

        simulateKeyPress(KeyCode.ENTER);
    }

    private void simulateKeyPress(KeyCode keyCode) {
        try{
            KeyEvent keyPressed = new KeyEvent(KeyEvent.KEY_PRESSED, keyCode.getChar(), keyCode.getChar(), keyCode, false, false, false, false);
            KeyEvent keyReleased = new KeyEvent(KeyEvent.KEY_RELEASED, keyCode.getChar(), keyCode.getChar(), keyCode, false, false, false, false);

            Event.fireEvent(scene, keyPressed);
            Event.fireEvent(scene, keyReleased);
        } catch (Exception ignored) {}
    }

    @Test
    public void testInstantiation(){
        scene = new Scene(new StackPane(), 300, 200);

        assertDoesNotThrow(() -> new BarcodeScanner(scene, world));
    }

    @Test
    public void testBarcodeScan(){
        scene = new Scene(new StackPane(), 300, 200);

        BarcodeScanner scanner = new BarcodeScanner(scene, world);

        scanner.startListening();
        simulateKeySequence("5181539527925");
        assertEquals(Difficulty.Easy, world.getDifficulty());

        scanner.startListening();
        simulateKeySequence("6211734858490");
        assertEquals(Difficulty.Medium, world.getDifficulty());

        scanner.startListening();
        simulateKeySequence("7751064387950");
        assertEquals(Difficulty.Hard, world.getDifficulty());
    }

    @Test
    public void testInvalidBarcodeScan(){
        scene = new Scene(new StackPane(), 300, 200);

        BarcodeScanner scanner = new BarcodeScanner(scene, world);

        scanner.startListening();
        simulateKeySequence("7122841455535");
        assertNull(world.getDifficulty());
    }
}
