package ch.IP12.fish.testUtils;

import ch.IP12.fish.fileInterpreters.Logger;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalTestWatcher implements TestWatcher, BeforeAllCallback, AfterAllCallback, ExtensionContext.Store.CloseableResource {
    private static final Logger logger = Logger.getInstance("testLog");

    private Class<?> currentTestClass;
    private static final Set<Class<?>> initializedClasses = ConcurrentHashMap.newKeySet();
    private static final Map<Class<?>, List<String>> failedTestsByClass = new ConcurrentHashMap<>();

    private final boolean logToFile;
    private final boolean logStackTraces;

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
            }

            // One-time class initialization
            if (initializedClasses.add(testClass)) {
                failedTestsByClass.putIfAbsent(testClass, new ArrayList<>());
                this.currentTestClass = testClass;
                System.out.println("[INIT] Starting Tests from: " + testClass.getSimpleName());
                if (logToFile)
                    logger.log("[INIT] Starting Tests from: " + testClass.getSimpleName());
            }
        }
    }

    @Override
    public void close() {
        System.out.println("[CLOSE] All tests finished");
        System.out.println("Final report:");

        if (logToFile) {
            logger.log("[CLOSE] All Tests finished");
            logger.log("Final report:");
        }

        failedTestsByClass.forEach((cls, failures) -> {
            System.out.printf("  %s: %d failures%n",
                    cls.getSimpleName(),
                    failures.size());
            if (logToFile)
                logger.log("  " + cls.getSimpleName() + ": " + failures.size() + " failure" + (failures.size() == 1 ? "" : "s"));
        });

        if (logToFile) logger.end();
    }

    @Override
    public void afterAll(ExtensionContext context) {
        if (context.getTestClass().isPresent()) {
            System.out.println("[AFTER] Finishing Tests from: " + context.getTestClass().get().getSimpleName());
            if (logToFile)
                logger.log("[AFTER] Ending Tests from: " + context.getTestClass().get().getSimpleName());
        }
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        String testName = context.getDisplayName().replace("(", "").replace(")", "");
        System.out.println("Test Disabled: " + testName + " - Reason: " + reason.orElse("No reason provided"));

        if (logToFile)
            logger.log("Test Disabled: " + testName + " - Reason: " + reason.orElse("No reason provided"));
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        String testName = context.getDisplayName().replace("(", "").replace(")", "");
        System.out.println("Test Passed: " + testName);
        if (logToFile)
            logger.log("Test Passed: " + testName);
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        String testName = context.getDisplayName().replace("(", "").replace(")", "");
        System.out.println("Test Aborted: " + testName + " - Reason: " + cause.getMessage());
        if (logToFile)
            logger.log("Test Aborted: " + testName + " - Reason: " + cause.getMessage());
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        String testName = context.getDisplayName().replace("(", "").replace(")", "");
        System.out.println("Test Failed: " + testName + " - Reason: " + cause.getMessage());
        if (logToFile)
            logger.log("Test Failed: " + testName + " - Reason: " + cause.getMessage());

        failedTestsByClass.get(currentTestClass.getSimpleName()).add(testName);
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
}
