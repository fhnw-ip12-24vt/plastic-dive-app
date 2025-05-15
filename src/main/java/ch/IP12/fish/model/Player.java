package ch.IP12.fish.model;

import ch.IP12.fish.components.JoystickAnalog;
import ch.IP12.fish.model.animations.SpriteAnimation;
import ch.IP12.fish.model.animations.Spritesheets;

public class Player extends Moveable {
    private final double initialXValue;
    private final double initialYValue;

    private final JoystickAnalog joystick;

    /**
     * @param x location on x-axis to spawn at
     * @param y location on y-axis to spawn at
     * @param speed Top speed that player can move at
     * @param spriteAnimation Animation that is used to draw player
     * @param joystick Joystick that inputs come from
     * @param world DTO (Data Transfer Object)
     */
    public Player(double x, double y, double speed, SpriteAnimation spriteAnimation, JoystickAnalog joystick, World world) {
        super((x - spriteAnimation.getWidth() * Spritesheets.spriteScaling), y, speed, spriteAnimation, Spritesheets.spriteScaling, world);
        setSpeed(speed);
        this.initialXValue = (x - spriteAnimation.getWidth() * Spritesheets.spriteScaling);
        this.initialYValue = y;
        this.joystick = joystick;
    }

    /**
     * Moves the player a set distance in a direction based on player's speed, the provided Strength value and the direction it is moving in
     * @param deltaTime Ensures movement amount is consistent regardless of any lag that may be occurring.
     * @param strength Strength of movement for player  Scale: 0 - 1
     */
    @Override
    public void move(double deltaTime, double strength) {
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

    /**
     * Updates player Position
     * @param deltaTime Ensures movement amount is consistent regardless of any lag that may be occurring.
     */
    @Override
    public void update(double deltaTime){
        if (!hasJoystick()) return;
        setDirection(joystick.getDirection());
        if (joystick.getStrength() > 0.0) {
            move(deltaTime, joystick.getStrength());
        }
    }

    /**
     * Resets player position to initial values
     */
    public void resetPosition() {
        setX(initialXValue);
        setY(initialYValue);
    }

    /**
     * Moves player to the right by 15
     */
    public void moveRight(double deltaTime) {
        setX(getX() + (500*deltaTime));
    }

    /**
     * Starts player joystick logic
     */
    public void startJoystick() {
        if (hasJoystick()) {
            joystick.onMove((double xPos, double yPos) -> {}, () -> {});
        } else {
            System.out.println("No joystick found");
        }
    }

    /**
     * Resets joystick
     */
    public void resetJoystick() {
        if (!hasJoystick()) return;
        joystick.reset();
    }

    private boolean hasJoystick() {
        return joystick != null;
    }
}
