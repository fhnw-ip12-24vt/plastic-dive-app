package ch.IP12.fish.model;

import ch.IP12.fish.model.animations.SpriteAnimation;
import ch.IP12.fish.model.animations.Spritesheets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Obstacle extends Moveable{
    Color color = Color.SEAGREEN; // temporary for demo
    public Obstacle(int x, int y, int speed, double maxX, double maxY, Spritesheets spriteSheet) {
        this(x, y, speed, maxX, maxY, spriteSheet.getSpriteAnimation());
    }

    public Obstacle(int x, int y, int speed, double maxX, double maxY, SpriteAnimation spriteAnimation) {
        super(x, y, speed, maxX, maxY, spriteAnimation,((Math.random()+0.5)*2));
        //forces the Obstacle to move to the left side of the screen.
        direction = Math.PI;
    }

    @Override
    public void update(double deltaTime, double strength){
        adjustDirection();
        move(strength);
    }

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
        return this.x < -250 || this.y+this.height < -250 || this.x+length > this.maxX+250 || this.y+height > this.maxY+250;
    }
}
