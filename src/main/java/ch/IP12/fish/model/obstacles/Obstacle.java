package ch.IP12.fish.model.obstacles;

import ch.IP12.fish.model.Moveable;
import ch.IP12.fish.model.World;
import ch.IP12.fish.model.animations.SpriteAnimation;
import ch.IP12.fish.model.animations.Spritesheets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Obstacle extends Moveable {
    /**
     * @param y position on the y-axis to spawn at
     * @param speed speed obstacle moves at
     * @param world DTO (Data Transfer Object)
     */
    public Obstacle(double y, double speed,  World world) {
        super(world.getWidth(), y, speed, Spritesheets.ObstacleSprite.getSpriteAnimation(), ((Math.random() + 0.5) * 2), world);
        //-200 because we dont want anything to be on the very bottom of the screen since it depicts the bottom of the ocean
        //forces the Obstacle to move to the left side of the screen.
        setDirection(Math.PI);
    }

    /**
     * @param obstacle Creates new instance based on provided Obstacle object
     */
    public Obstacle(Obstacle obstacle) {
        super(obstacle.getX(), obstacle.getY(), obstacle.getSpeed(), obstacle.getAnimation(), obstacle.getSpriteScale(), obstacle.getWorld());
        setDirection(Math.PI);
        setAnimation(Spritesheets.ObstacleSprite.getSpriteAnimation());
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

    /**
     * @return Whether Obstacle is out of bounds
     */
    public boolean isOutsideBounds(){
        //check if any of the values exceed the 250 buffer in
        return getX() < -250 || getY()+getSize() < -250 || getX()+getSize() > getMaxX()+250 || getY()+getSize() > getMaxY()+250;
    }
}
