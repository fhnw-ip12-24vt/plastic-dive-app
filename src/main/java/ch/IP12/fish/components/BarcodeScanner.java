package ch.IP12.fish.components;

import ch.IP12.fish.model.World;
import ch.IP12.fish.utils.Difficulty;
import ch.IP12.fish.utils.GamePhase;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class BarcodeScanner {
    private final World world;
    private final Scene scene;
    private String s = "\n";
    public BarcodeScanner(Scene scene, World world) {
        this.scene = scene;
        this.world = world;
    }

    public void startListening(){
        scene.setOnKeyPressed(event -> {
            if (s.replace("\n", "-").substring(s.length()-1).equals("-")){
                s = "";
            }
            if (event.getCode() == KeyCode.ENTER) {
                s += "\n";
                try {
                    world.setDifficulty(Difficulty.getDifficulty(Long.parseLong(s.replace("\n", ""))));
                    world.setGamePhase(GamePhase.StartingAnimation);
                    stopListening();
                } catch (RuntimeException ignore) {

                }
                return;
            }
            s += event.getCode().getChar();
        });
    }

    public void stopListening(){
        scene.setOnKeyPressed(event -> {});
    }

}
