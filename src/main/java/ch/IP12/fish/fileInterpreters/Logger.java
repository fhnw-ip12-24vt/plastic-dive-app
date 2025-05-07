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
        if (instance == null) instance = new Logger(Path.of(name));
        return instance;
    }

    public synchronized static Logger getInstance() {
        if (instance == null) throw new RuntimeException("Logger not initialized");
        return instance;
    }

    public synchronized void log(String message) {
        String log = "\n[" + (getDateTimeString()) + "] "+(message.trim());

        buffer.append(log);
        sync();
    }

    public synchronized void logError(String message, StackTraceElement[] stackTrace) {
        String log = "\n------Error------\n" + "[" + (getDateTimeString()) + "]\n" + (message.trim());
        if (stackTrace != null) log += "\nStacktrace" + formatStacktraceArray(stackTrace);
        log += "\n-----------------";

        buffer.append(log);
        sync();
    }

    public synchronized void logError(String message) {
        logError(message, null);
    }

    public void start(){
        String log = "--------- [Start  " + getDateTimeString() +"] ---------";

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            if (reader.readLine() != null) log = "\n" + log;

            Files.write(path, log.getBytes(), APPEND, SYNC);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void end(){
        String log = "\n--------- [End  " + getDateTimeString() + "] ---------\n";

        buffer.append(log);
        flush();
    }

    private void sync(){
        if(syncCountDown > 0) {
            syncCountDown--;
            return;
        }

        flush();

        if (syncCountDown == 0) syncCountDown = 10;
    }

    private void flush(){
        try{
            Files.write(path, buffer.toString().getBytes(), APPEND, DSYNC);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
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
