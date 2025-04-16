package ch.IP12.fish;

import ch.IP12.fish.components.Ads1115;
import ch.IP12.fish.components.JoystickAnalog;
import ch.IP12.fish.model.Obstacle;
import ch.IP12.fish.model.Player;
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
    static int WIDTH = 1920;
    static int HEIGHT = 1080;
    static Font FONT;
    private Context pi4j;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        pi4j = Pi4J.newAutoContext();

        Ads1115 ads1115 = new Ads1115(pi4j);
        JoystickAnalog joystick1 = new JoystickAnalog(ads1115, Ads1115.Channel.A0, Ads1115.Channel.A1);
        //JoystickAnalog joystick2 = new JoystickAnalog(ads1115, Ads1115.Channel.A2, Ads1115.Channel.A3);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        WIDTH = screenSize.width;
        HEIGHT = screenSize.height;
        setupStage(stage);

        //Creates the player and an array list for all the obstacles
        Player player1 = new Player(0, HEIGHT / 2.0, 3, WIDTH, HEIGHT, Spritesheets.Player, joystick1);
        // Player player2 = new Player(0, HEIGHT / 1.5, 3, WIDTH, HEIGHT, Spritesheets.Player, joystick2);
        List<Player> players = List.of(player1);

        List<Obstacle> obstacles = Collections.synchronizedList(new ArrayList<>());

        //Creates the area which we draw all the images on
        Canvas canvas = setupCanvas();

        //creates window and passes it the relevant objects (necessary for display)
        Scene scene = createScene(canvas);
        stage.setScene(scene);
        stage.show();

        //Initializes the controller and starts the game
        Controller controller = new Controller(players, obstacles, scene);

        //Starts the View and passes it the relevant things that are to be displayed
        View view = new View(canvas.getGraphicsContext2D(), players, obstacles);

        //starts the key listeners for the main scene.
        controller.createGameKeyListeners(scene); // for debugging
        controller.startGameLogic();

        view.startRendering();

        //Stops the game if the window is exited
        stage.setOnCloseRequest(event -> {pi4j.shutdown(); controller.stopGameLogic();});
    }

    private void setupStage(Stage stage) {
        stage.setResizable(false);
        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);
        stage.setTitle("IP12 Prototype");
    }

    private Canvas setupCanvas() {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
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
