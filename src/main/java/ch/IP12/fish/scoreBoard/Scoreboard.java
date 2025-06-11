package ch.IP12.fish.scoreBoard;

import ch.IP12.fish.fileInterpreters.Logger;
import ch.IP12.fish.model.World;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Scoreboard {
    //scoreboard list of scores and when
    private final Map<String, Double> scoreboard = new TreeMap<>();
    DataDealer dataDealer;

    private static Scoreboard instance = null;

    private final String filename;
    private final World world;

    private final ScoreboardComparer comparator = new ScoreboardComparer();

    private Scoreboard(World world, String fileName) {
        this.world = world;

        this.filename = fileName;

        try{
            dataDealer = DataDealer.getInstance(fileName);
            insertValues(fileName);
        } catch (IOException e){
            Logger.getInstance().logError(e.getMessage(), world.getConfigValue("log").equals("detailed") ? e.getStackTrace(): null);
        }
    }

    /**
     * **Note: Name is set to Highscores with this method**
     * @param world World DTO to interact with for drawing the table.
     * @return Instance of active Scoreboard.
     */
    public static Scoreboard getInstance(World world){
        return getInstance(world, "Highscores");
    }

    /**
     * @param world World DTO to interact with for drawing the table.
     * @param fileName Name of file values are stored in.
     * @return Instance of active Scoreboard.
     */
    public static Scoreboard getInstance(World world, String fileName) {
        return instance == null ? instance = new Scoreboard(world, fileName) : instance;
    }

    /**
     * @return Instance of active Scoreboard.
     * @throws NullPointerException if not instanced yet.
     */
    public static Scoreboard getInstance() throws NullPointerException{
        if (instance == null){
            throw new NullPointerException("Scoreboard not initialized with valid values yet");
        }
        return instance;
    }

    /**
     * Returns entire scoreboard.
     * @return All the entries in the Scoreboard.
     */
    ScoreboardEnitity[] getList(){
        try {
            this.insertValues(filename);
        } catch (IOException e) {
            Logger.getInstance().logError(e.getMessage(), e.getStackTrace());
        }

        ArrayList<ScoreboardEnitity> list = new ArrayList<>();
        for (String i : scoreboard.keySet()){
            StringBuilder temp = new StringBuilder(i);
            while (temp.length() < 30){
                temp.append(" ");
            }
            if (temp.length() > 30){
                temp = new StringBuilder(temp.substring(0, 30));
            }
            list.add(new ScoreboardEnitity(scoreboard.get(i), temp.toString()));
        }

        list.sort(comparator);
        return list.toArray(new ScoreboardEnitity[0]);
    }

    /**
     * Update values in the scoreboard
     * @throws IOException File interaction warrants possible errors.
     */
    private void insertValues(String fileName) throws IOException {
        scoreboard.clear();
        scoreboard.putAll(dataDealer.getValues());
    }

    /**
     * Draws scoreboard onto provided graphicsContext.
     * @param gc Graphics context that is drawn to.
     */
    public void draw(GraphicsContext gc){
        try {
            ScoreboardEnitity[] highScores = getList();

            String temp = "1.  " + highScores[0].name() + ":  " + highScores[0].score();
            double startX = world.getWidth() / 2f - (temp.length()*15)/2f;
            double startY = world.getHeight() / 2f - ((highScores.length)*30)/2f;
            float lineSpacing = 30f;

            gc.fillText(world.getTextMapValue("highscoreText") + world.getScoreWithoutDecimals(),
                    startX,
                    startY - 60);

            gc.setFill(Color.BLACK);

            for (int i = 0; i < highScores.length; i++) {
                ScoreboardEnitity entry = highScores[i];
                String text = (i + 1) + ".  " + entry.name() + ":  " + (int) entry.score();
                gc.fillText(text, startX, startY + i * lineSpacing);
            }
        } catch (Exception e) {
            gc.fillText(world.getTextMapValue("highscoreLoadingErrorText"), world.getWidth() / 2f, world.getHeight() / 2f);
            Logger.getInstance().logError(e.getMessage(), world.getConfigValue("log").equals("detailed") ? e.getStackTrace(): null);
        }
    }
}
