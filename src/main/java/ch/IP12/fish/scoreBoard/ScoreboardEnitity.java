package ch.IP12.fish.scoreBoard;

public class ScoreboardEnitity {
    final double score;
    final String name;

    /**
     * Creates a scoreboard entity.
     * @param score The score the entity will hold.
     * @param name The name that is associated with the entity.
     */
    public ScoreboardEnitity(double score, String name) {
        this.score = score;
        this.name = name;
    }

    public double getScore() {
        return score;
    }

    public String getName() {
        return name;
    }
}
