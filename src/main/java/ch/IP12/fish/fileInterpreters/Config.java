package ch.IP12.fish.fileInterpreters;

import ch.IP12.fish.model.World;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.*;

import static java.nio.file.StandardOpenOption.WRITE;

/**
 * Reads Config values and stores them in World's config variable
 */
public class Config {
    private final World world;
    private List<String> configElements;
    private final Logger logger = Logger.getInstance();

    //default config paths (stored in resources folder)
    private final Path elementsPath;
    private final Path defaultConfigPath;

    {
        try {
            //Create URI default config
            URI elementsUri = this.getClass().getResource("/defaultConfig/.elements").toURI();
            URI configUri = this.getClass().getResource("/defaultConfig/config").toURI();

            if ("jar".equals(elementsUri.getScheme())) {
                // Open the JAR filesystem if it's not already opened
                FileSystem fs = FileSystems.newFileSystem(elementsUri, Map.of());
                elementsPath = fs.getPath("/defaultConfig/.elements");
                defaultConfigPath = fs.getPath("/defaultConfig/config");
            } else {
                // Running from file system (e.g., in IDE or unpacked build)
                elementsPath = Path.of(elementsUri);
                defaultConfigPath = Path.of(configUri);
            }
        } catch (URISyntaxException | NullPointerException | IOException e) {
            logger.logError("Could not translate default config information location");
            throw new RuntimeException(e);
        }
    }


    public Config(String name, World world) {
        this.world = world;
        readElements();
        readConfig((Path.of(name)).toAbsolutePath());
    }

    private void readElements(){
        configElements = new ArrayList<>();

        if (!Files.exists(elementsPath)) {
            logger.logError("Could not find config Elements definition file");
            throw new RuntimeException("Could not find config Elements definition file");
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
            logger.logError("Could not read config Elements definition file");
            throw new RuntimeException("Could not read config Elements definition file");
        }
    }

    private void readConfig(Path path) {
        if (!Files.exists(path) && !Files.isDirectory(path)) {
            if (!createDefaultConfig(path)) {
                logger.logError("Could not create default config file");
                return;
            }

            fillDefaultConfig(path);
        } else if (!Files.exists(path)) {
            throw new RuntimeException("Invalid config file location");
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
                    else if (tempChar == '/' && tempLine.charAt(i + 1) == '/'&& !openString) break;
                    else line += tempChar;

                    if (line.endsWith(";")) {
                        line = line.substring(0, line.length() - 1);
                        lines.add(line);
                        line = "";
                    }
                }

                if (!line.trim().isEmpty()) line = line + "\n";
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

        try{
            Files.createFile(path);
            fillDefaultConfig(path);
        } catch (IOException e) {
            logger.logError(e.getMessage());
            return false;
        }
        return true;
    }

    private void fillDefaultConfig(Path path) {
        if (!Files.exists(defaultConfigPath)) {
            throw new RuntimeException("Default config file does not exist");
        }

        try (BufferedReader reader = Files.newBufferedReader(defaultConfigPath)) {
            String text = "";
            boolean first = true;

            while (reader.ready()) {
                String tempLine = reader.readLine();

                if (!first) {
                    text = text + "\n";
                } else {
                    first = false;
                }

                text = text + tempLine;
            }

            Files.write(path, text.getBytes(), WRITE);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void interpretConfig(List<String> lines){
        Map<String, String> config = new HashMap<>();

        lines.parallelStream().filter(l -> !l.trim().isEmpty()).forEach(s -> {
            s = s.replace("\r", "");
            String[] sections = s.split(":");

            //if shorter than two leave out entry
            if (sections.length < 2) return;

            //
            String key = sections[0].trim();
            String value = "";

            //readd extra colons
            for (int i = 1; i < sections.length; i++) {
                if(i > 1) value = value + (":" + sections[i]);
                else value = sections[i];
            }

            //if .elements contains found key, insert it into stored configs
            if (configElements.contains(key)) {
                config.merge(key, value, (oldValue, newValue) -> newValue);
            }
        });

        if (config.size() < configElements.size()) {
            logger.logError("Missing config elements");
            throw new RuntimeException("Missing config elements");
        }

        world.setConfigData(config);
    }
}