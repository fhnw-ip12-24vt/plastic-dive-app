package ch.IP12.fish;

import ch.IP12.fish.fileInterpreters.Logger;
import ch.IP12.fish.model.World;
import ch.IP12.fish.model.animations.Spritesheets;
import ch.IP12.fish.scoreBoard.Scoreboard;
import ch.IP12.fish.scoreBoard.ScoreboardEnitity;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class View {
    private final GraphicsContext graphicsContext;

    private final World world;
    private double frontLayerShift = 0.0;
    private double middleLayerShift = 0.0;
    private double backLayerShift = 0.0;
    private final Image frontLayer = new Image("/assets/frontLayer.png");
    private final Image middleLayer = new Image("/assets/middleLayer.png");
    private final Image backLayer = new Image("/assets/middleLayer.png");
    private final Image scannerImage = new Image("/assets/barcodeScanner.png");
    private double backgroundScalar = 3.5;
    private double layerShiftScalar = 0.2;
    private double upAndDownBobing = 0;
    private final Font fontHighscore;

    View(GraphicsContext graphicsContext, World world) {
        this.graphicsContext = graphicsContext;
        this.world = world;

        this.fontHighscore = Font.loadFont(world.getFont().getName(), 20);
        graphicsContext.setFont(world.getFont());
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

        if (upAndDownBobing <= 100) upAndDownBobing = 100;

        switch (world.getGamePhase()) {
            case Start -> start();
            case StartingAnimation -> startingAnimation();
            case Running -> running();
            case PreEndAnimation -> running();
            case End -> end();
            case HighScore -> highScore();
        }
    }


    private void start() {
        upAndDownBobing += 0.1;
        //drawing starting image
        graphicsContext.drawImage(scannerImage, world.getWidth() / 2 - scannerImage.getWidth() * 1.5, world.getHeight() / 2 - scannerImage.getHeight() * 1.5 + Math.sin(upAndDownBobing) * 5 - 100, scannerImage.getWidth() * 3, scannerImage.getHeight() * 3);
        Text text = new Text(world.getTextMapValue("scanText"));
        text.setFont(world.getFont());
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.setFont(world.getFont());
        graphicsContext.fillText(text.getText(), world.getWidth() / 2 - text.getLayoutBounds().getWidth() / 2, world.getHeight() / 2 + scannerImage.getHeight() * 1.5);
        //initial scalar values set
        frontLayerShift = middleLayerShift = backLayerShift = 0;
        layerShiftScalar = 0.2;
    }

    private void startingAnimation() {
        //draw player animation in the beginning
        world.getPlayers().forEach(player -> player.drawAnimation(graphicsContext));
        Text text = new Text(world.getTextMapValue("shirtInfoText"));
        text.setFont(world.getFont());
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.setFont(world.getFont());
        graphicsContext.fillText(text.getText(), world.getWidth() / 2 - text.getLayoutBounds().getWidth() / 2, world.getHeight() / 2 + scannerImage.getHeight() * 1.5);

        world.getDifficulty().drawAnimation(graphicsContext,world);

        //Timings for start animation
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
        //shift background to left
        middleLayerShift -= 5;
        frontLayerShift -= 7;
        graphicsContext.setFill(Color.BLACK);

        //Draw score text in top left corner
        graphicsContext.setFont(world.getFont());
        Text text = new Text("" + world.getScoreWithoutDecimals());
        text.setFont(world.getFont());
        graphicsContext.fillText("" + world.getScoreWithoutDecimals(), world.getWidth() - 5 - text.getLayoutBounds().getWidth(), 25);

        //draw each player
        world.getPlayers().forEach(player -> player.drawAnimation(graphicsContext));

        //draw all obstacles
        graphicsContext.setFill(Color.SEAGREEN);
        world.getObstacles().forEach(obstacle -> obstacle.drawAnimation(graphicsContext));
    }

    private void end() {
        //reset font to default
        graphicsContext.setFont(world.getFont());

        //draw player
        world.getPlayers().forEach(player -> player.drawAnimation(graphicsContext));
    }

    private void highScore() {
        graphicsContext.setFill(Color.YELLOW);
        graphicsContext.setFont(fontHighscore);

        try {
            Scoreboard scoreboard = Scoreboard.getInstance(world);
            scoreboard.draw(graphicsContext);
        } catch (Exception e) {
            graphicsContext.fillText(world.getTextMapValue("highscoreLoadingErrorText"), world.getWidth() / 2f, world.getHeight() / 2f);
            Logger.getInstance().logError(e.getMessage(), world.getConfigValue("log").equals("detailed") ? e.getStackTrace(): null);
        }
        graphicsContext.setFont(world.getFont());
    }

    private void drawBackground(Image image, double layerShift) {
        double backgroundScaling = 4;
        graphicsContext.drawImage(image, layerShift - image.getWidth() * backgroundScaling, world.getHeight() - image.getHeight() * backgroundScaling, image.getWidth() * backgroundScaling, image.getHeight() * backgroundScaling);
        graphicsContext.drawImage(image, layerShift, world.getHeight() - image.getHeight() * backgroundScaling, image.getWidth() * backgroundScaling, image.getHeight() * backgroundScaling);
        graphicsContext.drawImage(image, layerShift + image.getWidth() * backgroundScaling, world.getHeight() - image.getHeight() * backgroundScaling, image.getWidth() * backgroundScaling, image.getHeight() * backgroundScaling);
    }

    private void resetLayerShift() {
        double backgroundScaling = 4;
        if (Math.abs(frontLayerShift) > frontLayer.getWidth() * backgroundScaling) {
            frontLayerShift = 0.0;
        }
        if (Math.abs(middleLayerShift) > middleLayer.getWidth() * backgroundScaling) {
            middleLayerShift = 0.0;
        }
        if (Math.abs(backLayerShift) > backLayer.getWidth() * backgroundScaling) {
            backLayerShift = 0.0;
        }
    }
}