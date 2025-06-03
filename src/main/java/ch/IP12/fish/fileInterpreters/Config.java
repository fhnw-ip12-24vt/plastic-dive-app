package ch.IP12.fish.fileInterpreters;

import ch.IP12.fish.model.World;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.*;

import static java.nio.file.FileSystems.newFileSystem;
import static java.nio.file.StandardOpenOption.*;

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
            URI elementsUri = Objects.requireNonNull(this.getClass().getResource("/defaultConfig/.elements")).toURI();
            URI configUri = Objects.requireNonNull(this.getClass().getResource("/defaultConfig/config")).toURI();

            if ("jar".equals(elementsUri.getScheme())) {
                // Open the JAR filesystem if it's not already opened
                FileSystem fs = newFileSystem(elementsUri, Map.of());
                elementsPath = fs.getPath("/defaultConfig/.elements");
                defaultConfigPath = fs.getPath("/defaultConfig/config");
            } else {
                // Running from a file system (e.g. in IDE or unpacked build)
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

        if (elementsPath == null || !Files.exists(elementsPath)) {
            logger.logError("Could not find config Elements definition file");
            throw new RuntimeException("Could not find config Elements definition file");
        }

        try {
            List<String> lines = Files.readAllLines(elementsPath);

            StringBuilder line = new StringBuilder();
            for (String s : lines) {

                if (s.trim().isEmpty()) continue;

                for (int i = 0; i < s.length(); i++) {
                    char tempChar = s.charAt(i);
                    line.append(tempChar);

                    if (line.toString().endsWith(";")) {
                        line = new StringBuilder(line.substring(0, line.length() - 1));
                        configElements.add(line.toString().trim().toLowerCase());
                        line = new StringBuilder();
                    }
                }
            }
        } catch (IOException e) {
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

        if (!Files.exists(path))
            throw new RuntimeException("Config not generated at specified location");

        try {
            List<String> lines = Files.readAllLines(path);
            List<String> configs = new ArrayList<>();

            StringBuilder line = new StringBuilder();
            boolean openString = false;

            for (String s : lines) {
                if (s.trim().isEmpty()) continue;

                for (int i = 0; i < s.length(); i++) {
                    char tempChar = s.charAt(i);

                    if (openString && tempChar == '"') openString = false;
                    else if (!openString && tempChar == '"') openString = true;
                    else if (tempChar == '/' && s.charAt(i + 1) == '/'&& !openString) break;
                    else line.append(tempChar);

                    if (line.toString().endsWith(";")) {
                        line = new StringBuilder(line.substring(0, line.length() - 1));
                        configs.add(line.toString());
                        line = new StringBuilder();
                    }
                }

                if (!line.toString().trim().isEmpty()) line.append("\n");
            }

            interpretConfig(configs);
        } catch (IOException e) {
            logger.logError(e.getMessage());
        }
    }

    private boolean createDefaultConfig(Path path){
        if (!Files.exists(path.getParent())) {
            try {
                Files.createDirectories(path.getParent());
                //noinspection StatementWithEmptyBody
                while (!Files.exists(path.getParent())) {}
            } catch (IOException e) {
                logger.logError("Could not create directory: " + path.getParent());
                return false;
            }
        }

        try{
            Files.createFile(path);

            //ensure a file exists before moving forward
            //noinspection StatementWithEmptyBody
            while(!Files.exists(path)){}

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
            StringBuilder text = new StringBuilder();
            boolean first = true;

            while (reader.ready()) {
                String tempLine = reader.readLine();

                if (!first) {
                    text.append("\n");
                } else {
                    first = false;
                }

                text.append(tempLine);
            }

            Files.write(path, text.toString().getBytes(), WRITE, SYNC);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void interpretConfig(List<String> lines){
        Map<String, String> config = new HashMap<>();

        lines.stream().filter(l -> !l.trim().isEmpty()).forEach(s -> {
            s = s.replace("\r", "");
            String[] sections = s.split(":");

            //if shorter than two leave out entry
            if (sections.length < 2) return;

            //
            String key = sections[0].trim().toLowerCase();
            StringBuilder value = new StringBuilder();

            //readd extra colons
            for (int i = 1; i < sections.length; i++) {
                if(i > 1) value.append(":").append(sections[i]);
                else value = new StringBuilder(sections[i].trim().toLowerCase());
            }

            //if .elements contains found key, insert it into stored configs
            if (configElements.contains(key)) {
                config.merge(key, value.toString(), (oldValue, newValue) -> newValue);
            }
        });

        if (config.size() < configElements.size()) {
            ArrayList<String> missing = new ArrayList();
            for (String key: configElements)
                if (!config.containsKey(key))
                    missing.add(key);

            StringBuilder text = new StringBuilder("Missing " + missing.size() + " element" + (missing.size() > 1 ? "s" : "") + " in config\nMissing:");

            for (String s: missing)
                text.append("   \n").append(s);

            logger.logError(text.toString());
            throw new RuntimeException(text.toString());
        }

        world.setConfigData(config);
    }
}