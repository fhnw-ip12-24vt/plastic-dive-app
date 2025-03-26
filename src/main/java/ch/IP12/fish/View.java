package ch.IP12.fish;

import ch.IP12.fish.model.Obstacle;
import ch.IP12.fish.model.Player;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;


public class View {
    private final GraphicsContext graphicsContext;
    private final Player player;
    private final List<Obstacle> obstacles;
    private boolean initialScene = true;
    private long clock;

    View(GraphicsContext graphicsContext, Player player, List<Obstacle> obstacles) {
        this.graphicsContext = graphicsContext;
        this.player = player;
        this.obstacles = obstacles;
    }

    /**
     * Start rendering the relevant images for the objects on screen.
     */
    public void startRendering() {
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                render();
                if (!Controller.isRunning()){
                    this.stop();
                }
            }
        };
        animationTimer.start();
    }

    /**
     * Draws the objects on the screen
     */
    private void render() {
        //System.out.println(1000/(System.currentTimeMillis()-clock));
        clock = System.currentTimeMillis();

        graphicsContext.setFill(Color.DARKBLUE);
        graphicsContext.fillRect(0, 0, App.WIDTH, App.HEIGHT);

        /*
        This code can be used for a neat fading effect if we so desired for any transitions

        Current configuration: Fade in
        (for fade out invert opacity operation and start it at 0)

        for (double opacity = 1.0; opacity > 0.0; opacity -= 0.016) {
            graphicsContext.setFill(Color.DARKBLUE);
            graphicsContext.fillRect(0, 0, App.WIDTH, App.HEIGHT);

            graphicsContext.setFill(Color.rgb(0,0,0, opacity));
            graphicsContext.fillRect(0,0,App.WIDTH, App.HEIGHT);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        */

        graphicsContext.setFill(Color.RED);
        graphicsContext.fillRect(player.getX(), player.getY(), player.getLength(), player.getHeight());
        player.drawAnimation(graphicsContext);

        for (Obstacle obstacle : obstacles) {
            graphicsContext.setFill(Color.SEAGREEN);
            graphicsContext.fillRect(obstacle.getX(), obstacle.getY(), obstacle.getLength(), obstacle.getHeight());
        }
    }
}