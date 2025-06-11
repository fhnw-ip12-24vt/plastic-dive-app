package ch.IP12.fish.utils;

/**
 * Phases of a game that can be happening (One at a time):
 * <ul>
 *     <li>Start</li>
 *     <li>StartingAnimation</li>
 *     <li>Running</li>
 *     <li>End</li>
 *     <li>Highscore</li>
 * </ul>
 */
public enum GamePhase {
    Start,
    StartingAnimation,
    Running,
    End,
    HighScore;

    /**
     * @return Next phase that is listed.
     */
    public GamePhase next() {
        int nextOrdinal = (this.ordinal() + 1) % GamePhase.values().length;
        return GamePhase.values()[nextOrdinal];
    }
}
