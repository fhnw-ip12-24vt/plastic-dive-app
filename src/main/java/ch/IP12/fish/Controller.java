package ch.IP12.fish;

import ch.IP12.fish.components.BarcodeScanner;
import ch.IP12.fish.model.*;
import ch.IP12.fish.utils.Difficulty;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Controller {
    private final World world;
    private final ScheduledExecutorService executor;

    private double deltaTimeClock = 0;
    private double lastHitTime = 0;
    private final AtomicInteger gameTicks = new AtomicInteger(0);

    private final BarcodeScanner barcodeScanner;

    Controller(World world, Scene scene) {
        this.world = world;
        this.executor = Executors.newSingleThreadScheduledExecutor();
        this.barcodeScanner = new BarcodeScanner(scene, world);
    }

    /**
     * Creates Key listeners for movement logic.
     *
     * @param scene The Scene object which will receive the listeners
     */
    void createGameKeyListeners(Scene scene) {
        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.I) {
                world.nextPhase();
                world.setDifficulty(Difficulty.Easy);
            }
        });
    }

    /**
     * Starts the game logic, such as Obstacle movement and tick incrementation.
     */
    public void startGameLogic() {
        // Run the game logic at a fixed rate
        executor.scheduleAtFixedRate(this::gameStep, 0, 16666666, TimeUnit.NANOSECONDS); // 16ms ≈ 60 updates per second
        barcodeScanner.startListening();
    }

    /**
     * Stops the game logic from running and kills all other threads related to it.
     */
    public void stopGameLogic() {
        executor.shutdown();
    }

    public void reset() {
        world.getPlayers().forEach(Player::resetPosition);
        barcodeScanner.startListening();
    }


    private void gameStep() {
        switch (world.getGamePhase()) {
            case Start -> start();
            case StartingAnimation -> startingAnimation();
            case Running -> running();
            case PreEndAnimation -> preEndAnimation();
            case End -> end();
            case HighScore -> highscore();
        }
    }

    private void start() {
        deltaTimeClock = System.currentTimeMillis();
        world.resetClock();
    }

    private void startingAnimation() {
        if (world.getDeltaClock() > 10) {
            lastHitTime = world.currentTimeSeconds();
            world.nextPhase();
            world.getPlayers().forEach(Player::startJoystick);
            world.resetClock();
        } else if (world.getDeltaClock() > 9.5) {
            world.getPlayers().forEach(Player::moveRight);
        }
    }

    private void running() {
        double deltaTime = (System.currentTimeMillis() - deltaTimeClock) / 1000; // Approx. 60 FPS
        deltaTimeClock = System.currentTimeMillis();

        final List<Obstacle> deletionList = Collections.synchronizedList(new ArrayList<>());
        // Update the model (logic)
        gameTicks.getAndIncrement();

        if (gameTicks.get() >= 75 && !(world.getDeltaClock() > 30)) {
            world.getSpawner().spawnRandom();
            gameTicks.set(0);
        }

        world.getPlayers().forEach(player -> player.update(deltaTime));

        for (int i = world.getObstacles().size() -1; i >= 0; i--) {
            Obstacle obstacle = world.getObstacles().get(i);
            //Obstacle updates
            obstacle.update(deltaTime);

            //adds obstacle to deletion list if it is entirely out of frame for the player
            if (obstacle.isOutsideBounds()) {
                deletionList.add(obstacle);
            }

            //collision stops prototype
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

        if (world.currentTimeSeconds() > lastHitTime + 5) {
            world.incrementScore((int) (1 * deltaTime * (1 + ((world.currentTimeSeconds()) - lastHitTime) / 15)));
        }

        if (world.getDeltaClock() > 30) {
            if (world.isObstaclesEmpty()) {
                world.nextPhase();
                world.getPlayers().forEach(Player::resetJoystick);
                world.resetClock();
            }
        }

    }

    void preEndAnimation() {
        world.nextPhase();
    }

    private void end() {
        world.getPlayers().forEach(Player::moveRight);
        if (world.getDeltaClock() > 10) {
            reset();
        }
    }

    private void highscore() {
        if (world.getDeltaClock() > 10) {
            world.nextPhase();
        }
    }
}
