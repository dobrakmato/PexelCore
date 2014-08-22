package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.Collection;

import org.bukkit.Location;

/**
 * Interface for minigame.
 * 
 * @author Mato Kormuth
 * 
 */
public interface Minigame
{
	/**
	 * Returns display name of minigame.
	 * 
	 * @return
	 */
	public String getDisplayName();
	
	/**
	 * Returns code safe name of minigame.
	 * 
	 * @return
	 */
	public String getName();
	
	/**
	 * Returns arena by name.
	 * 
	 * @param name
	 *            arena's name
	 * @return
	 */
	public MinigameArena getArena(String name);
	
	/**
	 * Returns all arenas of this minigame.
	 * 
	 * @return
	 */
	public Collection<MinigameArena> getArenas();
	
	/**
	 * Returns the minigame lobby location.
	 * 
	 * @return
	 */
	public Location getLobbyLocation();
}
