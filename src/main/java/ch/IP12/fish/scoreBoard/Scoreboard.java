package ch.IP12.fish.scoreBoard;

import ch.IP12.fish.fileInterpreters.Logger;
import ch.IP12.fish.model.World;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.WeakHashMap;

public class Scoreboard {
    //scoreboard list of scores and when
    private final Map<String, Long> scoreboard = new TreeMap<>();
    private static Scoreboard instance = null;
    private final Logger logger = Logger.getInstance();

    private final World world;


    private Scoreboard(World world) {
        this.world = world;

        try{
            insertValues();
        } catch (IOException e){
            logger.logError(e.getMessage(), world.getConfigValue("log").equals("detailed") ? e.getStackTrace(): null);
        }
    }

    public static Scoreboard getInstance(World world){
        return instance == null ? instance = new Scoreboard(world) : instance;
    }

    public static Scoreboard getInstance(){
        if (instance == null){
            throw new NullPointerException("Scoreboard not initialized");
        }
        return instance;
    }

    /**
     * @return Highest score value on the leaderboard
     */
    public ScoreboardEnitity getHighscore(){
        Long t = 0L;
        String prevKey = "";
        for (String i : scoreboard.keySet()){
            if (prevKey.isEmpty()){
                t = scoreboard.get(i);
                prevKey = i;
            } else if (scoreboard.get(i) > scoreboard.get(prevKey)){
                t = scoreboard.get(i);
                prevKey = i;
            }
        }
        return new ScoreboardEnitity(t, prevKey);
    }

    /**
     * Returns entire scoreboard.
     * @return All the entries in the Scoreboard.
     */
    public ScoreboardEnitity[] getList(){
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
        ScoreboardComparer comparator = new ScoreboardComparer();
        //noinspection unchecked
        list.sort(comparator);
        return list.toArray(new ScoreboardEnitity[0]);
    }

    /**
     * Update values in the scoreboard
     * @throws IOException File interaction warrants possible errors.
     */
    public void insertValues() throws IOException {
        String fileName = "Highscore.json";
        DataDealer d = DataDealer.getInstance(fileName);
        scoreboard.clear();
        scoreboard.putAll(d.getValues());
    }

    /**
     * Draws scoreboard onto provided graphicsContext.
     * @param gc Graphics context that is drawn to.
     */
    public void draw(GraphicsContext gc){
        try {
            this.insertValues();
            ScoreboardEnitity[] highScores = getList();

            String temp = "1.  " + highScores[0].getName() + ":  " + highScores[0].getScore();
            double startX = world.getWidth() / 2f - (temp.length()*15)/2f;
            double startY = world.getHeight() / 2f - ((highScores.length)*30)/2f;
            float lineSpacing = 30f;

            gc.fillText(world.getTextMapValue("highscoreText") + world.getScoreWithoutDecimals(),
                    startX,
                    startY - 60);

            gc.setFill(Color.BLACK);

            for (int i = 0; i < highScores.length; i++) {
                ScoreboardEnitity entry = highScores[i];
                String text = (i + 1) + ".  " + entry.getName() + ":  " + entry.getScore();
                gc.fillText(text, startX, startY + i * lineSpacing);
            }
        } catch (Exception e) {
            gc.fillText(world.getTextMapValue("highscoreLoadingErrorText"), world.getWidth() / 2f, world.getHeight() / 2f);
            Logger.getInstance().logError(e.getMessage(), world.getConfigValue("log").equals("detailed") ? e.getStackTrace(): null);
        }
    }
}
