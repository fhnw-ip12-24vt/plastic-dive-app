package ch.IP12.fish;

import ch.IP12.fish.components.JoystickAnalog;
import ch.IP12.fish.components.BarcodeScanner;
import ch.IP12.fish.model.*;
import ch.IP12.fish.model.animations.Spritesheets;
import ch.IP12.fish.utils.Difficulty;
import ch.IP12.fish.utils.GamePhase;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Controller {
    private final List<Player> players;
    private final List<Obstacle> obstacles;
    private final ScheduledExecutorService executor;

    private Spawner spawner;
    private double deltaTimeClock = 0;
    private double lastHitTime = 0;
    private final AtomicInteger gameTicks = new AtomicInteger(0);

    public static volatile GamePhase GAMEPHASE = GamePhase.Start;
    public static double SCORE = 500;
    public static Difficulty DIFFICULTY;
    public static double CLOCK;

    Controller(List<Player> players, List<Obstacle> obstacles) {
        this.players = players;
        this.obstacles = obstacles;
        this.executor = Executors.newSingleThreadScheduledExecutor();
        this.spawner = new Spawner(obstacles);
    }

    /**
     * Creates Key listeners for movement logic.
     *
     * @param scene The Scene object which will receive the listeners
     */
    void createGameKeyListeners(Scene scene) {
        BarcodeScanner barcodeScanner = new BarcodeScanner(scene);
        barcodeScanner.startListening();

        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.I) {
                nextPhase();
                Controller.DIFFICULTY = Difficulty.Easy;
            }
        });
    }

    /**
     * Starts the game logic, such as Obstacle movement and tick incrementation.
     */
    public void startGameLogic() {
        // Run the game logic at a fixed rate
        executor.scheduleAtFixedRate(this::gameStep, 0, 16666666, TimeUnit.NANOSECONDS); // 16ms ≈ 60 updates per second
    }

    /**
     * Stops the game logic from running and kills all other threads related to it.
     */
    public void stopGameLogic() {
        executor.shutdown();
    }


    public static GamePhase getGAMEPHASE() {
        return GAMEPHASE;
    }

    private void gameStep() {
        switch (Controller.getGAMEPHASE()) {
            case Start -> start();
            case StartingAnimation -> startingAnimation();
            case Running -> running();
            case PreEndAnimation -> running();
            case End -> end();
            case HighScore -> highscore();
        }
    }

    private void start() {
        deltaTimeClock = System.currentTimeMillis();
        CLOCK = CURRENTTIMESECONDS();
    }

    private void startingAnimation() {
        if (GETDELTACLOCK() > 10) {
            lastHitTime = CURRENTTIMESECONDS();
            nextPhase();
            players.forEach(Player::startJoystick);

        } else if (GETDELTACLOCK() > 9.5) {
            players.forEach(Player::moveRight);
        }
    }

    private void running() {
        double deltaTime = (System.currentTimeMillis() - deltaTimeClock) / 1000; // Approx. 60 FPS
        deltaTimeClock = System.currentTimeMillis();

        final List<Obstacle> deletionList = Collections.synchronizedList(new ArrayList<>());
        // Update the model (logic)
        gameTicks.getAndIncrement();

        if (gameTicks.get() >= 75 && !(CURRENTTIMESECONDS() - CLOCK > 30)) {
            Random rand = new Random();
            spawner.spawnRandom(players.get(rand.nextInt(players.size())));
            gameTicks.set(0);
        }

        players.forEach(player -> player.update(deltaTime));
        obstacles.parallelStream().forEach(obstacle -> {
            //Obstacle updates
            obstacle.update(deltaTime);

            //adds obstacle to deletion list if it is entirely out of frame for the player
            if (obstacle.isOutsideBounds()) deletionList.add(obstacle);

            //collision stops prototype
            players.forEach(player -> {
                if (player.collidesWith(obstacle)) {
                    lastHitTime = CURRENTTIMESECONDS();
                    SCORE -= 50;
                    deletionList.add(obstacle);
                }
            });

        });

        //removes obstacles from main obstacle array
        //and clears the deletion list.
        obstacles.removeAll(deletionList);
        deletionList.clear();

        if (CURRENTTIMESECONDS() > lastHitTime + 5) {
            SCORE += 1 * deltaTime * (1 + ((CURRENTTIMESECONDS() - lastHitTime) / 15));
        }

        if (CURRENTTIMESECONDS() - CLOCK > 30) {
            if (obstacles.isEmpty()) {
                nextPhase();
                players.forEach(Player::resetJoystick);
                CLOCK = CURRENTTIMESECONDS();
            }
        }

    }

    private void end() {
        players.forEach(Player::moveRight);
        if (GETDELTACLOCK() > 10) {
            nextPhase();
            CLOCK = CURRENTTIMESECONDS();
        }
    }

    private void highscore() {
        if (GETDELTACLOCK() > 10) {
            nextPhase();
        }
    }

    private synchronized void nextPhase() {
        GAMEPHASE = GAMEPHASE.next();
    }

    public static double CURRENTTIMESECONDS() {
        return System.currentTimeMillis() / 1000.0;
    }

    public static double GETDELTACLOCK() {
        return CURRENTTIMESECONDS() - CLOCK;
    }
}
