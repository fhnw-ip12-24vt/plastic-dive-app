package ch.IP12.fish;

import ch.IP12.fish.components.JoystickAnalog;
import ch.IP12.fish.difficultySelector.BarcodeScanner;
import ch.IP12.fish.model.*;
import ch.IP12.fish.model.animations.Spritesheets;
import ch.IP12.fish.utils.GamePhase;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Controller {
    private final List<KeyCode> pressedKeys = Collections.synchronizedList(new ArrayList<>());
    private final Player player;
    private final List<Obstacle> obstacles;
    private final ScheduledExecutorService executor;
    public static volatile GamePhase gamePhase = GamePhase.Start;
    protected final AtomicInteger gameTicks = new AtomicInteger();
    protected final JoystickAnalog joystick;
    private long clock;
    private long deltatimeClock;
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
                gamePhase = gamePhase.next();
            }
        });



    }

    /**
     * Clears Key listeners from provided Scene Object
     *
     * @param scene Scene Object
     */
    void clearKeyListeners(Scene scene) {
        scene.setOnKeyPressed(e -> {
        });
        scene.setOnKeyReleased(e -> {
        });

        joystick.reset();
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


    public static GamePhase getGamePhase() {
        return gamePhase;
    }

    private void gameStep() {
        switch (Controller.getGamePhase()) {
            case Start -> start();
            case StartingAnimation -> startingAnimation();
            case Running -> running();
            case BeforeEndAnimation -> running();
            case End -> end();
            case HighScore -> highscore();
        }
    }

    private void start() {
        clock = System.currentTimeMillis();
    }

    private void startingAnimation() {
        if (System.currentTimeMillis() - clock > 10000) {
            gamePhase = gamePhase.next();
            if (this.joystick != null) {
                joystick.onMove((double xPos, double yPos) -> System.out.println(xPos + " " + yPos), () -> System.out.println("center"));
            } else {
                System.out.println("No joystick found");
            }
        }
    }

    private void running() {
        double deltaTime = System.currentTimeMillis() - deltatimeClock; // Approx. 60 FPS
        deltatimeClock = System.currentTimeMillis();

        final List<Obstacle> deletionList = Collections.synchronizedList(new ArrayList<>());
        // Update the model (logic)
        gameTicks.getAndIncrement();

        if (gameTicks.get() >= 50 && !(System.currentTimeMillis() - clock > 2400000)) {
            obstacles.add(new Obstacle(App.WIDTH, (int) ((Math.random() * (App.HEIGHT))), 2, App.WIDTH, App.HEIGHT, Spritesheets.getRandomSpritesheet()));
            gameTicks.set(0);
        }

        player.update(deltaTime, JoystickAnalog.getStrength());
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
        if (System.currentTimeMillis() - clock > 2400000) {

        }
        if (System.currentTimeMillis() - clock > 2400000 && obstacles.isEmpty()) {
            gamePhase = gamePhase.next();
            joystick.reset();
            clock = System.currentTimeMillis();
        }
    }

    private void end() {
        if (System.currentTimeMillis() - clock > 10000) {
            gamePhase = gamePhase.next();
            clock = System.currentTimeMillis();
        }
    }

    private void highscore() {
        if (System.currentTimeMillis() - clock > 10000) {
            gamePhase = gamePhase.next();
        }
    }
}
