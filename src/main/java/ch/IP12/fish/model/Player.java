package ch.IP12.fish.model;

import ch.IP12.fish.components.JoystickAnalog;
import ch.IP12.fish.fileInterpreters.Logger;
import ch.IP12.fish.model.animations.SpriteAnimation;
import ch.IP12.fish.model.obstacles.Obstacle;
import ch.IP12.fish.utils.IntUtils;

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
        super((x - spriteAnimation.getWidth()), y, speed, spriteAnimation, 1, world);
        setSpeed(speed);
        this.initialXValue = (x - spriteAnimation.getWidth());
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
        double xChange = (Math.cos(getDirection()) * (getSpeed() * strength)) * deltaTime;
        double yChange = (Math.sin(getDirection()) * (getSpeed() * strength)) * deltaTime;

        if (yBoundsCheck(yChange)) {
            setY(getY() + yChange);
        }

        if (xBoundsCheck(xChange)) {
            setX(getX() + xChange);
        }
    }

    public boolean yBoundsCheck(double yChange) {
        return !(getY() + yChange > getMaxY() - getSize()) && !(getY() + yChange < 0);
    }

    public boolean xBoundsCheck(double xChange) {
        return !(getX() + xChange > getMaxX() - (getSize()*2)) && !(getX() + xChange < 0);
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
        if (hasJoystick() && joystick.getAds1115().isContinuousReadingActive()) {
            joystick.onMove((double xPos, double yPos) -> {}, () -> {});
        } else {
            Logger.getInstance().log("No joystick found for player");
        }
    }

    /**
     * Resets joystick
     */
    public void resetJoystick() {
        if (!hasJoystick()) return;
        joystick.reset();
    }
    /**
     * Checks if the current instance collides with another object.
     *
     * @param obstacle Object to be checked for overlap.
     * @return Boolean value of the check
     */
    public boolean collidesWith(Obstacle obstacle) {
        return IntUtils.collidesWith(this, obstacle);
    }

    private boolean hasJoystick() {
        return joystick != null;
    }
}
