package ch.IP12.fish.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigReader {
    private static ConfigReader instance;
    private Map<String, String> config;
    private final Logger logger;

    private ConfigReader(Path path) {
        logger = Logger.getInstance();
        readFile(path);
    }

    public static ConfigReader getInstance(Path path) {
        if (path == null) throw new RuntimeException("Configuration path cannot be null");
        if (instance == null) instance = new ConfigReader(path);
        return instance;
    }

    public static ConfigReader getInstance(String path) {
        if (path == null) throw new RuntimeException("Configuration path cannot be null");
        return getInstance(Path.of(path));
    }

    public Map<String, String> getConfig() {
        return config;
    }

    private void readFile(Path path) {
        try (BufferedReader reader = Files.newBufferedReader(path)) {

            List<String> lines = new ArrayList<>();

            String line = "";

            while (reader.ready()) {
                String temp = reader.readLine();

                if (temp.trim().isEmpty()) continue;

                line += temp;

                if (line.endsWith(";")) {
                    line = line.substring(0, line.length() - 1);
                    lines.add(line);
                    line = "";
                }
            }

            lines.parallelStream().filter(l -> !l.trim().isEmpty()).forEach(s -> {
                String[] sections = s.split(":");
                if (sections.length != 2) return;
                String key = sections[0].trim();
                String value = sections[1].trim();

                config.merge(key, value, (oldValue, newValue) -> newValue);
            });

        } catch (IOException e) {
            logger.logError(e.getMessage());
        }
    }
}