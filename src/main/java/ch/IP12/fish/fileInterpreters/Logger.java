package ch.IP12.fish.fileInterpreters;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.SYNC;
import static java.nio.file.StandardOpenOption.DSYNC;

public class Logger {
    private final Path path;
    private static Logger instance;

    private boolean started = false;
    private boolean finished = false;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss z");
    private StringBuilder buffer = new StringBuilder();
    private int syncCountDown = 10;

    private Logger(Path path) {
        this.path = path.toAbsolutePath();
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else if (Files.isDirectory(path)) {
            try {
                Files.delete(path);
                Files.createFile(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Retrieves instance of Logger
     * @param name Name of log file that is written to / generated
     * @return Instance of Logger
     * @throws NullPointerException When no name is provided
     */
    public synchronized static Logger getInstance(String name) {
        if (name == null || name.trim().isEmpty()) throw new NullPointerException("Logger path cannot be null");
        if (instance == null) instance = new Logger(Path.of("logs", name));
        return instance;
    }

    /**
     * Retrieves instance of Logger
     * @return Instance of Logger
     * @throws NullPointerException When no Logger has been instantiated yet
     */
    public synchronized static Logger getInstance() {
        if (instance == null) throw new NullPointerException("Logger not initialized");
        return instance;
    }

    /**
     * Writes provided message to log file
     * @param message Message to be written into log file
     */
    public synchronized void log(String message) {
        String log = "\n[" + (getDateTimeString()) + "] "+(message);
        System.out.println(log);

        buffer.append(log);
        sync();
    }

    /**
     * Writes provided message into log file and marks section as being an error
     * @param message Message to be written as an error into log file
     * @param stackTrace Stacktrace of Exception that points to where error originated
     */
    public synchronized void logError(String message, StackTraceElement[] stackTrace) {
        String log = "\n------Error------\n" + "[" + (getDateTimeString()) + "]\n" + (message);
        if (stackTrace != null) log += "\nStacktrace" + formatStacktraceArray(stackTrace);
        log += "\n-----------------";
        System.out.println(log);

        buffer.append(log);
        sync();
    }

    /**
     * Writes provided message into log file and marks section as being an error
     * @param message Message to be written as an error into log file
     */
    public synchronized void logError(String message) {
        logError(message, null);
    }

    /**
     * Writes line into log that denotes the beginning of a new log section
     */
    public synchronized void start(){
        if (started) return;

        String log = "--------- [Start  " + getDateTimeString() +"] ---------";

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            if (reader.readLine() != null) log = "\n" + log;

            Files.write(path, log.getBytes(), APPEND, SYNC);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            started = true;
        }
    }

    /**
     * Writes all remaining logs to file and a line into log that denotes the ending of a log section
     */
    public synchronized void end(){
        if (finished) return;

        String log = "\n--------- [End  " + getDateTimeString() + "] ---------\n";

        buffer.append(log);
        flush();
        finished = true;
    }

    private synchronized void sync(){
        if(syncCountDown > 0) {
            syncCountDown--;
            return;
        }

        flush();

        if (syncCountDown == 0) syncCountDown = 10;
    }

    private synchronized void flush(){
        try{
            Files.write(path, buffer.toString().getBytes(), APPEND, DSYNC);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        buffer = new StringBuilder();
    }

    private String formatStacktraceArray(StackTraceElement[] stackTrace) {
        String stacktrace = "";
        for (StackTraceElement element : stackTrace) {
            stacktrace = element.toString() + "\n" + stacktrace;
        }

        return stacktrace;
    }

    private String getDateTimeString() {
        ZonedDateTime now = ZonedDateTime.now();
        return now.format(formatter);
    }
}
