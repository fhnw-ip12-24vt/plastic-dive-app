package ch.IP12.fish.scoreBoard;

import ch.IP12.fish.fileInterpreters.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataDealer {
    //variable for easy access to file
    private final Path filePath;

    private final int sizeLimit;
    private final Logger logger = Logger.getInstance();

    private static DataDealer instance = null;

    private DataDealer(String fileName, int boardSizeLimit) throws IOException {
        //check for file existence and creation if it doesn't
        sizeLimit = boardSizeLimit;
        fileName += ".json";

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
    public void dataStore(String name, double score){
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
            jsonA.addAll(JSONFileParser());
            jsonA.add(json);
            //sorts from highest to lowest
            jsonA.sort((o1, o2) -> {
                double score1 = (double) ((JSONObject) o1).get("Score");
                double score2 = (double) ((JSONObject) o2).get("Score");
                return Double.compare(score2, score1);
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

    @SuppressWarnings("StatementWithEmptyBody")
    private void writeJSONToFile(String JSONString) throws IOException {
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
            while (!Files.exists(filePath)) {}
        }
        Files.write(filePath, JSONString.getBytes(), StandardOpenOption.WRITE, StandardOpenOption.DSYNC);
    }

    //Parse JSON file provided for a JSON object
    private JSONArray JSONFileParser() throws IOException, ParseException {
        Files.readAllLines(filePath);
        List<String> stringList = Files.readAllLines(filePath);

        String jsonFile = "";
        for (String string : stringList) {
            jsonFile += string.replace("\r", "").replace("\n", "").trim();
        }



        if (stringList.isEmpty()) {
            return new JSONArray();
        }

        JSONParser parser = new JSONParser();

        JSONObject json = (JSONObject) parser.parse(jsonFile);

        return (JSONArray) json.get("Highscores");
    }

    /**
     * @return All the values as a Hashmap from the JSON file.
     */
    //IDE doesn't show warnings for "put" or "add" methods
    @SuppressWarnings("unchecked")
    public HashMap<String, Double> getValues(){
        //create return variable
        HashMap<String, Double> highscores = new HashMap<>();

        try {
            //try parsing file and putting all values into return variable;
            ArrayList<JSONObject> jsonA = new ArrayList<>();
            JSONArray jsonA1 = JSONFileParser();
            jsonA1.forEach(i -> jsonA.add((JSONObject) i));

            for (JSONObject json : jsonA){
                highscores.put((String) json.get("Name"), (Double) json.get("Score"));
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
