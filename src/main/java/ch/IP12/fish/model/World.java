package ch.IP12.fish.model;

import ch.IP12.fish.Spawner;
import ch.IP12.fish.components.Ads1115;
import ch.IP12.fish.components.JoystickAnalog;
import ch.IP12.fish.model.animations.Spritesheets;
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
    private int score;
    private Difficulty difficulty;
    private double clock;
    private Font font;

    private final Spawner spawner;

    public World(Context pi4j) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = screenSize.width;
        height = screenSize.height;

        Ads1115 ads1115 = new Ads1115(pi4j);

        JoystickAnalog joystick1 = new JoystickAnalog(ads1115, Ads1115.Channel.A0, Ads1115.Channel.A1);
        //JoystickAnalog joystick2 = new JoystickAnalog(ads1115, Ads1115.Channel.A2, Ads1115.Channel.A3);

        Player player1 = new Player(0, width / 2.0, 3, Spritesheets.Player.getSpriteAnimation(), joystick1, this);
        // Player player2 = new Player(0, HEIGHT / 1.5, 3, WIDTH, HEIGHT, Spritesheets.Player, joystick2, this);

        players = List.of(player1);

        obstacles = Collections.synchronizedList(new ArrayList<>());

        spawner = new Spawner(this);
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }


    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean isObstaclesEmpty() {
        return obstacles.isEmpty();
    }

    public Player getRandomPlayer() {
        if (players == null || players.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty");
        }
        Random random = new Random();
        return players.get(random.nextInt(players.size()));
    }


    public synchronized void nextPhase() {
        gamePhase = gamePhase.next();
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }

    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }


    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }


    public int getScore() {
        return score;
    }

    public void incrementScore(int amount) {
        score += amount;
    }

    public void decrementScore(int amount) {
        score -= amount;
    }


    public double getClock() {
        return clock;
    }

    public void resetClock() {
        clock = currentTimeSeconds();
    }

    public double currentTimeSeconds() {
        return System.currentTimeMillis() / 1000.0;
    }

    public double getDeltaClock() {
        return currentTimeSeconds() - clock;
    }


    public void setFont(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return font;
    }

    public Spawner getSpawner() {
        return spawner;
    }

    public void removeObstacle(Obstacle obstacle) {
        getObstacles().remove(obstacle);
    }

    public void removeObstacle(List<Obstacle> deletionList) {
        getObstacles().removeAll(deletionList);
    }

    public void setConfigData(Map<String, String> config) {
        this.config = config;
    }

    public String getConfigValue(String key) {
        return config.get(key);
    }


    public void setTextMapData(Map<String, String> textMap) {
        this.textMap = textMap;
    }

    public String getTextMapValue(String key) {
        return textMap.get(key);
    }
}