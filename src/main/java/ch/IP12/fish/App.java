package ch.IP12.fish;

import ch.IP12.fish.components.Ads1115;
import ch.IP12.fish.components.JoystickAnalog;
import ch.IP12.fish.model.Obstacle;
import ch.IP12.fish.model.Player;
import ch.IP12.fish.model.World;
import ch.IP12.fish.model.animations.Spritesheets;
import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class App extends Application {
    public static Font FONT;
    Context pi4j;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        pi4j = Pi4J.newAutoContext();


        stage.setResizable(false);
        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);

        World world = new World(pi4j);
        //Creates the player and an array list for all the obstacles


        //Creates the area which we draw all the images on
        Canvas canvas = new Canvas(world.getWidth(), world.getHeight());
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setImageSmoothing(false);



        //creates window and passes it the relevant objects (necessary for display)
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root);
        scene.setCursor(Cursor.NONE);
        stage.setScene(scene);
        stage.setTitle("IP12 Prototype");
        stage.show();

        //Initializes the controller and starts the game
        Controller controller = new Controller(world, scene);

        //Starts the View and passes it the relevant things that are to be displayed
        View view = new View(graphicsContext, world);


        //starts the key listeners for the main scene.
        controller.createGameKeyListeners(scene); // for debugging
        controller.startGameLogic();

        view.startRendering();

        //Stops the game if the window is exited
        stage.setOnCloseRequest(event -> {
            pi4j.shutdown();
            controller.stopGameLogic();
        });
        stage.setOnCloseRequest(event -> {pi4j.shutdown(); controller.stopGameLogic();});
    }

    private void setupStage(Stage stage) {
        stage.setResizable(false);
        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);
        stage.setTitle("IP12 Prototype");
    }

    private Canvas setupCanvas() {
        Canvas canvas = new Canvas(world.getHeight(), world.getWidth());
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setImageSmoothing(false);

        //load custom font
        FONT = Font.loadFont(getClass().getResourceAsStream("/fonts/MinecraftRegular-Bmg3.otf"), 12);
        graphicsContext.setFont(FONT);

        return canvas;
    }

    private Scene createScene(Canvas canvas) {
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root);
        scene.setCursor(Cursor.NONE);

        return scene;
    }
}
