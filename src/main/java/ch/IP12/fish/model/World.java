package ch.IP12.fish.model;

import ch.IP12.fish.components.Ads1115;
import ch.IP12.fish.components.JoystickAnalog;
import ch.IP12.fish.model.animations.Spritesheets;
import ch.IP12.fish.utils.Difficulty;
import ch.IP12.fish.utils.GamePhase;
import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import javafx.scene.text.Font;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class World {
    double width = 1920;
    double height = 1080;

    List<Obstacle> obstacles;
    List<Player> players;

    GamePhase gamePhase;
    int score;
    Difficulty difficulty;
    double clock;

    public World(Context pi4j) {

        Ads1115 ads1115 = new Ads1115(pi4j);
        JoystickAnalog joystick1 = new JoystickAnalog(ads1115, Ads1115.Channel.A0, Ads1115.Channel.A1);
        //JoystickAnalog joystick2 = new JoystickAnalog(ads1115, Ads1115.Channel.A2, Ads1115.Channel.A3);

        Player player1 = new Player(0, width / 2.0, 3, width, height, Spritesheets.Player, joystick1);
        // Player player2 = new Player(0, HEIGHT / 1.5, 3, WIDTH, HEIGHT, Spritesheets.Player, joystick2);
        players = List.of(player1);

        obstacles = Collections.synchronizedList(new ArrayList<>());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = screenSize.width;
        height = screenSize.height;
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

    public GamePhase getGamePhase() {
        return gamePhase;
    }

    public int getScore() {
        return score;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public boolean isObstaclesEmpty() {
        return obstacles.isEmpty();
    }

    public synchronized void nextPhase() {
        gamePhase = gamePhase.next();
    }

    public Player getRandomPlayer() {
        if (players == null || players.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty");
        }
        Random random = new Random();
        return players.get(random.nextInt(players.size()));
    }

    public Difficulty setDifficulty(Difficulty difficulty) {
        return this.difficulty = difficulty;
    }

    public void resetClock() {
    }
}
