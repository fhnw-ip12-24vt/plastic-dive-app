package ch.IP12.fish.scenes;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public class Scenes {
    public static Scene getGameScene(javafx.scene.Node node) {
        StackPane root = new StackPane(node);
        Scene scene = new Scene(root);
        scene.setCursor(Cursor.NONE);
        return scene;
    }

    public static Scene getMainScene() {
        StackPane root = new StackPane();
        Scene scene = new Scene(root);
        scene.setCursor(Cursor.NONE);
        return scene;
    }

    public static Scene getEndScene() {
        StackPane root = new StackPane();
        Scene scene = new Scene(root);
        scene.setCursor(Cursor.NONE);
        return scene;
    }

    public static Scene getScoreboardScene() {
        StackPane root = new StackPane();
        Scene scene = new Scene(root);
        scene.setCursor(Cursor.NONE);
        return scene;
    }
}
