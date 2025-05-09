package ch.IP12.fish.model;

import ch.IP12.fish.model.animations.SpriteAnimation;
import ch.IP12.fish.model.animations.Spritesheets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Obstacle extends Moveable {
    protected Color color = Color.SEAGREEN; // temporary for demo

    /**
     * @param y position on the y-axis to spawn at
     * @param speed speed obstacle moves at
     * @param spriteAnimation Animation that is used to draw obstacle
     * @param world DTO (Data Transfer Object)
     */
    public Obstacle(double y, double speed, SpriteAnimation spriteAnimation, World world) {
        super(world.getWidth(), y, speed, spriteAnimation, ((Math.random() + 0.5) * 2), world);
        //forces the Obstacle to move to the left side of the screen.
        setDirection(Math.PI);
    }

    /**
     * @param obstacle Creates new instance based on provided Obstacle object
     */
    public Obstacle(Obstacle obstacle) {
        super(obstacle.getX(), obstacle.getY(), obstacle.getSpeed(), obstacle.getAnimation(), obstacle.getSpriteScale(), obstacle.getWorld());
        color = obstacle.color;
        setDirection(Math.PI);
    }

    /**
     * Updates position and calls adjustDirection() for this Obstacle
     * @param deltaTime Ensures movement amount is consistent regardless of any that may be occurring lag.
     */
    @Override
    public void update(double deltaTime) {
        adjustDirection();
        move(deltaTime, 1);
    }

    /**
     * Empty method for subclasses to inherit to change their movement behaviour with in some way
     */
    protected void adjustDirection() {
        // this is an empty function since we want the default to move in a straight line.
    }

    @Override
    public synchronized void drawAnimation(GraphicsContext graphicsContext) {
        // temporary for demo
        graphicsContext.setFill(color);
        graphicsContext.fillRect(this.getX(), this.getY(), this.getLength(), this.getHeight());
    }

    /**
     * @return Whether Obstacle is out of bounds
     */
    public boolean isOutsideBounds(){
        //check if any of the values exceed the 250 buffer in
        return getX() < -250 || getY()+getHeight() < -250 || getX()+getLength() > getMaxX()+250 || getY()+getHeight() > getMaxY()+250;
    }
}
