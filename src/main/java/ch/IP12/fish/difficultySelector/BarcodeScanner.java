package ch.IP12.fish.difficultySelector;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class BarcodeScanner {
    private final Scene scene;
    private String DifficultyString = "\n";
    private long difficulty = 0;

    private final static long EASY_DIFFICULTY = 5181539527921L;
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
                if (isValid()) difficulty = Long.parseLong(DifficultyString.replace("\n", ""));
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
        if (difficulty == 0) {
            return "Invalid code";
        }

        if (difficulty == EASY_DIFFICULTY) {
            return "Easy";
        } else if (difficulty == MEDIUM_DIFFICULTY) {
            return "Medium";
        } else {
            return "Hard";
        }
    }
}
