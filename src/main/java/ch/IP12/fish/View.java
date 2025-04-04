package ch.IP12.fish;

import ch.IP12.fish.model.Obstacle;
import ch.IP12.fish.model.Player;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.List;


public class View {
    private final GraphicsContext graphicsContext;
    private final Player player;
    private final List<Obstacle> obstacles;
    private double frontLayerShift = 0.0;
    private double middleLayerShift = 0.0;
    private double backLayerShift = 0.0;
    private long clock;
    private final Image frontLayer = new Image("frontLayer.png");
    private final Image middleLayer = new Image("middleLayer.png");
    private final Image backLayer = new Image("middleLayer.png");
    double backgroundScalar = 3.5;
    double layerShiftScalar = 0.2;

    View(GraphicsContext graphicsContext, Player player, List<Obstacle> obstacles) {
        this.graphicsContext = graphicsContext;
        this.player = player;
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
            case BeforeEndAnimation -> running();
            case End -> end();
            case HighScore -> highscore();
        }
    }


    private void start() {
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.strokeText("Scan smth", App.WIDTH / 2f, App.HEIGHT / 2f);
    }

    private void startingAnimation() {
        player.drawAnimation(graphicsContext);
        graphicsContext.strokeText(Controller.DIFFICULTY.text,  App.WIDTH / 2f, App.HEIGHT / 2f);
        if (Controller.getDELTACLOCK() > 9.9) {
            return;
        } else if (Controller.getDELTACLOCK() > 9.5) {
            layerShiftScalar -= 0.2;
            middleLayerShift += 5 * layerShiftScalar;
            frontLayerShift += 7 * layerShiftScalar;
        } else if (Controller.getDELTACLOCK() > 9) {
            layerShiftScalar += 0.2;
            middleLayerShift += 5 * layerShiftScalar;
            frontLayerShift += 7 * layerShiftScalar;
        }

    }

    private void running() {
        middleLayerShift -= 5;
        frontLayerShift -= 7;


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

        player.drawAnimation(graphicsContext);

        graphicsContext.setFill(Color.SEAGREEN);
        for (Obstacle obstacle : obstacles) {
            obstacle.drawAnimation(graphicsContext);
        }
    }

    private void end() {
        player.drawAnimation(graphicsContext);
    }

    private void highscore() {

    }

    private void drawBackground(Image image, double layerShift) {
        graphicsContext.drawImage(image, layerShift - image.getWidth() * backgroundScalar, App.HEIGHT - image.getHeight() * backgroundScalar, image.getWidth() * backgroundScalar, image.getHeight() * backgroundScalar);
        graphicsContext.drawImage(image, layerShift, App.HEIGHT - image.getHeight() * backgroundScalar, image.getWidth() * backgroundScalar, image.getHeight() * backgroundScalar);
        graphicsContext.drawImage(image, layerShift + image.getWidth() * backgroundScalar, App.HEIGHT - image.getHeight() * backgroundScalar, image.getWidth() * backgroundScalar, image.getHeight() * backgroundScalar);

    }
    private void resetLayerShift() {
        if (Math.abs(frontLayerShift) > frontLayer.getWidth() * backgroundScalar) {
            frontLayerShift = 0.0;
        }
        if (Math.abs(middleLayerShift) > middleLayer.getWidth() * backgroundScalar) {
            middleLayerShift = 0.0;
        }
        if (Math.abs(backLayerShift) > backLayer.getWidth() * backgroundScalar) {
            backLayerShift = 0.0;
        }
    }
}