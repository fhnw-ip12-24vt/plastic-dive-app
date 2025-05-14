package ch.IP12.fish.testUtils;

import ch.IP12.fish.fileInterpreters.Logger;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalTestWatcher implements TestWatcher, BeforeAllCallback, AfterAllCallback, ExtensionContext.Store.CloseableResource {
    private final Logger logger = Logger.getInstance("testLog");
    private final Path exportPath = Path.of("testExport.txt");
    private boolean exportFirstLine;

    private Class<?> currentTestClass;
    private static final Set<Class<?>> initializedClasses = ConcurrentHashMap.newKeySet();
    private static final Map<Class<?>, List<String>> failedTestsByClass = new ConcurrentHashMap<>();

    private final boolean logToFile;
    private final boolean logStackTraces;

    private final StringBuilder exportBuffer = new StringBuilder();

    public GlobalTestWatcher() {
        this(true, true);
    }

    public GlobalTestWatcher(boolean logToFile, boolean logStackTraces) {
        this.logToFile = logToFile;
        this.logStackTraces = logStackTraces;
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        if (context.getTestClass().isPresent()) {
            Class<?> testClass = context.getTestClass().get();
            // Register global cleanup on first initialization
            if (initializedClasses.isEmpty()) {
                context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL)
                        .put("global_cleanup", this);
                if (logToFile) logger.start();

                exportFirstLine = true;
                createExportFile();
                writeToExport("[INIT] Starting tests\n");
            }

            // One-time class initialization
            if (initializedClasses.add(testClass)) {
                failedTestsByClass.putIfAbsent(testClass, new ArrayList<>());
                this.currentTestClass = testClass;

                String text = "[INIT] Starting Tests from: " + testClass.getSimpleName();

                System.out.println(text);
                if (logToFile)
                    logger.log(text);

                writeToExport(text);
            }
        }
    }

    @Override
    public void close() {
        writeToExport("[CLOSE] All tests finished");
        writeToExport("[CLOSE] Final report:");

        System.out.println("[CLOSE] All tests finished");
        System.out.println("[CLOSE] Final report:");

        if (logToFile) {
            logger.log("[CLOSE] All Tests finished");
            logger.log("[CLOSE] Final report:");
        }

        failedTestsByClass.forEach((cls, failures) -> {
            String text = "  " + cls.getSimpleName() + ": " + failures.size() + " failure" + (failures.size() == 1 ? "" : "s");
            System.out.println(text);
            if (logToFile)
                logger.log(text);

            writeToExport(text);
        });

        flushExport();

        if (logToFile) logger.end();
    }

    @Override
    public void afterAll(ExtensionContext context) {
        if (context.getTestClass().isPresent()) {
            String text = "[AFTER] Finishing Tests from: " + context.getTestClass().get().getSimpleName();
            System.out.println(text);
            if (logToFile)
                logger.log(text);

            writeToExport(text+"\n", true);
        }
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        String testName = context.getDisplayName().replace("(", "").replace(")", "");

        String text = "[DISABLED] Test Disabled: " + testName + " - Reason: " + reason.orElse("No reason provided");
        System.out.println(text);

        if (logToFile)
            logger.log(text);

        writeToExport(text);
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        String testName = context.getDisplayName().replace("(", "").replace(")", "");

        String text = "[SUCCESS] Test Successful: " + testName;
        System.out.println(text);
        if (logToFile)
            logger.log(text);

        writeToExport(text);
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        String testName = context.getDisplayName().replace("(", "").replace(")", "");

        String text = "[ABORTED] Test Aborted: " + testName + " - Reason: " + cause.getMessage();
        System.out.println(text);
        if (logToFile)
            logger.log(text);

        writeToExport(text);
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        String testName = context.getDisplayName().replace("(", "").replace(")", "");

        String text = "[FAILED] Test Failed: " + testName + " - Reason: " + cause.getMessage();
        System.out.println(text);
        if (logToFile)
            logger.log(text);

        failedTestsByClass.get(currentTestClass).add(testName);

        writeToExport(text);

        handleTestFailure(context, cause);
    }

    private void handleTestFailure(ExtensionContext context, Throwable cause) {
        System.err.println("Handling failure for: " + context.getRequiredTestClass().getSimpleName() +
                "." + context.getRequiredTestMethod().getName());
        cause.printStackTrace();

        if (logStackTraces && logToFile)
            logger.logError(cause.getMessage(), cause.getStackTrace());
        else if (logToFile)
            logger.logError(cause.getMessage());
    }

    private void createExportFile(){
        try {
            if (Files.exists(exportPath)) {
                Files.delete(exportPath);
            }
            Files.createFile(exportPath);
        } catch (IOException e) {
            logger.logError("Could not create testExport.txt", e.getStackTrace());
            throw new RuntimeException(e);
        }
    }

    private void writeToExport(String text, boolean writeToFile){
        if (!exportFirstLine) exportBuffer.append("\n");
        else exportFirstLine = false;

        exportBuffer.append(text);

        if (exportBuffer.length() > 512 || writeToFile) {
            flushExport();
        }
    }

    private void writeToExport(String text){
        writeToExport(text, false);
    }

    private void flushExport(){
        try {
            Files.write(exportPath, exportBuffer.toString().getBytes(), StandardOpenOption.APPEND, StandardOpenOption.DSYNC);
        } catch (IOException e) {
            logger.logError("Failed to write to export", e.getStackTrace());
            throw new RuntimeException(e);
        }
        exportBuffer.delete(0, exportBuffer.length());
    }
}
