package ch.IP12.fish.difficultySelector;

import ch.IP12.fish.Controller;
import ch.IP12.fish.utils.GamePhase;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class BarcodeScanner {
    private final Scene scene;
    private String DifficultyString = "\n";
    private long difficulty = 0;

    private final static long EASY_DIFFICULTY = 7624841656535L;
    private final static long MEDIUM_DIFFICULTY = 6211734858498L;
    private final static long HARD_DIFFICULTY = 7751064387955L;

    public BarcodeScanner(Scene scene) {
        this.scene = scene;
    }

    public void startListening(){
        scene.setOnKeyPressed(event -> {
            if (DifficultyString.replace("\n", "-").substring(DifficultyString.length()-1).equals("-")){
                DifficultyString = "";
            }
            if (event.getCode() == KeyCode.ENTER) {
                DifficultyString += "\n";
                if (isValid()) {
                    System.out.println("asdf");
                    difficulty = Long.parseLong(DifficultyString.replace("\n", ""));
                    Controller.DIFFICULTY = getDifficulty();
                    Controller.GAMEPHASE = GamePhase.StartingAnimation;
                    stopListening();
                }
                return;
            }
            DifficultyString += event.getCode().getChar();
        });
    }

    public void stopListening(){
        scene.setOnKeyReleased(event -> {});
    }

    public boolean isValid(){
        long difficulty;

        try{
            difficulty = Long.parseLong(DifficultyString.replace("\n", ""));
        } catch (NumberFormatException e){
            return false;
        }

        return difficulty == EASY_DIFFICULTY || difficulty == MEDIUM_DIFFICULTY || difficulty == HARD_DIFFICULTY;
    }

    public String getDifficulty() {
        if (difficulty == EASY_DIFFICULTY) {
            return "Easy";
        } else if (difficulty == MEDIUM_DIFFICULTY) {
            return "Medium";
        } else if (difficulty == HARD_DIFFICULTY){
            return "Hard";
        }
        return "Invalid code";
    }
}
