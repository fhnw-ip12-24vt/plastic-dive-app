package ch.IP12.fish.model;

import ch.IP12.fish.components.JoystickAnalog;
import ch.IP12.fish.model.animations.SpriteAnimation;
import ch.IP12.fish.model.animations.Spritesheets;

public class Player extends Moveable {
    private final double maxSpeed;
    private final double friction = 0.1;
    private final double acceleration = 0.2;

    private final double initialXValue;
    private final double initialYValue;

    private final JoystickAnalog joystick;

    public Player(double x, double y, double speed, double maxX, double maxY, Spritesheets spriteSheet, JoystickAnalog joystick) {
        this(x, y, speed, maxX, maxY, spriteSheet.getSpriteAnimation(), joystick);
    }

    public Player(double x, double y, double speed, double maxX, double maxY, SpriteAnimation spriteAnimation, JoystickAnalog joystick) {
        super((x - spriteAnimation.getWidth() * Spritesheets.spriteScaling), y, speed, maxX, maxY, spriteAnimation, Spritesheets.spriteScaling);
        maxSpeed = speed;
        setSpeed(0);
        this.initialXValue = (x - spriteAnimation.getWidth() * Spritesheets.spriteScaling);
        this.initialYValue = y;
        this.joystick = joystick;
    }

    @Override
    public void move(double deltaTime, double strength) {
        if (getSpeed() < maxSpeed) {
            setSpeed(getSpeed() + acceleration);
        }

        //check for possible exit from bounds of screen before doing movement
        double xChange = (Math.cos(getDirection()) * (getSpeed() * strength));
        double yChange = (Math.sin(getDirection()) * (getSpeed() * strength));

        if (!(getY() + yChange > getMaxY() - getHeight()) && !(getY() + yChange < 0)) {
            setY(getY() + yChange);
        }

        if (!(getX() + xChange > getMaxX() - getLength()) && !(getX() + xChange < 0)) {
            setX(getX() + xChange);
        }
    }

    public void update(double deltaTime) {
        if (!hasJoystick()) return;
        setDirection(joystick.getDirection());
        if (joystick.getStrength() > 0.0) {
            move(deltaTime, joystick.getStrength());
        } else {
            doFriction();
        }
    }

    public void doFriction() {
        if (getSpeed() > 0) {
            setSpeed(getSpeed() - friction);
        }
    }

    public void resetPosition() {
        setX(initialXValue);
        setY(initialYValue);
    }

    public void moveRight() {
        setX(getX() + 15);
    }

    public void startJoystick() {
        if (hasJoystick()) {
            joystick.onMove((double xPos, double yPos) -> {

            }, () -> {});
        } else {
            System.out.println("No joystick found");
        }
    }

    public void resetJoystick() {
        if (!hasJoystick()) return;
        joystick.reset();
    }

    private boolean hasJoystick() {
        return joystick != null;
    }
}
