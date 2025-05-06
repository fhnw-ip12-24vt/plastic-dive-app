package ch.IP12.fish.fileInterpreters;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.*;

import static java.nio.file.StandardOpenOption.WRITE;

public class ConfigReader {
    private Map<String, String> config;
    private List<String> configElements;
    private final Logger logger;

    public ConfigReader(Path path) {
        logger = Logger.getInstance();
        readConfig(path);
    }

    public Map<String, String> getConfig() {
        return config;
    }

    private void readElements(){
        configElements = new ArrayList<>();
        Path elementsPath = Path.of("/defaultConfig/.Elements.txt");
        if (!Files.exists(elementsPath)) {
            logger.logError("Could not find config Elements definition file.");
            throw new RuntimeException("Could not find config Elements definition file.");
        }

        try (BufferedReader reader = Files.newBufferedReader(elementsPath)) {
            String line = "";
            while (reader.ready()) {
                String tempLine = reader.readLine();

                if (tempLine.trim().isEmpty()) continue;

                for (int i = 0; i < tempLine.length(); i++) {
                    char tempChar = tempLine.charAt(i);
                    line += tempChar;

                    if (line.endsWith(";")) {
                        line = line.substring(0, line.length() - 1);
                        configElements.add(line.trim());
                        line = "";
                    }
                }
            }
        } catch (IOException e){
            logger.logError("Could not read config Elements definition file.");
            throw new RuntimeException("Could not read config Elements definition file.");
        }
    }

    private void readConfig(Path path) {
        if (!Files.exists(path) && !Files.isDirectory(path)) {
            if (!createDefaultConfig(path)) {
                logger.logError("Could not create default config file");
                return;
            }

            fillDefaultConfig(path);
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

            interpretConfig(lines);
        } catch (IOException e) {
            logger.logError(e.getMessage());
        }
    }

    private boolean createDefaultConfig(Path path){
        if (!Files.exists(path.getParent())) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                logger.logError("Could not create directory: " + path.getParent());
                return false;
            }
        }
        Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxrwxrwx");
        FileAttribute<Set<PosixFilePermission>> posixFilePerms = PosixFilePermissions.asFileAttribute(permissions);

        try{
            Files.createFile(path, posixFilePerms);
            fillDefaultConfig(path);
        } catch (IOException e) {
            logger.logError(e.getMessage());
            return false;
        }
        return true;
    }

    private void fillDefaultConfig(Path path) {
        Path defaultConfigPath = Path.of("/defaultConfig/config.txt");
        if (!Files.exists(defaultConfigPath)) {
            throw new RuntimeException("Default config file does not exist");
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            while (reader.ready()) {
                Files.write(path, reader.readLine().getBytes(), WRITE);
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void interpretConfig(List<String> lines){
        config = new HashMap<>();

        lines.parallelStream().filter(l -> !l.trim().isEmpty()).forEach(s -> {
            s = s.replace("\r", "");
            String[] sections = s.split(":");

            //if shorter than two leave out entry
            if (sections.length < 2) return;

            //
            String key = sections[0].trim();
            String value = "";

            //readd extra colons
            for (int i = 1; i < sections.length-1; i++) value += (":" + sections[i]);

            //if .Elements.txt contains found key, insert it into stored configs
            if (configElements.contains(key)) {
                config.merge(key, value, (oldValue, newValue) -> newValue);
            }
        });
    }
}