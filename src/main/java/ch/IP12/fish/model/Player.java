package ch.IP12.fish.model;

import ch.IP12.fish.components.JoystickAnalog;
import ch.IP12.fish.model.animations.SpriteAnimation;
import ch.IP12.fish.model.animations.Spritesheets;

public class Player extends Moveable{
    protected final boolean[] tempDir = new boolean[4];

    private final double maxSpeed;
    private final double friction = 0.1;
    private final double acceleration = 0.2;

    private final double initialXValue;
    private final double initialYValue;

    public Player(int x, int y, double speed, double maxX, double maxY, Spritesheets spriteSheet) {
        this(x,y,speed,maxX, maxY, spriteSheet.getSpriteAnimation());
    }

    public Player(int x, int y, double speed, double maxX, double maxY, SpriteAnimation spriteAnimation) {
        super(x, y, speed, maxX, maxY, spriteAnimation, 3);
        maxSpeed = speed;
        this.speed = 0;
        this.initialXValue = x;
        this.initialYValue = y;
    }

    /**
     * Overridden move function from super class for reason of temporary control scheme
     */
    @Override
    public void move(double strength) {
        if (speed < maxSpeed) {
            speed += acceleration;
        }

        x += (Math.cos(direction)*(speed*strength));
        y += (Math.sin(direction)*(speed*strength));
    }

    @Override
    public void update(double deltaTime, double strength){
        if (strength > 0.0){
            move(strength);
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

    public void setTempDir(boolean val, int index) {
        tempDir[index] = val;
    }
}
