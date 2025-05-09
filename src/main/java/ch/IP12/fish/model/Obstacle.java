package ch.IP12.fish.model;

import ch.IP12.fish.model.animations.SpriteAnimation;
import ch.IP12.fish.model.animations.Spritesheets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Obstacle extends Moveable {
    protected Color color = Color.SEAGREEN; // temporary for demo

    public Obstacle(double y, double speed, SpriteAnimation spriteAnimation, World world) {
        super(world.getWidth(), y, speed, spriteAnimation, ((Math.random() + 0.5) * 2), world);
        //forces the Obstacle to move to the left side of the screen.
        setDirection(Math.PI);
    }

    public Obstacle(Obstacle obstacle) {
        super(obstacle.getX(), obstacle.getY(), obstacle.getSpeed(), obstacle.getAnimation(), obstacle.getSpriteScale(), obstacle.getWorld());
        color = obstacle.color;
        setDirection(Math.PI);
    }

    @Override
    public void update(double deltaTime) {
        adjustDirection();
        move(deltaTime, 1);
    }

    /**
     * Adjusts direction of obstacle in specified way.
     */
    protected void adjustDirection() {
        // this is an empty function since we want the default to move in a straight line.
    }

    @Override
    public void drawAnimation(GraphicsContext graphicsContext) {
        // temporary for demo
        graphicsContext.setFill(color);
        graphicsContext.fillRect(this.getX(), this.getY(), this.getLength(), this.getHeight());
    }

    public boolean isOutsideBounds(){
        //check if any of the values exceed the 250 buffer in
        return getX() < -250 || getY()+getHeight() < -250 || getX()+getLength() > getMaxX()+250 || getY()+getHeight() > getMaxY()+250;
    }
}
