package ch.IP12.fish;

import ch.IP12.fish.components.BarcodeScanner;
import ch.IP12.fish.fileInterpreters.Logger;
import ch.IP12.fish.model.*;
import ch.IP12.fish.scoreBoard.DataDealer;
import ch.IP12.fish.model.obstacles.Obstacle;
import ch.IP12.fish.utils.Difficulty;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {
    private final World world;
    private final ScheduledExecutorService executor;

    private final Logger logger = Logger.getInstance();

    private double deltaTimeClock = 0;
    private double deltaTime = 0;
    private double lastHitTime = 0;
    private double gameTicks = 0;

    private final BarcodeScanner barcodeScanner;

    /**
     * General logic controller for the Game
     *
     * @param world DTO (Data Transfer Object)
     * @param scene Scene to listen for inputs on
     */
    Controller(World world, Scene scene) {
        this.world = world;
        this.executor = Executors.newSingleThreadScheduledExecutor();
        this.barcodeScanner = new BarcodeScanner(scene, world);
    }

    /**
     * Creates Key listeners for debugging to skip phases.
     * **MAY CREATE UNEXPECTED BEHAVIOUR**
     * @param scene The Scene object which will receive the listeners
     */
    void createGameKeyListeners(Scene scene) {
        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.I) {
                phaseChange(0);
            }
        });
    }

    /**
     * Starts the game logic, such as Obstacle movement and tick incrementation.
     */
    public void startGameLogic() {
        // Run the game logic at a fixed rate
        executor.scheduleAtFixedRate(this::gameStep, 0, 16666666, TimeUnit.NANOSECONDS); // 16ms ≈ 60 updates per second
        world.setDifficulty(Difficulty.Easy);
    }

    /**
     * Stops the game logic from running and kills all other threads related to it.
     */
    public void stopGameLogic() {
        executor.shutdown();
    }

    /**
     * Reset game state to same state as just before a round
     */
    public void reset() {
        world.reset();
        world.clearObstacles();
        barcodeScanner.startListening();
    }


    private void gameStep() {
        deltaTime = (System.currentTimeMillis() - deltaTimeClock) / 1000; // Approx. 60 FPS
        deltaTimeClock = System.currentTimeMillis();
        gameTicks += deltaTime;
        switch (world.getGamePhase()) {
            case Start -> start();
            case StartingAnimation -> startingAnimation();
            case Running -> running();
            case PreEndAnimation -> preEndAnimation();
            case End -> end();
            case HighScore -> highScore();
        }
    }

    private void start() {
        deltaTimeClock = System.currentTimeMillis();
        world.resetClock();
        reset();
    }

    private void startingAnimation() {
        if (world.getDeltaClock() > 9.5) {
            world.getPlayers().forEach(player -> player.moveRight(deltaTime));
        }

        phaseChange(10, () -> {
            lastHitTime = world.currentTimeSeconds();
            world.getPlayers().forEach(Player::startJoystick);
        });
    }

    private void running() {

        final List<Obstacle> deletionList = Collections.synchronizedList(new ArrayList<>());

        if (gameTicks >= 1 && !(world.getDeltaClock() > 30)) {
            world.getSpawner().spawnRandom();
            gameTicks = 0;
        }

        world.getPlayers().forEach(player -> player.update(deltaTime));

        for (int i = world.getObstacles().size() - 1; i >= 0; i--) {
            Obstacle obstacle = world.getObstacles().get(i);
            //Obstacle updates
            obstacle.update(deltaTime);

            //adds obstacle to deletion list if it is entirely out of frame for the player
            if (obstacle.isOutsideBounds()) {
                deletionList.add(obstacle);
            }

            //collision deletes obstacle from existence
            world.getPlayers().forEach(player -> {
                if (player.collidesWith(obstacle)) {
                    lastHitTime = world.currentTimeSeconds();
                    world.decrementScore(50);
                    deletionList.add(obstacle);
                }
            });

        }

        //removes obstacles from main obstacle array
        //and clears the deletion list.
        world.removeObstacle(deletionList);
        deletionList.clear();

        //Score increments after not being hit for 5 seconds. This increment increases with more time passed while not hit.
        if (world.currentTimeSeconds() > lastHitTime + 5)
            world.incrementScore((long) (1 * deltaTime * (1 + ((world.currentTimeSeconds()) - lastHitTime))));


        if (world.isObstaclesEmpty())
            phaseChange(30, () -> world.getPlayers().forEach(Player::resetJoystick));

    }

    void preEndAnimation() {
        phaseChange(0);
    }

    private void end() {
        // Score nur beim ersten Aufruf speichern
        if (!world.getScoreSaved()) {
            try {
                DataDealer dealer = DataDealer.getInstance("Highscore.json");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String timestamp = LocalDateTime.now().format(formatter);

                //json file updated with new possible entry and sorted
                dealer.dataStore("Datum: " + timestamp, world.getScoreWithoutDecimals());
                //Scoreboard.getInstance().insertValues();
                world.setScoreSaved(true);
            } catch (IOException e) {
                logger.logError(e.getMessage(), world.getConfigValue("log").equals("detailed") ? e.getStackTrace(): null);
                System.out.println(e.getMessage());
            }
        }

        world.getPlayers().forEach(player -> player.moveRight(deltaTime));
        phaseChange(10);
    }

    private void highScore() {
        if(world.getScoreSaved()){
            world.setScoreSaved(false);
        }

        phaseChange(10, () -> logger.log("Round finished, final score: " + world.getScoreWithoutDecimals()));
    }

    //Shift phase to th next one and execute an action after n amount of seconds have passed
    private void phaseChange(int timeInPhase, Runnable action) {
        if (world.getDeltaClock() >= timeInPhase) {
            action.run();

            world.resetClock();
            world.nextPhase();
        }
    }

    private void phaseChange(int timeInPhase) {
        phaseChange(timeInPhase, () -> {});
    }
}
