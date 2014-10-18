package eu.matejkormuth.pexel.PexelCore.core;

import eu.matejkormuth.pexel.PexelCore.minigame.Minigame;

/**
 * Class that represents achievement.
 */
public class Achievement {
    private String name;
    private String displayName;
    private int    maxProgress;
    
    /**
     * Builds achievement for specified minigame with specified name and display name.
     * 
     * @param minigame
     *            minigame
     * @param name
     *            name of achievement (will be automatically prefixed with '{minigamename}.'.
     * @param displayName
     *            display name
     * @return achievement
     */
    public static Achievement minigame(final Minigame minigame, final String name,
            final String displayName, final int maxProgress) {
        Achievement ach = new Achievement();
        ach.name = minigame.getName() + "." + name;
        ach.displayName = displayName;
        ach.maxProgress = maxProgress;
        return ach;
    }
    
    /**
     * Builds global achievement that applies to whole network with specified name and display name.
     * 
     * @param name
     *            name (will be automatically prefixed with 'global.'.
     * @param displayName
     *            display name
     * @return achievement
     */
    public static Achievement global(final String name, final String displayName,
            final int maxProgress) {
        Achievement ach = new Achievement();
        ach.name = "global." + name;
        ach.displayName = displayName;
        ach.maxProgress = maxProgress;
        return ach;
    }
    
    /**
     * Returns the name of achievement.
     * 
     * @return the name
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Returns the max progress value of this achievement (<b>value, when achievement is awarded to player<b>).
     * 
     * @return the max progress value
     */
    public int getMaxProgress() {
        return this.maxProgress;
    }
    
    /**
     * Returns the display name of achievement.
     * 
     * @return the display name of achievement
     */
    public String getDisplayName() {
        return this.displayName;
    }
}
