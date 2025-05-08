package ch.IP12.fish.utils;

import ch.IP12.fish.fileInterpreters.Logger;
import javafx.application.Platform;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static ch.IP12.fish.utils.StackUtils.getClassName;
import static ch.IP12.fish.utils.StackUtils.getMethodName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class StackUtilsTest {
    private static Logger logger = Logger.getInstance("testLog");

    @BeforeAll
    public static void initJfxRuntime() {
        try {
            Platform.runLater(() -> {});
        } catch (Exception e) {
            Platform.startup(() -> {});
        }

        logger.start();
        logger.log("Starting " + getClassName());

        logger.log("Test data initialized");
    }

    @AfterAll
    static void endLog(){
        logger.end();
    }

    @Test
    public void testGetClassName() {
        String className = getClassName();

        assertEquals("StackUtilsTest", className);
        logger.log("Test passed: " + getMethodName());
    }

    @Test
    public void testGetMethodName() {
        String methodName = getMethodName();

        assertEquals("testGetMethodName", methodName);
        logger.log("Test passed: " + getMethodName());
    }

    @Test
    public void testGetMethodNameWithSkip() {
        String methodName = getMethodName(1);

        assertNotEquals("testGetMethodNameWithSkip", methodName);
        logger.log("Test passed: " + getMethodName());
    }

    @Test
    public void testGetClassNameWithSkip() {
        String className = getClassName(1);

        assertNotEquals("StackUtilsTest", className);
        logger.log("Test passed: " + getClassName());
    }
}
