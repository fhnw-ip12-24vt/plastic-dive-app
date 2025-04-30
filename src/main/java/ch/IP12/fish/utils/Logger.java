package ch.IP12.fish.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;

import static java.nio.file.StandardOpenOption.WRITE;

public class Logger {
    private final Path path;
    private static Logger instance;

    private Logger(Path path) {
        this.path = path.toAbsolutePath();
    }

    public static Logger getInstance(Path path) {
        if (path == null) throw new RuntimeException("Logger path cannot be null");
        if (instance == null) instance = new Logger(path);
        return instance;
    }

    public static Logger getInstance(String path) {
        if (path == null) throw new RuntimeException("Logger path cannot be null");
        return getInstance(Path.of(path));
    }

    public static Logger getInstance() {
        if (instance == null) throw new RuntimeException("Logger not initialized");
        return instance;
    }

    public synchronized void log(String message) {
        String log = "\n[" + (getDateTimeString()) + "] "+(message.trim());
        try{
            Files.write(path, log.getBytes(), WRITE);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public synchronized void logError(String message, String stackTrace) {
        String log = "\n------Error------\n" + "[" + (getDateTimeString()) + "]\n" + (message.trim());
        if (stackTrace != null) log += "\nStacktrace" + stackTrace;
        log += "\n-----------------";

        try{
            Files.write(path, log.getBytes(), WRITE);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public synchronized void logError(String message) {
        logError(message, null);
    }

    private String getDateTimeString() {
        return (Calendar.getInstance()).getTime().toString().trim();
    }
}
