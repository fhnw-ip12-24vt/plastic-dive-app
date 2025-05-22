package ch.IP12.fish.utils;

import ch.IP12.fish.model.World;
import ch.IP12.fish.model.animations.SpriteAnimation;
import ch.IP12.fish.model.animations.Spritesheets;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public enum Difficulty {
    Easy(
            5181539527925L, Spritesheets.easyDifficultyAnimation, 1.0, 1),
    Medium(
            6211734858490L, Spritesheets.mediumDifficultyAnimation, 0.75, 1.25),
    Hard(
            7751064387950L, Spritesheets.hardDifficultyAnimation, 0.5, 1.5),
    ;

    public final long barcode;
    public final SpriteAnimation animation;
    public final double timeBetweenSpawns;
    public final double pointScaling;

    Difficulty(long barcode, Spritesheets spritesheet, double timeBetweenSpawns, double pointScaling) {
        this.barcode = barcode;
        this.animation = spritesheet.getSpriteAnimation();
        this.timeBetweenSpawns = timeBetweenSpawns;
        this.pointScaling = pointScaling;
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
    public synchronized void drawAnimation(GraphicsContext graphicsContext, World world) {
        animation.play();
        Rectangle2D viewRect = animation.getImageView().getViewport();
        graphicsContext.drawImage(animation.getImageView().getImage(), viewRect.getMinX(), viewRect.getMinY(), viewRect.getWidth(), viewRect.getHeight(), world.getWidth() / 2 - viewRect.getWidth()/2, world.getHeight()/2-200, viewRect.getWidth()*3.5, viewRect.getHeight()*3.5);
        //graphicsContext.drawImage(animation.getImageView().getImage(), viewRect.getMinX(), viewRect.getMinY(), viewRect.getWidth(), viewRect.getHeight(), x, y, length, height);
    }
}

