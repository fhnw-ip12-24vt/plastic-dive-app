package ch.IP12.fish.model;

import ch.IP12.fish.Spawner;
import ch.IP12.fish.components.Ads1115;
import ch.IP12.fish.components.JoystickAnalog;
import ch.IP12.fish.model.animations.Spritesheets;
import ch.IP12.fish.model.obstacles.Obstacle;
import ch.IP12.fish.utils.Difficulty;
import ch.IP12.fish.utils.GamePhase;
import com.pi4j.context.Context;
import javafx.scene.text.Font;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Random;

public class World {
    private final double width;
    private final double height;

    private Map<String, String> config;
    private Map<String, String> textMap;

    private final List<Obstacle> obstacles;
    private final List<Player> players;

    private GamePhase gamePhase = GamePhase.Start;
    private float score = 100;
    private Difficulty difficulty;
    private double clock;
    private Font font;

    private final Spawner spawner;

    /**
     * Creates new DTO (Data Transfer Object) that can be used transfer data between all classes in this project.
     * @param pi4j Instantiates a Pi4J context to enable joystick input processing
     */
    public World(Context pi4j) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = screenSize.width;
        height = screenSize.height;

        Ads1115 ads1115 = new Ads1115(pi4j);

        JoystickAnalog joystick1 = new JoystickAnalog(ads1115, Ads1115.Channel.A0, Ads1115.Channel.A1);
        JoystickAnalog joystick2 = new JoystickAnalog(ads1115, Ads1115.Channel.A2, Ads1115.Channel.A3);

        Player player1 = new Player(0, height / 2.0 + 100, 3, Spritesheets.Player.getSpriteAnimation(), joystick1, this);
        Player player2 = new Player(0, height / 2.0 - 100, 3, Spritesheets.Player.getSpriteAnimation(), joystick2, this);

        players = List.of(player1,player2);

        obstacles = Collections.synchronizedList(new ArrayList<>());

        spawner = new Spawner(this);
    }

    /**
     * @return Width of screen
     */
    public double getWidth() {
        return width;
    }

    /**
     * @return Height of screen
     */
    public double getHeight() {
        return height;
    }


    /**
     * @return List of existing obstacles
     */
    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    /**
     * @return List of existing players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * @return Whether the obstacles array is empty
     */
    public boolean isObstaclesEmpty() {
        return obstacles.isEmpty();
    }

    /**
     * Removes all obstacle from the list
     */
    public void clearObstacles() {
        obstacles.clear();
    }

    /**
     * @return Random player Object from the list of players
     */
    public Player getRandomPlayer() {
        if (players == null || players.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty");
        }
        Random random = new Random();
        return players.get(random.nextInt(players.size()));
    }


    /**
     * Moves to next phase per the order determined in the Gamephases Enum
     */
    public synchronized void nextPhase() {
        gamePhase = gamePhase.next();
    }

    /**
     * @return Current GamePhase
     */
    public GamePhase getGamePhase() {
        return gamePhase;
    }

    /**
     * Sets GamePhase to provided one
     * @param gamePhase Gamephase to change to
     */
    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }


    /**
     * Sets difficulty to provided one
     * @param difficulty difficulty to change to
     */
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * @return Current Difficulty
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }


    /**
     * @return Current shared score of all players
     */
    public float getScore() {
        return score;
    }

    /**
     * @return Current shared score of all players without decimals. This is what we need for displaying score;
     */
    public int getScoreWithoutDecimals() {
        return (int)score;
    }
    /**
     * @param amount Increases shared score by this amount
     */
    public void incrementScore(float amount) {
        score += amount;
    }

    /**
     * @param amount Decreases shared score by this amount
     */
    public void decrementScore(int amount) {
        score -= amount;
    }


    /**
     * Sets clock to current time since the unix epoch in seconds
     */
    public void resetClock() {
        clock = currentTimeSeconds();
    }

    /**
     * @return Current time since the unix epoch in seconds
     */
    public double currentTimeSeconds() {
        return System.currentTimeMillis() / 1000.0;
    }

    /**
     * @return Difference between last saved time and current time (All in seconds)
     */
    public double getDeltaClock() {
        return currentTimeSeconds() - clock;
    }


    /**
     * @param font Sets font to provided one
     */
    public void setFont(Font font) {
        this.font = font;
    }

    /**
     * @return Currently selected font
     */
    public Font getFont() {
        return font;
    }


    /**
     * @return Obstacle spawner
     */
    public Spawner getSpawner() {
        return spawner;
    }

    /**
     * @param obstacle Removes provided Obstacle from list
     */
    public void removeObstacle(Obstacle obstacle) {
        getObstacles().remove(obstacle);
    }

    /**
     * @param deletionList Removes all Obstacle in provided list from internal list
     */
    public void removeObstacle(List<Obstacle> deletionList) {
        getObstacles().removeAll(deletionList);
    }


    /**
     * @param config Sets internal config to provided Map of settings
     */
    public void setConfigData(Map<String, String> config) {
        this.config = config;
    }

    /**
     * @param key Key of value to get
     * @return Value at provided key (Type: String)
     */
    public String getConfigValue(String key) {
        return config.get(key);
    }

    /**
     * @return Amount of settings stored
     */
    public int getConfigSize() {
        return config.size();
    }


    /**
     * @param textMap Sets Map of texts to provided map
     */
    public void setTextMapData(Map<String, String> textMap) {
        this.textMap = textMap;
    }

    /**
     * @param key Key for stored text
     * @return Text stored with provided key
     */
    public String getTextMapValue(String key) {
        return textMap.get(key);
    }

    /**
     * @return Amount of stored texts
     */
    public int getTextMapSize() {
        return textMap.size();
    }

    public void reset() {
        players.forEach(Player::resetPosition);
        resetClock();
        clearObstacles();
        score = 100;
        GamePhase gamePhase = GamePhase.Start;
    }
}