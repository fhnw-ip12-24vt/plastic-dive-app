package ch.IP12.fish.fileInterpreters;

import ch.IP12.fish.model.World;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Reads language pack selected in config, and stores values read from file into World's textMap variable
 */
public class LanguageLoader {
    private final Logger logger = Logger.getInstance();
    private final World world;
    private List<String> langElements;

    //default language paths (stored in resources folder)
    private final Path elementsPath;
    private final Path defaultLanguagePackPath;

    {
        try {
            elementsPath = Path.of(this.getClass().getResource("/languages/.elements").toURI());
            defaultLanguagePackPath = Path.of(this.getClass().getResource("/languages/en").toURI());
        } catch (URISyntaxException | NullPointerException e) {
            logger.logError("Could not translate internal language pack information location");
            throw new RuntimeException(e);
        }
    }

    public LanguageLoader(World world) {
        this.world = world;
        readElements();

        String selectedLanguage = world.getConfigValue("lang");
        try{
            if (selectedLanguage == null) throw new RuntimeException("No language selected in config");
            readLanguageFile(Path.of(this.getClass().getResource("/languages/" + selectedLanguage).toURI()));
        } catch(RuntimeException e){
            System.out.println("Failed to load selected language pack, attempting to load default language pack");

            if (world.getConfigValue("log").equals("detailed"))
                logger.logError("Failed to load selected language pack, attempting to load default language pack", e.getStackTrace());
            else logger.logError("Failed to load selected language pack, attempting to load default language pack");

            try {
                readLanguageFile(defaultLanguagePackPath);
            } catch(RuntimeException e1){
                if (world.getConfigValue("log").equals("detailed"))
                    logger.logError("Failed to load default language Pack", e1.getStackTrace());
                else logger.logError("Failed to load default language Pack");
                throw new RuntimeException(e1);
            }
        } catch (URISyntaxException e) {
            if (world.getConfigValue("log").equals("detailed"))
                logger.logError("Failed to translate language pack location", e.getStackTrace());
            else logger.logError("Failed to translate language pack location");
            throw new RuntimeException(e);
        }
    }

    private void readElements(){
        langElements = new ArrayList<>();

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
                        langElements.add(line.trim());
                        line = "";
                    }
                }
            }
        } catch (IOException e){
            if (world.getConfigValue("log").equals("detailed"))
                logger.logError("Could not read config Elements definition file", e.getStackTrace());
            else logger.logError("Could not read config Elements definition file");

            throw new RuntimeException("Could not read config Elements definition file");
        }
    }

    private void readLanguageFile(Path path) {
        if (!Files.exists(path)) {
            logger.logError("Language definition file does not exist");
            throw new RuntimeException("Language definition file does not exist");
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
            if (world.getConfigValue("log").equals("detailed"))
                logger.logError(e.getMessage(), e.getStackTrace());
            else logger.logError(e.getMessage());

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

            //if .elements contains found key, insert it into stored configs
            if (langElements.contains(key)) {
                textMap.merge(key, value, (oldValue, newValue) -> newValue);
            }
        });

        if (textMap.size() < langElements.size()) throw new RuntimeException("Missing text in language pack");

        world.setTextMapData(textMap);
    }
}
