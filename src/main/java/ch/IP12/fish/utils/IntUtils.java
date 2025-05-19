package ch.IP12.fish.utils;

import ch.IP12.fish.model.Moveable;
import ch.IP12.fish.model.Player;
import ch.IP12.fish.model.obstacles.Obstacle;

public class IntUtils {
    /**
     * @param m1 Moveable 1.
     * @param m2 Moveable 2.
     * @return Boolean value if the ranges overlap.
     */
    public static boolean collidesWith(Player m1, Obstacle m2) {
        double m1Radius = m1.getSize() / 2.0;
        double m2Radius = m2.getSize() / 2.0;

        // Compute center coordinates
        double m1CenterX = m1.getX() + 5 + m1Radius;
        double m1CenterY = m1.getY() + m1Radius;
        double m1CenterX2 = m1.getX() + 50 + m1Radius;
        double m1CenterY2 = m1.getY() + m1Radius;
        double m2CenterX = m2.getX() + m2Radius;
        double m2CenterY = m2.getY() + m2Radius;

        // Compute distance between centers
        double distanceX = m2CenterX - m1CenterX;
        double distanceY = m2CenterY - m1CenterY;
        double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

        double distanceX2 = m2CenterX - m1CenterX2;
        double distanceY2 = m2CenterY - m1CenterY2;
        double distance2 = Math.sqrt(distanceX2 * distanceX2 + distanceY2 * distanceY2);
        // Check for collision
        return (distance < (m1Radius + m2Radius) || distance2 < (m1Radius + m2Radius));

    }
}
