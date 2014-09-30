package eu.matejkormuth.pexel.PexelCore.bans;

/**
 * Represents part of network, from which can be player banned.
 */
public interface Bannable {
    /**
     * Name of the bannable part.
     * 
     * @return name of part.
     */
    public String getBannableName();
    
    /**
     * Returns ID of bannable part.
     * 
     * @return id of part.
     */
    public String getBannableID();
}
