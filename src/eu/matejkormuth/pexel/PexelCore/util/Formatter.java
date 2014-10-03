package eu.matejkormuth.pexel.PexelCore.util;

/**
 * Class that formats some things.
 */
public class Formatter {
    /**
     * Returns formatter time in format mm:ss from provided time left.
     * 
     * @param secondsLeft
     *            seconds to format
     * @return formatted time
     */
    public static String formatTimeLeft(final int secondsLeft) {
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
