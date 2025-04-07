package ch.IP12.fish.model;

import ch.IP12.fish.model.animations.SpriteAnimation;
import ch.IP12.fish.model.animations.Spritesheets;

public class Player extends Moveable{
    private final double maxSpeed;
    private final double friction = 0.1;
    private final double acceleration = 0.2;

    private final double initialXValue;
    private final double initialYValue;

    public Player(int x, int y, double speed, double maxX, double maxY, Spritesheets spriteSheet) {
        this(x,y,speed,maxX, maxY, spriteSheet.getSpriteAnimation());
    }

    public Player(int x, int y, double speed, double maxX, double maxY, SpriteAnimation spriteAnimation) {
        super((int) (x-spriteAnimation.getWidth()*Spritesheets.spriteScaling), y, speed, maxX, maxY, spriteAnimation, Spritesheets.spriteScaling);
        maxSpeed = speed;
        this.speed = 0;
        this.initialXValue = x;
        this.initialYValue = y;
    }

    /**
     * Overridden move function from super class for reason of temporary control scheme
     */
    @Override
    public void move(double deltaTime,double strength) {
        if (speed < maxSpeed) {
            speed += acceleration;
        }

        double xChange = (Math.cos(direction)*(speed*strength)) * deltaTime;
        double yChange = (Math.sin(direction)*(speed*strength)) * deltaTime;

        if (x+xChange > maxX || y+yChange > maxY || x+xChange < 0 || y+yChange < 0) {
            return;
        }

        x += xChange;
        y += yChange;
    }

    @Override
    public void update(double deltaTime, double strength){
        if (strength > 0.0){
            move(deltaTime,strength);
        } else {
            doFriction();
        }
    }

    public void update(double deltaTime, double strength, double direction){
        this.direction = direction;
        update(deltaTime, strength);
    }

    public void doFriction() {
        if(speed > 0){
            speed -= friction;
        }
    }

    public void resetPosition() {
        x = initialXValue;
        y = initialYValue;
    }

    public void moveRight() {
        x += 15;
    }
}
