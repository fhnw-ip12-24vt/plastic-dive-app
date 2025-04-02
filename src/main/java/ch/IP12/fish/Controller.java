package ch.IP12.fish;

import ch.IP12.fish.components.JoystickAnalog;
import ch.IP12.fish.model.*;
import ch.IP12.fish.model.animations.Spritesheets;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class Controller {
    private final List<KeyCode> pressedKeys = Collections.synchronizedList(new ArrayList<>());
    private final Player player;
    private final List<Obstacle> obstacles;
    private final ScheduledExecutorService executor;
    private static volatile boolean running = true;
    protected final AtomicInteger gameTicks = new AtomicInteger();
    protected final JoystickAnalog joystick;
    private long clock;
    private int[] fpsArray = new int[100];
    private int tick = 0;

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
        scene.setOnKeyReleased(e -> {
            if (!running) {
                //If the game is over and the player releases a button the key listeners are cleared
                clearKeyListeners(scene);
                return;
            }

            if (e.getCode() == KeyCode.A && pressedKeys.contains(e.getCode()) || e.getCode() == KeyCode.LEFT && pressedKeys.contains(e.getCode())) {
                player.setTempDir(false, 1);
                pressedKeys.remove(e.getCode());
            }
            if (e.getCode() == KeyCode.D && pressedKeys.contains(e.getCode()) || e.getCode() == KeyCode.RIGHT && pressedKeys.contains(e.getCode())) {
                player.setTempDir(false, 0);
                pressedKeys.remove(e.getCode());
            }
            if (e.getCode() == KeyCode.S && pressedKeys.contains(e.getCode()) || e.getCode() == KeyCode.DOWN && pressedKeys.contains(e.getCode())) {
                player.setTempDir(false, 3);
                pressedKeys.remove(e.getCode());
            }
            if (e.getCode() == KeyCode.W && pressedKeys.contains(e.getCode()) || e.getCode() == KeyCode.UP && pressedKeys.contains(e.getCode())) {
                player.setTempDir(false, 2);
                pressedKeys.remove(e.getCode());
            }
        });
        scene.setOnKeyPressed(e -> {
            if (!running) {
                //If the game is over and the player presses a button the key listeners are cleared
                clearKeyListeners(scene);
                return;
            }

            //moves player using temporary variables whilst there is no joystick present.
            if (e.getCode() == KeyCode.A && !pressedKeys.contains(e.getCode()) || e.getCode() == KeyCode.LEFT && !pressedKeys.contains(e.getCode())) {
                player.setTempDir(true, 1);
                pressedKeys.add(e.getCode());
            }
            if (e.getCode() == KeyCode.D && !pressedKeys.contains(e.getCode()) || e.getCode() == KeyCode.RIGHT && !pressedKeys.contains(e.getCode())) {
                player.setTempDir(true, 0);
                pressedKeys.add(e.getCode());
            }
            if (e.getCode() == KeyCode.S && !pressedKeys.contains(e.getCode()) || e.getCode() == KeyCode.DOWN && !pressedKeys.contains(e.getCode())) {
                player.setTempDir(true, 3);
                pressedKeys.add(e.getCode());
            }
            if (e.getCode() == KeyCode.W && !pressedKeys.contains(e.getCode()) || e.getCode() == KeyCode.UP && !pressedKeys.contains(e.getCode())) {
                player.setTempDir(true, 2);
                pressedKeys.add(e.getCode());
            }
        });

        if (this.joystick != null) {
            joystick.onMove((double xPos, double yPos) -> System.out.println("moved"), () -> System.out.println("centered"));
        } else {
            System.out.println("No joystick found");
        }

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
        executor.scheduleAtFixedRate(this::gameStep, 0, 16, TimeUnit.MILLISECONDS); // 16ms ≈ 60 updates per second
    }

    /**
     * Stops the game logic from running and kills all other threads related to it.
     */
    public void stopGameLogic() {
        running = false;
        executor.shutdown();
    }

    public void resetGame() {
        obstacles.clear();
        player.resetPosition();
        gameTicks.set(0);
        running = true;
        startGameLogic();
    }

    public static boolean isRunning() {
        return running;
    }

    private void gameStep() {
        if ((System.currentTimeMillis() - clock) != 0) {
            fpsArray[tick] = (int) (1000 / (System.currentTimeMillis() - clock));
            tick++;
        }
        if (tick == fpsArray.length) tick = 0;
        int fps = 0;
        for (int i : fpsArray) {
            fps += i;
        }
        fps /= fpsArray.length;
        System.out.println("Ticks per second: " + fps);
        clock = System.currentTimeMillis();

        if (running) {
            final List<Obstacle> deletionList = Collections.synchronizedList(new ArrayList<>());
            // Update the model (logic)
            gameTicks.getAndIncrement();

            if (gameTicks.get() >= 50) {
                obstacles.add(new Obstacle(App.WIDTH, (int) ((Math.random() * (App.HEIGHT))), 2, App.WIDTH, App.HEIGHT, Spritesheets.getRandomSpritesheet()));
                gameTicks.set(0);
            }

            double deltaTime = 0.016; // Approx. 60 FPS
            player.update(deltaTime, JoystickAnalog.getStrength(), joystick.inHomePosition());
            obstacles.parallelStream().forEach(obstacle -> {
                //Obstacle updates
                obstacle.update(deltaTime, 0.9, false);

                //adds obstacle to deletion list if it is entirely out of frame for the player
                if (obstacle.getX() + obstacle.getLength() < 0) deletionList.add(obstacle);

                //collision stops prototype
                //if (player.collidesWith(obstacle)) {
                    //stopGameLogic();
                //}
            });

            //removes obstacles from main obstacle array
            //and clears the deletion list.
            obstacles.removeAll(deletionList);
            deletionList.clear();
        }
    }
}
