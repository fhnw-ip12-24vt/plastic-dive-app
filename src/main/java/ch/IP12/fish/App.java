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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.Objects;

public class App extends Application {
    Context pi4j;

    public static void main(String[] args) {
        try {
            launch(args);
        }
        catch (Exception e) {
            try {
                Logger.getInstance().logError(e.getMessage(), null);
                Logger.getInstance().end();
            } catch (Exception e1) {
                throw e;
            }
            throw e;
        }
    }

    /**
     * Starts the application
     * @param stage JavaFX Stage for things to be displayed on
     */
    public void start(Stage stage) {
        System.setProperty("sun.java2d.opengl", "True");
        System.setProperty("prism.order", "es2");

        Logger logger = Logger.getInstance("log");
        logger.start();
        logger.log("Start sequence started");

        pi4j = Pi4J.newAutoContext();
        logger.log("Pi4J context initialized");

        World world = new World(pi4j);

        //load config and text (in selected language) into the world object
        Config ignored1 = new Config("config", world);
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
        stage.requestFocus();
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

            //Close filesystem for interpreting files stored inside the jar file when the program closes
            try {
                URI uri = Objects.requireNonNull(this.getClass().getResource("/fonts/MinecraftRegular-Bmg3.otf")).toURI();

                if (uri.getScheme().equals("jar")) {
                    FileSystem fileSystem = FileSystems.getFileSystem(uri);
                    fileSystem.close();
                }
            } catch (IOException | URISyntaxException | NullPointerException ignore) {}

            logger.log("Program Shutdown");
            logger.end();
        });
    }

    private void setupStage(Stage stage) {
        //default stage settings
        stage.setResizable(false);
        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);
        stage.setTitle("Plastic Dive");
    }

    private Canvas setupCanvas(World world) {
        //load Graphics context and canvas
        Canvas canvas = new Canvas(world.getWidth(), world.getHeight());
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setImageSmoothing(false);

        //load custom font
        world.setFont(Font.loadFont(this.getClass().getResourceAsStream("/fonts/MinecraftRegular-Bmg3.otf"),32));

        return canvas;
    }

    private Scene createScene(Canvas canvas) {
        //create a visible scene that is to be displayed to the user
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root);
        scene.setCursor(Cursor.NONE);

        return scene;
    }
}
