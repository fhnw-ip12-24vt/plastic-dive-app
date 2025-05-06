package ch.IP12.fish.fileInterpreters;

import ch.IP12.fish.model.World;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageLoader {
    private final Logger logger;
    private final World world;
    private List<String> langElements;

    //default language paths (stored in resources folder)
    private final Path elementsPath;
    private final Path defaultLanguagePackPath;

    {
        try {
            elementsPath = Path.of(this.getClass().getResource("/languages/.Elements.txt").toURI());
            defaultLanguagePackPath = Path.of(this.getClass().getResource("/languages/en.txt").toURI());
        } catch (URISyntaxException | NullPointerException e) {
            throw new RuntimeException(e);
        }
    }

    public LanguageLoader(World world) {
        this.world = world;
        this.logger = Logger.getInstance();
        readElements();

        String selectedLanguage = world.getConfigValue("lang");
        try{
            readLanguageFile(Path.of(this.getClass().getResource("/languages/" + selectedLanguage + ".txt").toURI()));
        } catch(RuntimeException e){
            System.out.println("Failed to load selected language pack, attempting to load default language pack");
            logger.logError("Failed to load selected language pack, attempting to load default language pack");

            try {
                readLanguageFile(defaultLanguagePackPath);
            } catch(RuntimeException e1){
                logger.logError("Failed to load default language Pack");
                throw new RuntimeException(e1);
            }
        } catch (URISyntaxException e) {
            logger.logError("Failed to translate language pack location");
            throw new RuntimeException(e);
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

                if (!line.trim().isEmpty()) line = line + "\n";
            }

            interpretLanguageFile(lines);
        } catch (IOException e) {
            logger.logError(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void interpretLanguageFile(List<String> lines){
        Map<String, String> textMap = new HashMap<>();

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

            //if .Elements.txt contains found key, insert it into stored configs
            if (langElements.contains(key)) {
                textMap.merge(key, value, (oldValue, newValue) -> newValue);
            }
        });

        if (textMap.size() < langElements.size()) throw new RuntimeException("Missing text in language pack");

        world.setTextMapData(textMap);
    }
}
