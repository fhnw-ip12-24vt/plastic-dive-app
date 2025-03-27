package ch.IP12.fish.model;

import ch.IP12.fish.model.animations.SpriteAnimation;
import ch.IP12.fish.model.animations.Spritesheets;
import ch.IP12.fish.utils.IntUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class Moveable {
    protected double x;
    protected double y;

    protected double speed;
    protected final double length;
    protected final double height;

    protected double direction;

    protected final double maxX;
    protected final double maxY;

    protected final double spriteScale;

    //path to animation images
    protected final SpriteAnimation animation;

    Moveable(int x, int y, double speed, double maxX, double maxY, Spritesheets spritesheets, double spriteScale){
        this(x, y, speed, maxX, maxY, spritesheets.getSpriteAnimation(), spriteScale);
    }

    Moveable(int x, int y, double speed, double maxX, double maxY, SpriteAnimation spriteAnimation, double spriteScale) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.animation = spriteAnimation;
        this.maxX = maxX;
        this.maxY = maxY;
        this.spriteScale = spriteScale;

        this.length = animation.getWidth()*spriteScale-5;
        this.height = animation.getHeight()*spriteScale-5;
    }

    /**
     * Updates the Object instance every x...
     * @param deltaTime
     */
    public void update(double deltaTime, double strength){
        move(strength);
    }

    /**
     * Moves the moveable object in the specified direction.
     */
    public void move(double strength) {
        x += (Math.cos(Math.toRadians(direction))*(speed*strength));
        y += (Math.sin(Math.toRadians(direction))*(speed*strength));
    }

    /**
     * Checks if the current instance collides with another object.
     * @param moveable Object to be checked for overlap.
     * @return Boolean value of the check
     */
    public boolean collidesWith(Moveable moveable) {
        return IntUtils.isRangeInRange(moveable.x, moveable.x+moveable.length, this.x, this.x+this.length)
                && IntUtils.isRangeInRange(moveable.y, moveable.y+moveable.height, this.y, this.y+this.height);
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

    public void drawAnimation(GraphicsContext graphicsContext){
        animation.play();
        Rectangle2D viewRect = animation.getImageView().getViewport();
        graphicsContext.drawImage(animation.getImageView().getImage(), viewRect.getMinX(), viewRect.getMinY(),viewRect.getWidth(), viewRect.getHeight(), x, y, length, height);
    }
}
