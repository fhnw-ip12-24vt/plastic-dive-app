package ch.IP12.fish;

import ch.IP12.fish.model.Obstacle;
import ch.IP12.fish.model.Player;
import ch.IP12.fish.model.animations.Spritesheets;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.List;


public class View {
    private final GraphicsContext graphicsContext;
    private final List<Player> players;
    private final List<Obstacle> obstacles;
    private double frontLayerShift = 0.0;
    private double middleLayerShift = 0.0;
    private double backLayerShift = 0.0;
    private long clock;
    private final Image frontLayer = new Image("backgroundLayers/frontLayer.png");
    private final Image middleLayer = new Image("backgroundLayers/middleLayer.png");
    private final Image backLayer = new Image("backgroundLayers/middleLayer.png");
    private double backgroundScalar = 3.5;
    private double layerShiftScalar = 0.2;

    View(GraphicsContext graphicsContext, List<Player> players, List<Obstacle> obstacles) {
        this.graphicsContext = graphicsContext;
        this.players = players;
        this.obstacles = obstacles;
    }

    /**
     * Start rendering the relevant images for the objects on screen.
     */
    public void startRendering() {
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                render();
            }
        };
        animationTimer.start();
    }

    /**
     * Draws the objects on the screen
     */
    private void render() {
        graphicsContext.setFill(Color.web("#3e79dd"));
        graphicsContext.fillRect(0, 0, App.WIDTH, App.HEIGHT);

        drawBackground(middleLayer, middleLayerShift);
        drawBackground(frontLayer, frontLayerShift);

        resetLayerShift();

        switch (Controller.GAMEPHASE) {
            case Start -> start();
            case StartingAnimation -> startingAnimation();
            case Running -> running();
            case PreEndAnimation -> running();
            case End -> end();
            case HighScore -> highscore();
        }
    }


    private void start() {
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillText("Scan one of the Barcodes in front of you", App.WIDTH / 2f, App.HEIGHT / 2f);
        frontLayerShift = middleLayerShift = backLayerShift = 0;
        layerShiftScalar = 0.2;
    }

    private void startingAnimation() {
        players.forEach(player -> player.drawAnimation(graphicsContext));
        graphicsContext.setFill(Color.BLACK);
        Text text = new Text(Controller.DIFFICULTY.text);
        text.setFont(App.FONT);
        graphicsContext.fillText(Controller.DIFFICULTY.text, ((App.WIDTH - text.getLayoutBounds().getWidth()) / 2f) + frontLayerShift, App.HEIGHT / 3f);
        if (Controller.GETDELTACLOCK() > 9.9) {
            return;
        } else if (Controller.GETDELTACLOCK() > 9) {
            if (layerShiftScalar - 0.15 > 0) {
                layerShiftScalar -= 0.15;
            }
            middleLayerShift += 5 * layerShiftScalar;
            frontLayerShift += 7 * layerShiftScalar;
        } else if (Controller.GETDELTACLOCK() > 8.5) {
            layerShiftScalar += 0.2;
            middleLayerShift += 5 * layerShiftScalar;
            frontLayerShift += 7 * layerShiftScalar;
        }

    }

    private void running() {
        middleLayerShift -= 5;
        frontLayerShift -= 7;
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillText("Score: " + (int) Controller.SCORE, 10, 30);

        /*
        This code can be used for a neat fading effect if we so desired for any transitions

        Current configuration: Fade in
        (for fade out invert opacity operation and start it at 0)

        for (double opacity = 1.0; opacity > 0.0; opacity -= 0.016) {
            graphicsContext.setFill(Color.DARKBLUE);
            graphicsContext.fillRect(0, 0, App.WIDTH, App.HEIGHT);

            graphicsContext.setFill(Color.rgb(0,0,0, opacity));
            graphicsContext.fillRect(0,0,App.WIDTH, App.HEIGHT);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        */

        players.forEach(player -> player.drawAnimation(graphicsContext));

        graphicsContext.setFill(Color.SEAGREEN);
        for (Obstacle obstacle : obstacles) {
            obstacle.drawAnimation(graphicsContext);
        }
    }

    private void end() {
        players.forEach(player -> player.drawAnimation(graphicsContext));
    }

    private void highscore() {

    }

    private void drawBackground(Image image, double layerShift) {
        graphicsContext.drawImage(image, layerShift - image.getWidth() * Spritesheets.spriteScaling, App.HEIGHT - image.getHeight() * Spritesheets.spriteScaling, image.getWidth() * Spritesheets.spriteScaling, image.getHeight() * Spritesheets.spriteScaling);
        graphicsContext.drawImage(image, layerShift, App.HEIGHT - image.getHeight() * Spritesheets.spriteScaling, image.getWidth() * Spritesheets.spriteScaling, image.getHeight() * Spritesheets.spriteScaling);
        graphicsContext.drawImage(image, layerShift + image.getWidth() * Spritesheets.spriteScaling, App.HEIGHT - image.getHeight() * Spritesheets.spriteScaling, image.getWidth() * Spritesheets.spriteScaling, image.getHeight() * Spritesheets.spriteScaling);

    }

    private void resetLayerShift() {
        if (Math.abs(frontLayerShift) > frontLayer.getWidth() * Spritesheets.spriteScaling) {
            frontLayerShift = 0.0;
        }
        if (Math.abs(middleLayerShift) > middleLayer.getWidth() * Spritesheets.spriteScaling) {
            middleLayerShift = 0.0;
        }
        if (Math.abs(backLayerShift) > backLayer.getWidth() * Spritesheets.spriteScaling) {
            backLayerShift = 0.0;
        }
    }
}