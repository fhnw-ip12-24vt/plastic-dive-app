package ch.IP12.fish.model;

import ch.IP12.fish.Controller;
import ch.IP12.fish.model.animations.Spritesheets;

public class AtPlayerObstacle extends Obstacle {
    long creationTime;
    boolean hasChanged;
    Player player;

    public AtPlayerObstacle(int x, int y, int speed, double maxX, double maxY, Spritesheets spriteSheet, Player player) {
        super(x, y, speed, maxX, maxY, spriteSheet);
        creationTime = System.currentTimeMillis();
        this.player = player;
    }

    @Override
    protected void adjustDirection() {
        if (creationTime > Controller.getDELTACLOCK()) {

        }
    }
}
