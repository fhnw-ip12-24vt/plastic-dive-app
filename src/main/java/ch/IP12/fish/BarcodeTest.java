package ch.IP12.fish;

import ch.IP12.fish.difficultySelector.BarcodeScanner;
import ch.IP12.fish.scenes.Scenes;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class BarcodeTest extends Application {
    static int WIDTH = 800, HEIGHT = 600;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        stage.setFullScreenExitHint("");

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setImageSmoothing(false);

        Scene scene = Scenes.getGameScene(canvas);
        stage.setScene(scene);
        stage.setTitle("IP12 barcode test");
        stage.show();

        BarcodeScanner bs = new BarcodeScanner(scene);

        AnimationTimer view = new AnimationTimer() {
            @Override
            public void handle(long l) {
                graphicsContext.clearRect(0, 0, WIDTH, HEIGHT);
                graphicsContext.strokeText(bs.getDifficulty(), WIDTH/2.0, HEIGHT/2.0);
            }
        };

        bs.startListening();
        view.start();

        stage.setOnCloseRequest(event -> {
            bs.stopListening();
            view.stop();
        });
    }
}
