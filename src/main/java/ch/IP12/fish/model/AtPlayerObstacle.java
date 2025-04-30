package ch.IP12.fish.model;

import javafx.scene.paint.Color;

public class AtPlayerObstacle extends Obstacle {
    private final double creationTime = world.currentTimeSeconds();
    private boolean hasChanged;
    private final Player player;

    public AtPlayerObstacle(Obstacle obstacle, Player param) {
        super(obstacle);
        this.player = param;
        this.color = Color.RED;
    }

    @Override
    protected void adjustDirection() {
        if (creationTime + 2 < world.currentTimeSeconds() && !hasChanged) {
            double x = player.getX() - getX();
            double y = player.getY() - getY();
            setDirection(Math.atan2(y, x));
            setSpeed(getSpeed()*2);
            hasChanged = true;
        }
    }
}
