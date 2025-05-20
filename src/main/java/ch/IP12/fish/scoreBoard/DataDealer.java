package ch.IP12.fish.scoreBoard;

import ch.IP12.fish.fileInterpreters.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;

public class DataDealer {
    //variable for easy access to file
    private final String fileName;
    private final Path filePath;

    private final int sizeLimit;
    private final Logger logger = Logger.getInstance();

    private static DataDealer instance = null;

    private DataDealer(String fileName, int boardSizeLimit) throws IOException {
        //check for file existence and creation if it doesn't
        sizeLimit = boardSizeLimit;
        fileName += ".json";
        this.fileName = fileName;

        filePath = Path.of(fileName);
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }

        //noinspection StatementWithEmptyBody
        while (!Files.exists(filePath)) {}
    }

    /**
     * Create an instance of the DataDealer object or returns it if one already exists.
     * **Notice: BoardSizeLimit is set to 10**
     * @param fileName The name of the file that will be written to.
     * @return DataDealer instance
     * @throws IOException File interaction can throw an error, cannot be avoided.
     */
    public static DataDealer getInstance(String fileName) throws IOException {
        return getInstance(fileName, 10);
    }

    /**
     * Create an instance of the DataDealer object or returns it if one already exists.
     * @return DataDealer instance
     * @throws NullPointerException If not instantiated before this method is called
     */
    public static DataDealer getInstance() {
        if (instance == null) {
            throw new NullPointerException("DataDealer not initialized with valid values yet");
        }
        return instance;
    }

    /**
     * Create an instance of the DataDealer object or returns it if one already exists.
     * @param fileName The name of the file that will be written to.
     * @param boardSizeLimit how large the leaderboard can be.
     * @return DataDealer instance
     * @throws IOException File interaction can throw an error, cannot be avoided.
     */
    public static DataDealer getInstance(String fileName, int boardSizeLimit) throws IOException {
        if(instance == null){
            instance = new DataDealer(fileName, boardSizeLimit);
        }
        return instance;
    }

    /**
     * Write score and the player's name to JSON file
     * @param name Name that the player chose
     * @param score The player's score
     */
    //IDE doesn't show warnings for "put" or "add" methods
    @SuppressWarnings("unchecked")
    public void dataStore(String name, long score){
        //create variables for JSON and content writing functionality
        boolean b = false;
        JSONObject json = new JSONObject();
        JSONObject finalO = new JSONObject();
        JSONArray jsonA = new JSONArray();

        //add values to JSON Object
        json.put("Name", name);
        json.put("Score", score);

        //write to file if it has entries already
        try {
            //add existing entries to JSON Array and write into file as new JSON object
            jsonA.addAll((JSONArray) JSONFileParser().get("Highscores"));
            jsonA.add(json);
            //sorts from highest to lowest
            jsonA.sort((o1, o2) -> {
                long score1 = (long) ((JSONObject) o1).get("Score");
                long score2 = (long) ((JSONObject) o2).get("Score");
                return Long.compare(score2, score1);
            });

            //shorten the list of objects to be the specified length.
            while (jsonA.size() > sizeLimit){
                jsonA.removeLast();
            }

            finalO.put("Highscores", jsonA);
            writeJSONToFile(finalO.toJSONString());
        } catch (IOException | ParseException e) {
            //If no entries exist, go into if Statement
            logger.logError(e.getMessage());
            b = true;
        }

        if (b){
            try {
                //write JSONObject into file as new JSON object
                jsonA.add(json);
                finalO.put("Highscores", jsonA);
                writeJSONToFile(finalO.toJSONString());
            } catch (IOException e) {
                logger.logError(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    private void writeJSONToFile(String JSONString) throws IOException {
        Files.write(filePath, JSONString.getBytes(), StandardOpenOption.WRITE, StandardOpenOption.DSYNC);
    }

    //Parse JSON file provided for a JSON object
    private JSONObject JSONFileParser() throws IOException, ParseException {
        FileReader reader = new FileReader(fileName);
        JSONParser parser = new JSONParser();

        JSONObject json = (JSONObject) parser.parse(reader);
        reader.close();
        return json;
    }

    /**
     * @return All the values as a Hashmap from the JSON file.
     */
    //IDE doesn't show warnings for "put" or "add" methods
    @SuppressWarnings("unchecked")
    public HashMap<String, Long> getValues(){
        //create return variable
        HashMap<String, Long> highscores = new HashMap<>();
        try {
            //try parsing file and putting all values into return variable;
            ArrayList<JSONObject> jsonA = new ArrayList<>();
            JSONArray jsonA1 = (JSONArray) JSONFileParser().get("Highscores");
            jsonA1.forEach(i -> jsonA.add((JSONObject) i));
            for (JSONObject json : jsonA){
                highscores.put((String) json.get("Name"), (Long)json.get("Score"));
            }
        } catch (IOException | ParseException e) {
            logger.logError(e.getMessage());
            throw new RuntimeException(e);
        }
        return highscores;
    }

    void clearInstance(){
        instance = null;
    }
}
