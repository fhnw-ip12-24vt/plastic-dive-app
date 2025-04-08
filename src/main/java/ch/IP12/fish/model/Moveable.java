package ch.IP12.fish.model;

import ch.IP12.fish.model.animations.SpriteAnimation;
import ch.IP12.fish.model.animations.Spritesheets;
import ch.IP12.fish.utils.IntUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class Moveable {
    private double x;
    private double y;

    private double speed;
    private final double length;
    private final double height;

    private double direction;

    private final double maxX;
    private final double maxY;

    //Scalar for object size;
    private final double spriteScale;


    //path to animation images
    private final SpriteAnimation animation;

    Moveable(double x, double y, double speed, double maxX, double maxY, Spritesheets spritesheets, double spriteScale) {
        this(x, y, speed, maxX, maxY, spritesheets.getSpriteAnimation(), spriteScale);
    }

    Moveable(double x, double y, double speed, double maxX, double maxY, SpriteAnimation spriteAnimation, double spriteScale) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.animation = spriteAnimation;
        this.maxX = maxX;
        this.maxY = maxY;
        this.spriteScale = spriteScale;

        this.length = animation.getWidth() * spriteScale - 5;
        this.height = animation.getHeight() * spriteScale - 5;
    }

    /**
     * Calls movement code of calling Moveable
     *
     * @param deltaTime Ensures movement amount is consistent regardless of any that may be occurring lag.
     */
    public abstract void update(double deltaTime);

    /**
     * Moves the moveable object in the specified direction.
     */
    public void move(double deltaTime, double strength) {
        x += (Math.cos(direction) * (speed * strength)) * deltaTime;
        y += (Math.sin(direction) * (speed * strength)) * deltaTime;
    }

    /**
     * Checks if the current instance collides with another object.
     *
     * @param moveable Object to be checked for overlap.
     * @return Boolean value of the check
     */
    public boolean collidesWith(Moveable moveable) {
        return IntUtils.isRangeInRange(moveable.x, moveable.x + moveable.length, this.x, this.x + this.length)
                && IntUtils.isRangeInRange(moveable.y, moveable.y + moveable.height, this.y, this.y + this.height);
    }

    public void drawAnimation(GraphicsContext graphicsContext) {
        animation.play();
        Rectangle2D viewRect = animation.getImageView().getViewport();
        graphicsContext.drawImage(animation.getImageView().getImage(), viewRect.getMinX(), viewRect.getMinY(), viewRect.getWidth(), viewRect.getHeight(), x, y, length, height);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getHeight() {
        return height;
    }

    public double getLength() {
        return length;
    }

    public SpriteAnimation getAnimation() {
        return animation;
    }

    public double getSpriteScale() {
        return spriteScale;
    }

    public double getMaxY() {
        return maxY;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

}
