package eu.matejkormuth.pexel.PexelCore.bans;

import org.bukkit.entity.Player;

/**
 * Interface that specified ban.
 */
public interface Ban {
    /**
     * Returns whether is ban permanent or not.
     * 
     * @return true if ban is permanent, false otherwise.
     */
    public boolean isPermanent();
    
    /**
     * Returns author of ban.
     * 
     * @return author of ban.
     */
    public BanAuthor getAuthor();
    
    /**
     * Return banned player.
     * 
     * @return banned player.
     */
    public Player getPlayer();
    
    /**
     * Returns part of network, from which is player banned.
     * 
     * @return part of network.
     */
    public Bannable getBanned();
    
    /**
     * Returns reason of this ban.
     * 
     * @return reson of ban.
     */
    public String getReason();
    
    /**
     * Returns length of ban in miliseconds if is ban temporary, -1 if is ban permanent.
     * 
     * @return lenght in ms or -1.
     */
    public long getLength();
    
    /**
     * Returns creation time of ban in epoch time format or -1, if is ban permanent.
     * 
     * @return timestamp of time at creation.
     */
    public long getCreationTime();
    
    /**
     * Returns time, when ban expiries or -1, if is ban permanent.
     * 
     * @return epoch timestamp
     */
    public long getExpirationTime();
}
