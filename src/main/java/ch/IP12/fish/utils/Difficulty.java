package ch.IP12.fish.utils;

import ch.IP12.fish.model.World;
import ch.IP12.fish.model.animations.SpriteAnimation;
import ch.IP12.fish.model.animations.Spritesheets;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public enum Difficulty {
    Easy(
            5181539527925L, Spritesheets.easyStarting, Spritesheets.easyOpening, Spritesheets.easyWashing, 1.5, 1, "Easy"),
    Medium(
            6211734858490L, Spritesheets.mediumStarting, Spritesheets.mediumOpening, Spritesheets.mediumWashing, 1, 1.25, "Medium"),
    Hard(
            7751064387950L, Spritesheets.hardStarting, Spritesheets.hardOpening, Spritesheets.hardWashing, 0.3, 1.5, "Hard"),
    ;

    public final long barcode;
    public final SpriteAnimation startingAnimation;
    public final SpriteAnimation openingAnimation;
    public final SpriteAnimation washingAnimation;
    public final double timeBetweenSpawns;
    public final double pointScaling;
    public final String textName;

    Difficulty(long barcode, Spritesheets startingSprites, Spritesheets openingSprites, Spritesheets washingSprites,double timeBetweenSpawns, double pointScaling, String textName) {
        this.barcode = barcode;
        this.startingAnimation = startingSprites.getSpriteAnimation();
        this.openingAnimation = openingSprites.getSpriteAnimation();
        this.washingAnimation = washingSprites.getSpriteAnimation();
        this.timeBetweenSpawns = timeBetweenSpawns;
        this.pointScaling = pointScaling;
        this.textName = textName;
    }


    public static Difficulty getDifficulty(long barcode) throws RuntimeException {
        if (Easy.barcode == barcode) {
            return Easy;
        } else if (Medium.barcode == barcode) {
            return Medium;
        } else if (Hard.barcode == barcode) {
            return Hard;
        }

        //if provided barcode is invalid throw error
        throw new RuntimeException("bad barcode: " + barcode);
    }

    public synchronized void drawStartingAnimation(GraphicsContext graphicsContext, World world) {
        Rectangle2D viewRect = startingAnimation.getImageView().getViewport();
        graphicsContext.drawImage(startingAnimation.getImageView().getImage(), viewRect.getMinX(), viewRect.getMinY(), viewRect.getWidth(), viewRect.getHeight(), world.getWidth() / 2 - viewRect.getWidth()/2, world.getHeight()/2-200, viewRect.getWidth()*3.5, viewRect.getHeight()*3.5);
    }

    public synchronized void drawOpeningAnimation(GraphicsContext graphicsContext, World world) {
        openingAnimation.play();
        Rectangle2D viewRect = openingAnimation.getImageView().getViewport();
        graphicsContext.drawImage(openingAnimation.getImageView().getImage(), viewRect.getMinX(), viewRect.getMinY(), viewRect.getWidth(), viewRect.getHeight(), world.getWidth() / 2 - viewRect.getWidth()/2, world.getHeight()/2-200, viewRect.getWidth()*3.5, viewRect.getHeight()*3.5);
    }

    public synchronized void drawWashingAnimation(GraphicsContext graphicsContext, World world) {
        washingAnimation.play();
        Rectangle2D viewRect = washingAnimation.getImageView().getViewport();
        graphicsContext.drawImage(washingAnimation.getImageView().getImage(), viewRect.getMinX(), viewRect.getMinY(), viewRect.getWidth(), viewRect.getHeight(), world.getWidth() / 2 - viewRect.getWidth()/2, world.getHeight()/2-200, viewRect.getWidth()*3.5, viewRect.getHeight()*3.5);
    }
}

