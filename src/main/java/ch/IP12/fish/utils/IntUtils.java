package ch.IP12.fish.utils;

public class IntUtils {
    /**
     * Checks if the provided number is in the specified range.
     * @param value Number that is to be checked.
     * @param min Highest point in range.
     * @param max Highest number in range.
     * @return Boolean value if the number is in the range.
     */
    public static boolean isInRange(double value, double min, double max) {
        return value >= min && value <= max;
    }

    /**
     *
     * @param valueMin Lowest number in the range that is checked.
     * @param valueMax Highest number in the range that is checked.
     * @param min Highest point in base range.
     * @param max Highest number in base range.
     * @return Boolean value if the ranges overlap.
     */
    public static boolean isRangeInRange(double valueMin, double valueMax, double min, double max) {
        for (double i = valueMin; i <= valueMax; i++) {
            if (isInRange(i, min, max)) {
                return true;
            }
        }
        return false;
    }
}
