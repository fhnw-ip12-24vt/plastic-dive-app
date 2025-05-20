package ch.IP12.fish.scoreBoard;

import java.util.Comparator;

public class ScoreboardComparer implements Comparator {
    /**
     * Compare two scores from provided ScoreboardEntity objects.
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return 0 if they are equal, positive number if the first is larger and negative if the second is smaller.
     */
    @Override
    public int compare(Object o1, Object o2) {
        if (o1 instanceof ScoreboardEnitity && o2 instanceof ScoreboardEnitity) {
            return (int)(((ScoreboardEnitity)o2).score - ((ScoreboardEnitity)o1).score);
        }
        return -1;
    }
}
