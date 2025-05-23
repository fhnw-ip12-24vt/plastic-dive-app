package ch.IP12.fish.fileInterpreters;

import ch.IP12.fish.model.World;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.*;

/**
 * Reads language pack selected in config, and stores values read from a file into the World's textMap variable
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
            //create URI to language packs
            String elemPathString = "/languages/.elements";
            String defaultLangPathString = "/languages/en";

            URI elementsUri = Objects.requireNonNull(this.getClass().getResource(elemPathString)).toURI();
            URI langUri = Objects.requireNonNull(this.getClass().getResource(defaultLangPathString)).toURI();

            if ("jar".equals(elementsUri.getScheme())) {
                // Use filesystem for interpreting Jar URIs
                FileSystem fs = FileSystems.getFileSystem(elementsUri);
                elementsPath = fs.getPath(elemPathString);
                defaultLanguagePackPath = fs.getPath(defaultLangPathString);
            } else {
                //use default filesystem (necessary for running from IDE)
                elementsPath = Path.of(elementsUri);
                defaultLanguagePackPath = Path.of(langUri);
            }
        } catch (URISyntaxException | NullPointerException e) {
            logger.logError("Could not translate default config information location");
            throw new RuntimeException(e);
        }
    }

    public LanguageLoader(World world) {
        this.world = world;
        readElements();

        String selectedLanguage = world.getConfigValue("lang");
        try{
            if (selectedLanguage == null) throw new RuntimeException("No language selected in config");
            readLanguageFile(Path.of(Objects.requireNonNull(this.getClass().getResource("/languages/" + selectedLanguage))
                    .toURI()).toAbsolutePath());
        } catch(RuntimeException e){
            String errorText = "Failed to load selected language pack, attempting to load default language pack. Reason: " + e.getMessage();
            System.out.println(errorText);

            logger.logError(errorText, world.getConfigValue("log").equals("detailed") ? e.getStackTrace(): null);

            try {
                readLanguageFile(defaultLanguagePackPath);
            } catch(RuntimeException e1){
                errorText = "Failed to load default language Pack. Reason: " + e1.getMessage();

                System.out.println(errorText);
                logger.logError(errorText, world.getConfigValue("log").equals("detailed") ? e1.getStackTrace(): null);
                throw new RuntimeException(e1);
            }
        } catch (URISyntaxException e) {
            logger.logError("Failed to translate language pack location", world.getConfigValue("log").equals("detailed") ? e.getStackTrace(): null);
            throw new RuntimeException(e);
        }
    }

    private void readElements(){
        langElements = new ArrayList<>();

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
                        langElements.add(line.toString().trim().toLowerCase());
                        line = new StringBuilder();
                    }
                }
            }
        } catch (IOException e){
            String errorText = "Could not read language Elements definition file";

            logger.logError(errorText, world.getConfigValue("log").equals("detailed") ? e.getStackTrace(): null);
            throw new RuntimeException(errorText, e.getCause());
        }
    }

    private void readLanguageFile(Path path) {
        if (!Files.exists(path)) {
            logger.logError("Language definition file does not exist");
            throw new RuntimeException("Language definition file does not exist");
        }

        try {
            List<String> lines = Files.readAllLines(path);
            List<String> langTexts = new ArrayList<>();

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
                        langTexts.add(line.toString());
                        line = new StringBuilder();
                    }
                }

                if (!line.toString().trim().isEmpty()) line.append("\n");
            }

            interpretLanguageFile(langTexts);
        } catch (IOException e) {
            logger.logError(e.getMessage(), world.getConfigValue("log").equals("detailed") ? e.getStackTrace(): null);

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
            String key = sections[0].trim().toLowerCase();
            StringBuilder value = new StringBuilder();

            //readd extra colons
            for (int i = 1; i < sections.length; i++) {
                if(i > 1) value.append(":").append(sections[i]);
                else value = new StringBuilder(sections[i]);
            }

            //if .elements contains found key, insert it into stored configs
            if (langElements.contains(key)) {
                textMap.merge(key, value.toString(), (oldValue, newValue) -> newValue);
            }
        });

        if (textMap.size() < langElements.size()) {
            ArrayList<String> missing = new ArrayList<>();
            for (String key: langElements)
                if (!textMap.containsKey(key))
                    missing.add(key);

            StringBuilder text = new StringBuilder("Missing " + missing.size() + " text element" + (missing.size() > 1 ? "s" : "") + " in language pack\nMissing:");

            for (String s: missing)
                text.append("   \n").append(s);

            logger.logError(text.toString());
            throw new RuntimeException(text.toString());
        }

        world.setTextMapData(textMap);
    }
}
