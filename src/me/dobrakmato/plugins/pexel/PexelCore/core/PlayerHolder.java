package me.dobrakmato.plugins.pexel.PexelCore.core;

import java.util.Collection;

import org.bukkit.entity.Player;

/**
 * Specifies class that hold collection of players.
 * 
 * @author Mato Kormuth
 * 
 */
public interface PlayerHolder {
    /**
     * Returns collection of players in this object.
     * 
     * @return list of players.
     */
    public Collection<Player> getPlayers();
    
    /**
     * Returns number of players in this object.
     * 
     * @return player count
     */
    public int getPlayerCount();
    
    /**
     * Returns whether object contains specified player.
     * 
     * @param player
     *            specified player to check
     * @return whether the object contains player or not
     */
    public boolean contains(Player player);
}
