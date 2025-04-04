package ch.IP12.fish.model;

import ch.IP12.fish.model.animations.SpriteAnimation;
import ch.IP12.fish.model.animations.Spritesheets;

public abstract class Obstacle extends Moveable{
    public Obstacle(int x, int y, int speed, double maxX, double maxY, Spritesheets spriteSheet) {
        this(x, y, speed, maxX, maxY, spriteSheet.getSpriteAnimation());
    }

    public Obstacle(int x, int y, int speed, double maxX, double maxY, SpriteAnimation spriteAnimation) {
        super(x, y, speed, maxX, maxY, spriteAnimation,((Math.random()+0.5)*2));
        //forces the Obstacle to move to the left side of the screen.
        direction = 180;
    }

    @Override
    public void update(double deltaTime, double strength){
        adjustDirection();
        move(strength);
    }

    protected abstract void adjustDirection();
}
