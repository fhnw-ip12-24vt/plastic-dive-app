package ch.IP12.fish.model;

import ch.IP12.fish.model.animations.SpriteAnimation;
import ch.IP12.fish.model.animations.Spritesheets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Obstacle extends Moveable {
    protected Color color = Color.SEAGREEN; // temporary for demo

    public Obstacle(double x, double y, double speed, double maxX, double maxY, Spritesheets spriteSheet) {
        this(x, y, speed, maxX, maxY, spriteSheet.getSpriteAnimation());
    }

    public Obstacle(double x, double y, double speed, double maxX, double maxY, SpriteAnimation spriteAnimation) {
        super(x, y, speed, maxX, maxY, spriteAnimation, ((Math.random() + 0.5) * 2));
        //forces the Obstacle to move to the left side of the screen.
        setDirection(Math.PI);
    }

    public Obstacle(Obstacle other) {
        this(other.getX(), other.getY(), other.getSpeed(), other.getMaxX(), other.getMaxY(), other.getAnimation());
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
