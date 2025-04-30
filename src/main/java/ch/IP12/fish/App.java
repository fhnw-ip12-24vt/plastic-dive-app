package ch.IP12.fish;

import ch.IP12.fish.model.World;
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

public class App extends Application {
    Context pi4j;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        pi4j = Pi4J.newAutoContext();
        World world = new World(pi4j);

        setupStage(stage);

        //Creates the area which we draw all the images on
        Canvas canvas = setupCanvas(world);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        //creates window and passes it the relevant objects (necessary for display)
        Scene scene = createScene(canvas);
        stage.setScene(scene);
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
            controller.stopGameLogic();
            pi4j.shutdown();
        });
    }

    private void setupStage(Stage stage) {
        stage.setResizable(false);
        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);
        stage.setTitle("IP12 Prototype");
    }

    private Canvas setupCanvas(World world) {
        Canvas canvas = new Canvas(world.getHeight(), world.getWidth());
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setImageSmoothing(false);

        //load custom font
        world.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/MinecraftRegular-Bmg3.otf"), 12));
        graphicsContext.setFont(world.getFont());

        return canvas;
    }

    private Scene createScene(Canvas canvas) {
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root);
        scene.setCursor(Cursor.NONE);

        return scene;
    }
}
