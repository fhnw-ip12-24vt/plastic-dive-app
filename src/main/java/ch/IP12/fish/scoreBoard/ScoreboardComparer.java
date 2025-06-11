package ch.IP12.fish.scoreBoard;

import java.util.Comparator;

public class ScoreboardComparer implements Comparator<ScoreboardEnitity> {

    /**
     * Compare two scores from provided ScoreboardEntity objects.
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return 0 if they are equal, positive number if the first is larger and negative if the second is smaller.
     */
    @Override
    public int compare(ScoreboardEnitity o1, ScoreboardEnitity o2) {
        return (int)(o2.score() - o1.score());
    }
}
