package ch.IP12.fish.model;

import ch.IP12.fish.Controller;
import ch.IP12.fish.model.animations.Spritesheets;
import javafx.scene.paint.Color;

public class AtPlayerObstacle extends Obstacle {
    double creationTime = Controller.currentTimeSeconds();
    boolean hasChanged;
    Player player;

    public AtPlayerObstacle(int x, int y, int speed, double maxX, double maxY, Spritesheets spriteSheet, Player player) {
        super(x, y, speed, maxX, maxY, spriteSheet);
        this.player = player;
        this.color = Color.RED;
    }

    @Override
    protected void adjustDirection() {
        if (creationTime + 2 < Controller.currentTimeSeconds() && !hasChanged) {
            double x = player.getX() - this.x;
            double y = player.getY() - this.y;
            this.direction = Math.atan2(y, x);
            hasChanged = true;
        }
    }
}
