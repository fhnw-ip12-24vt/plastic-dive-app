package ch.IP12.fish;

import ch.IP12.fish.model.World;
import ch.IP12.fish.model.animations.Spritesheets;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.concurrent.atomic.AtomicInteger;

public class View {
    private final GraphicsContext graphicsContext;
    private final Font font = Font.loadFont(getClass().getResourceAsStream("/fonts/MinecraftRegular-Bmg3.otf"), 12);
    private final World world;
    private double frontLayerShift = 0.0;
    private double middleLayerShift = 0.0;
    private double backLayerShift = 0.0;
    private final Image frontLayer = new Image("backgroundLayers/frontLayer.png");
    private final Image middleLayer = new Image("backgroundLayers/middleLayer.png");
    private final Image backLayer = new Image("backgroundLayers/middleLayer.png");
    private double backgroundScalar = 3.5;
    private double layerShiftScalar = 0.2;

    View(GraphicsContext graphicsContext, World world) {
        this.graphicsContext = graphicsContext;
        this.world = world;

        graphicsContext.setFont(font);
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

    private void render() {
        graphicsContext.setFill(Color.web("#3e79dd"));
        graphicsContext.fillRect(0, 0, world.getWidth(), world.getHeight());

        drawBackground(middleLayer, middleLayerShift);
        drawBackground(frontLayer, frontLayerShift);

        resetLayerShift();

        switch (world.getGamePhase()) {
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
        graphicsContext.fillText("Scan one of the Barcodes in front of you", world.getWidth() / 2f, world.getHeight() / 2f);
        frontLayerShift = middleLayerShift = backLayerShift = 0;
        layerShiftScalar = 0.2;
    }

    private void startingAnimation() {
        world.getPlayers().forEach(player -> player.drawAnimation(graphicsContext));
        graphicsContext.setFill(Color.BLACK);
        Text text = new Text(world.getDifficulty().text);
        text.setFont(world.getFont());
        graphicsContext.fillText(world.getDifficulty().text, ((world.getWidth() - text.getLayoutBounds().getWidth()) / 2f) + frontLayerShift, world.getHeight() / 3f);
        if (world.getDeltaClock() > 9.9) {
            return;
        } else if (world.getDeltaClock() > 9) {
            if (layerShiftScalar - 0.15 > 0) {
                layerShiftScalar -= 0.15;
            }
            middleLayerShift += 5 * layerShiftScalar;
            frontLayerShift += 7 * layerShiftScalar;
        } else if (world.getDeltaClock() > 8.5) {
            layerShiftScalar += 0.2;
            middleLayerShift += 5 * layerShiftScalar;
            frontLayerShift += 7 * layerShiftScalar;
        }

    }

    private void running() {
        middleLayerShift -= 5;
        frontLayerShift -= 7;
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillText("Score: " + world.getScoreWithoutDecimals(), 10, 30);

        world.getPlayers().forEach(player -> {
            player.drawAnimation(graphicsContext);
        });

        graphicsContext.setFill(Color.SEAGREEN);
        world.getObstacles().forEach(obstacle -> obstacle.drawAnimation(graphicsContext));
    }

    private void end() {
        world.getPlayers().forEach(player -> player.drawAnimation(graphicsContext));
    }

    private void highscore() {

    }

    private void drawBackground(Image image, double layerShift) {
        graphicsContext.drawImage(image, layerShift - image.getWidth() * Spritesheets.spriteScaling, world.getHeight() - image.getHeight() * Spritesheets.spriteScaling, image.getWidth() * Spritesheets.spriteScaling, image.getHeight() * Spritesheets.spriteScaling);
        graphicsContext.drawImage(image, layerShift, world.getHeight() - image.getHeight() * Spritesheets.spriteScaling, image.getWidth() * Spritesheets.spriteScaling, image.getHeight() * Spritesheets.spriteScaling);
        graphicsContext.drawImage(image, layerShift + image.getWidth() * Spritesheets.spriteScaling, world.getHeight() - image.getHeight() * Spritesheets.spriteScaling, image.getWidth() * Spritesheets.spriteScaling, image.getHeight() * Spritesheets.spriteScaling);

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