package ch.IP12.fish;

import ch.IP12.fish.components.JoystickAnalog;
import ch.IP12.fish.difficultySelector.BarcodeScanner;
import ch.IP12.fish.model.*;
import ch.IP12.fish.model.animations.Spritesheets;
import ch.IP12.fish.utils.GamePhase;
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
    private final Player player;
    private final List<Obstacle> obstacles;
    private final ScheduledExecutorService executor;
    public static volatile GamePhase GAMEPHASE = GamePhase.Start;
    protected final AtomicInteger gameTicks = new AtomicInteger();
    protected final JoystickAnalog joystick;
    public static double CLOCK;
    private double deltatimeClock;
    public static String DIFFICULTY = "";

    Controller(Player player, List<Obstacle> obstacles, JoystickAnalog joystick) {
        this.joystick = joystick;
        this.player = player;
        this.obstacles = obstacles;
        this.executor = Executors.newSingleThreadScheduledExecutor();
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
            case BeforeEndAnimation -> running();
            case End -> end();
            case HighScore -> highscore();
        }
    }

    private void start() {
        CLOCK = currentTimeSeconds();
    }

    private void startingAnimation() {
        if (getDELTACLOCK() > 10) {
            nextPhase();
            if (this.joystick != null) {
                joystick.onMove((double xPos, double yPos) -> {}, () -> {});
            } else {
                System.out.println("No joystick found");
            }
        } else if (getDELTACLOCK() > 9.5) {
            player.moveRight();
        }
    }

    private void running() {
        double deltaTime = System.currentTimeMillis() - deltatimeClock; // Approx. 60 FPS
        deltatimeClock = System.currentTimeMillis();

        final List<Obstacle> deletionList = Collections.synchronizedList(new ArrayList<>());
        // Update the model (logic)
        gameTicks.getAndIncrement();

        if (gameTicks.get() >= 50 && !(currentTimeSeconds() - CLOCK > 240)) {
            obstacles.add(new Obstacle(App.WIDTH, (int) ((Math.random() * (App.HEIGHT))), 2, App.WIDTH, App.HEIGHT, Spritesheets.getRandomSpritesheet()));
            gameTicks.set(0);
        }

        player.update(deltaTime, joystick.getStrength(), joystick.getDirection());
        obstacles.parallelStream().forEach(obstacle -> {
            //Obstacle updates
            obstacle.update(deltaTime, 0.9);

            //adds obstacle to deletion list if it is entirely out of frame for the player
            if (obstacle.getX() + obstacle.getLength() < 0) deletionList.add(obstacle);

            //collision stops prototype
            if (player.collidesWith(obstacle)) {

            }
        });

        //removes obstacles from main obstacle array
        //and clears the deletion list.
        obstacles.removeAll(deletionList);
        deletionList.clear();
        if (currentTimeSeconds() - CLOCK > 240) {

        }
        if (currentTimeSeconds() - CLOCK > 240 && obstacles.isEmpty()) {
            nextPhase();
            joystick.reset();
            CLOCK = currentTimeSeconds();
        }
    }

    private void end() {
        player.moveRight();
        if (getDELTACLOCK() > 10) {
            nextPhase();
            CLOCK = currentTimeSeconds();
        }
    }

    private void highscore() {
        if (getDELTACLOCK() > 10) {
            nextPhase();
        }
    }

    private synchronized void nextPhase(){
        GAMEPHASE = GAMEPHASE.next();
    }

    private static double currentTimeSeconds() {
        return System.currentTimeMillis() / 1000.0;
    }

    public static double getDELTACLOCK() {
        return currentTimeSeconds()-CLOCK;
    }
}
