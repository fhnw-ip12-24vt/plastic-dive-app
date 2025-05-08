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
                Files.createFile(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public synchronized static Logger getInstance(String name) {
        if (name == null) throw new RuntimeException("Logger path cannot be null");
        if (instance == null) instance = new Logger(Path.of("logs", name));
        return instance;
    }

    public synchronized static Logger getInstance() {
        if (instance == null) throw new RuntimeException("Logger not initialized");
        return instance;
    }

    public synchronized void log(String message) {
        String log = "\n[" + (getDateTimeString()) + "] "+(message);

        buffer.append(log);
        sync();
    }

    public synchronized void logError(String message, StackTraceElement[] stackTrace) {
        String log = "\n------Error------\n" + "[" + (getDateTimeString()) + "]\n" + (message);
        if (stackTrace != null) log += "\nStacktrace" + formatStacktraceArray(stackTrace);
        log += "\n-----------------";

        buffer.append(log);
        sync();
    }

    public synchronized void logError(String message) {
        logError(message, null);
    }

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
