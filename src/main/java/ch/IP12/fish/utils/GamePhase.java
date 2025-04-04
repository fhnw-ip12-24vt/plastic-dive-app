package ch.IP12.fish.utils;

import java.util.Arrays;

public enum GamePhase {
    Start,
    StartingAnimation,
    Running,
    BeforeEndAnimation,
    End,
    HighScore;


    public GamePhase next() {
        int nextOrdinal = (this.ordinal() + 1) % GamePhase.values().length;
        return GamePhase.values()[nextOrdinal];
    }
}
