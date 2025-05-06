package ch.IP12.fish.fileInterpreters;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class languageLoader {
    private final Logger logger;
    private List<String> langElements;
    private Map<String, String> textMap;

    //default language paths (stored in resources folder)
    private final Path elementsPath = Path.of("languages/.Elements.txt");

    private final Path defaultEnPath = Path.of("languages/en.txt");
    private final Path defaultDePath = Path.of("languages/de.txt");
    private final Path defaultFrPath = Path.of("languages/fr.txt");

    public languageLoader(Config config, Logger logger) {
        this.logger = logger;
        readElements();
        switch (config.getConfigValue("lang")) {
            case "en": readLanguageFile(defaultEnPath); break;
            case "de": readLanguageFile(defaultDePath); break;
            case "fr": readLanguageFile(defaultFrPath); break;
            default: {
                readLanguageFile(defaultEnPath);
                logger.logError("Unknown language file: " + config.getConfigValue("lang") + ", loading english instead");
            }
        }
    }

    private void readElements(){
        langElements = new ArrayList<>();

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
                        langElements.add(line.trim());
                        line = "";
                    }
                }
            }
        } catch (IOException e){
            logger.logError("Could not read config Elements definition file.");
            throw new RuntimeException("Could not read config Elements definition file.");
        }
    }

    private void readLanguageFile(Path path) {
        if (!Files.exists(path)) {
            logger.logError("Could not find language definition file.");
            throw new RuntimeException("Could not find language definition file.");
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
            }

            interpretLanguageFile(lines);
        } catch (IOException e) {
            logger.logError(e.getMessage());
        }
    }

    private void interpretLanguageFile(List<String> lines){
        textMap = new HashMap<>();

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
            if (langElements.contains(key)) {
                textMap.merge(key, value, (oldValue, newValue) -> newValue);
            }
        });
    }
}
