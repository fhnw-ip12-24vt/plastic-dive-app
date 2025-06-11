package ch.IP12.fish;

import ch.IP12.fish.fileInterpreters.Logger;
import ch.IP12.fish.model.World;
import ch.IP12.fish.scoreBoard.Scoreboard;
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
    private double layerShiftScalar = 0.2;

    private final double backgroundScalar = 3.5;

    private final Image frontLayer = new Image("/assets/frontLayer.png");
    private final Image middleLayer = new Image("/assets/middleLayer.png");
    private final Image backLayer = new Image("/assets/backLayer.png");
    private final Image scannerImage = new Image("/assets/barcodeScanner.png");

    private double upAndDownBobbing = 0;
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

        drawBackground(backLayer, backLayerShift);
        drawBackground(middleLayer, middleLayerShift);
        drawBackground(frontLayer, frontLayerShift);

        resetLayerShift();

        if (upAndDownBobbing <= 100) upAndDownBobbing = 100;

        switch (world.getGamePhase()) {
            case Start -> start();
            case StartingAnimation -> startingAnimation();
            case Running -> running();
            case End -> end();
            case HighScore -> highScore();
        }
    }


    private void start() {
        upAndDownBobbing += 0.1;

        //drawing starting image
        graphicsContext.drawImage(scannerImage, world.getWidth() / 2 - scannerImage.getWidth() * 1.5, world.getHeight() / 2 - scannerImage.getHeight() * 1.5 + Math.sin(upAndDownBobbing) * 5 - 100, scannerImage.getWidth() * 3, scannerImage.getHeight() * 3);

        writeText(world.getTextMapValue("scanText"), 70);

        //initial scalar values set
        frontLayerShift = middleLayerShift = backLayerShift = 0;
        layerShiftScalar = 0.2;
    }

    @SuppressWarnings("UnnecessaryReturnStatement")
    private void startingAnimation() {
        //draw player animation in the beginning
        world.getPlayers().forEach(player -> player.drawAnimation(graphicsContext));

        //Timings for start animation
        if (world.getDeltaClock() > 15.9) {
            return;
        } else if (world.getDeltaClock() > 15) {
            //slow shift down for start of game
            if (layerShiftScalar - 0.15 > 0) {
                layerShiftScalar -= 0.15;
            }
            backLayerShift += 3 * layerShiftScalar;
            middleLayerShift += 5 * layerShiftScalar;
            frontLayerShift += 7 * layerShiftScalar;
        } else if (world.getDeltaClock() > 14.5) {
            //shift background to the left so that everything leaves the screen to the right
            layerShiftScalar += 0.2;
            backLayerShift += 3 * layerShiftScalar;
            middleLayerShift += 5 * layerShiftScalar;
            frontLayerShift += 7 * layerShiftScalar;
        } else if (world.getDeltaClock() > 11) {
            //write tutorial text
            world.getDifficulty().drawWashingAnimation(graphicsContext,world);
            writeText(world.getTextMapValue("tutorialText"), 35);
        } else if (world.getDeltaClock() > 4.0) {
            writeText(world.getTextMapValue(("shirtInfoText" + world.getDifficulty().textName)), 35);
            world.getDifficulty().drawWashingAnimation(graphicsContext,world);
        } else if(world.getDeltaClock() > 3.2) {
            writeText(world.getTextMapValue(("shirtInfoText" + world.getDifficulty().textName)), 35);
            world.getDifficulty().drawOpeningAnimation(graphicsContext,world);
        } else {
            world.getDifficulty().drawStartingAnimation(graphicsContext,world);
        }
    }

    private void running() {
        //shift background to left
        backLayerShift -= 3;
        middleLayerShift -= 5;
        frontLayerShift -= 7;
        graphicsContext.setFill(Color.BLACK);

        //Draw score text in the top left corner
        graphicsContext.setFont(world.getFont());
        Text text = new Text("" + world.getScoreWithoutDecimals());
        text.setFont(world.getFont());
        graphicsContext.fillText("" + world.getScoreWithoutDecimals(), world.getWidth() - 5 - text.getLayoutBounds().getWidth(), 25);

        //draw each player
        world.getPlayers().forEach(player -> player.drawAnimation(graphicsContext));

        //draw all obstacles
        world.getObstacles().forEach(obstacle -> obstacle.drawAnimation(graphicsContext));
    }

    private void end() {
        //reset font to default
        graphicsContext.setFont(world.getFont());

        //draw player
        world.getPlayers().forEach(player -> player.drawAnimation(graphicsContext));
    }

    private void highScore() {
        //set font and colour for highscore table
        graphicsContext.setFill(Color.YELLOW);
        graphicsContext.setFont(fontHighscore);

        try {
            Scoreboard scoreboard = Scoreboard.getInstance(world);
            scoreboard.draw(graphicsContext);
        } catch (Exception e) {
            writeText(world.getTextMapValue("highscoreLoadingErrorText"), 0);
            Logger.getInstance().logError(e.getMessage(), world.getConfigValue("log").equals("detailed") ? e.getStackTrace(): null);
        }
        graphicsContext.setFont(world.getFont());
    }

    private void drawBackground(Image image, double layerShift) {
        graphicsContext.drawImage(image, layerShift - image.getWidth() * backgroundScalar, world.getHeight() - image.getHeight() * backgroundScalar, image.getWidth() * backgroundScalar, image.getHeight() * backgroundScalar);
        graphicsContext.drawImage(image, layerShift, world.getHeight() - image.getHeight() * backgroundScalar, image.getWidth() * backgroundScalar, image.getHeight() * backgroundScalar);
        graphicsContext.drawImage(image, layerShift + image.getWidth() * backgroundScalar, world.getHeight() - image.getHeight() * backgroundScalar, image.getWidth() * backgroundScalar, image.getHeight() * backgroundScalar);
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

    private void writeText(String string, int yOffsetDown) {
        Text text = new Text(string);
        text.setFont(world.getFont());

        graphicsContext.setFill(Color.BLACK);
        graphicsContext.setFont(world.getFont());
        graphicsContext.fillText(text.getText(), world.getWidth() / 2 - text.getLayoutBounds().getWidth() / 2, world.getHeight() / 2 + yOffsetDown);

    }
}