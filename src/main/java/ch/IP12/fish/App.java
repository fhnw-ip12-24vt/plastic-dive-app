package ch.IP12.fish;

import ch.IP12.fish.fileInterpreters.Config;
import ch.IP12.fish.fileInterpreters.LanguageLoader;
import ch.IP12.fish.fileInterpreters.Logger;
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
        Logger logger = Logger.getInstance("log.txt");
        logger.start();
        logger.log("Start sequence started");

        pi4j = Pi4J.newAutoContext();
        logger.log("Pi4J context initialized");

        World world = new World(pi4j);

        //load config and text (in selected language) into world object
        Config ignored1 = new Config("config.txt", world);
        LanguageLoader ignored2 = new LanguageLoader(world);
        logger.log("Loaded configs and language pack");

        //base settings for stage applied
        setupStage(stage);

        //Creates the area which we draw all the images on
        Canvas canvas = setupCanvas(world);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        //creates window and passes it the relevant objects (necessary for display)
        Scene scene = createScene(canvas);
        stage.setScene(scene);
        stage.show();
        logger.log("Window initialized and setup");

        //Initializes the controller and starts the game
        Controller controller = new Controller(world, scene);

        //Starts the View and passes it the relevant things that are to be displayed
        View view = new View(graphicsContext, world);
        logger.log("View and Controller initialized");

        //starts the key listeners for the main scene.
        controller.createGameKeyListeners(scene); // for debugging
        controller.startGameLogic();

        view.startRendering();
        logger.log("Start Sequence finished");

        //Stops the game if the window is exited
        stage.setOnCloseRequest(event -> {
            controller.stopGameLogic();
            pi4j.shutdown();
            logger.log("Program Shutdown");
            logger.end();
        });
    }

    private void setupStage(Stage stage) {
        stage.setResizable(false);
        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);
        stage.setTitle("IP12 Prototype");
    }

    private Canvas setupCanvas(World world) {
        Canvas canvas = new Canvas(world.getWidth(), world.getHeight());
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
