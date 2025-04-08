package ch.IP12.fish.model;

import ch.IP12.fish.Controller;
import ch.IP12.fish.model.animations.Spritesheets;
import javafx.scene.paint.Color;

public class AtPlayerObstacle extends Obstacle {
    private final double creationTime = Controller.CURRENTTIMESECONDS();
    private boolean hasChanged;
    private final Player player;

    public AtPlayerObstacle(int x, int y, int speed, double maxX, double maxY, Spritesheets spriteSheet, Player player) {
        super(x, y, speed, maxX, maxY, spriteSheet);
        this.player = player;
        this.color = Color.RED;
    }

    @Override
    protected void adjustDirection() {
        if (creationTime + 2 < Controller.CURRENTTIMESECONDS() && !hasChanged) {
            double x = player.getX() - getX();
            double y = player.getY() - getY();
            setDirection(Math.atan2(y, x));
            setSpeed(getSpeed()*2);
            hasChanged = true;
        }
    }
}
