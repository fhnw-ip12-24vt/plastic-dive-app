package ch.IP12.fish.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConfigReader {
    private Map<String, String> config;
    private final Logger logger;

    public ConfigReader(Path path) {
        logger = Logger.getInstance();
        readFile(path);
    }

    public ConfigReader(String path) {
        this(Path.of(path));
    }

    public Map<String, String> getConfig() {
        return config;
    }

    private void readFile(Path path) {
        if (!Files.exists(path) && !Files.isDirectory(path)) {
            if (!Files.exists(path.getParent())) {
                try {
                    Files.createDirectories(path.getParent());
                } catch (IOException e) {
                    logger.logError("Could not create directory: " + path.getParent());
                }
            }
            Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxrwxrwx");
            FileAttribute<Set<PosixFilePermission>> posixFilePerms = PosixFilePermissions.asFileAttribute(permissions);

            try{
                Files.createFile(path, posixFilePerms);
            } catch (IOException e) {
                logger.logError(e.getMessage());
            }
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            List<String> lines = new ArrayList<>();

            String line = "";
            boolean openString = false;

            while (reader.ready()) {
                String tempLine = reader.readLine();

                if (tempLine.trim().isEmpty()) continue;

                for (int i = 0; i < tempLine.length(); i++) {
                    char tempChar = tempLine.charAt(i);

                    if (openString && tempChar == '"') openString = false;
                    else if (!openString && tempChar == '"') openString = true;
                    else line += tempChar;

                    if (line.endsWith(";")) {
                        line = line.substring(0, line.length() - 1);
                        lines.add(line);
                        line = "";
                    }
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