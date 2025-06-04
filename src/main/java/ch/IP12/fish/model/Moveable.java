package ch.IP12.fish.model;

import ch.IP12.fish.model.animations.SpriteAnimation;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class Moveable {
    protected final World world;

    private double x;
    private double y;

    private double speed;
    private final double size;

    //Dircetion object is moving in, in radians
    private double direction;

    //Scalar for object size;
    private final double spriteScale;


    //path to animation images
    private SpriteAnimation animation;
    /**
     * @param x x-axis postion to spawn object at
     * @param y y-axis position to spawn object at
     * @param speed Speed object moves at
     * @param spriteAnimation Animation for object to draw, and to inherit size from (width, height)
     * @param spriteScale scale for animation size to be increased by
     * @param world DTO (Data Transfer Object)
     */
    protected Moveable(double x, double y, double speed, SpriteAnimation spriteAnimation, double spriteScale, World world) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.animation = spriteAnimation;
        this.spriteScale = spriteScale;

        this.size = animation.getHeight() * spriteScale;

        this.world = world;
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



    public void drawAnimation(GraphicsContext graphicsContext) {
        animation.play();
        Rectangle2D viewRect = animation.getImageView().getViewport();
        graphicsContext.drawImage(animation.getImageView().getImage(), viewRect.getMinX(), viewRect.getMinY(), viewRect.getWidth(), viewRect.getHeight(), x, y, animation.getWidth(), animation.getHeight());
    }

    /**
     * @return X-axis position of Moveable object
     */
    public double getX() {
        return x;
    }

    /**
     * @return Y-axis position of Moveable object
     */
    public double getY() {
        return y;
    }

    /**
     * @return Size of object
     */
    public double getSize() {
        return size;
    }

    /**
     * @return This objects SpriteAnimation
     */
    public SpriteAnimation getAnimation() {
        return animation;
    }

    /**
     * @return Scalar for the sprite
     */
    public double getSpriteScale() {
        return spriteScale;
    }

    /**
     * @return Limit for y-axis before going off-screen
     */
    public double getMaxY() {
        return world.getHeight();
    }

    /**
     * @return Limit for x-axis before going off-screen
     */
    public double getMaxX() {
        return world.getWidth();
    }

    /**
     * @return Current direction in radians
     */
    public double getDirection() {
        return direction;
    }

    /**
     * Sets current direction to provided one
     * @param direction New direction in radians
     */
    public void setDirection(double direction) {
        this.direction = direction;
    }

    /**
     * @return Speed of this object
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * @param speed Sets this objects speed to provided value
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * @param y Set y-axis position to provided one
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @param x Set x-axis position to provided one
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return DTO this object uses
     */
    public World getWorld() {
        return world;
    }

    /**
     * @param animation Sets SpriteAnimation
     */
    public void setAnimation(SpriteAnimation animation) {
        this.animation = animation;
    }
}
