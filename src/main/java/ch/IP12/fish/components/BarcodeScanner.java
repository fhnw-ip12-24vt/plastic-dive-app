package ch.IP12.fish.components;

import ch.IP12.fish.Controller;
import ch.IP12.fish.utils.Difficulty;
import ch.IP12.fish.utils.GamePhase;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.io.IOException;

public class BarcodeScanner {
    private final Scene scene;
    private Difficulty difficulty;
    private String s = "\n";
    public BarcodeScanner(Scene scene) {
        this.scene = scene;
    }

    public void startListening(){
        scene.setOnKeyPressed(event -> {
            if (s.replace("\n", "-").substring(s.length()-1).equals("-")){
                s = "";
            }
            if (event.getCode() == KeyCode.ENTER) {
                s += "\n";
                try {
                    System.out.println("asdf");
                    Controller.DIFFICULTY = Difficulty.getDifficulty(Long.parseLong(s.replace("\n", "")));
                    Controller.GAMEPHASE = GamePhase.StartingAnimation;
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
