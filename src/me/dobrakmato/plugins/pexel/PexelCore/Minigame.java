package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.Collection;
import java.util.List;

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
	 * @return display name
	 */
	public String getDisplayName();
	
	/**
	 * Returns code safe name of minigame.
	 * 
	 * @return code safe name
	 */
	public String getName();
	
	/**
	 * Returns minigame category.
	 * 
	 * @return minigame categorry
	 */
	public MinigameCategory getCategory();
	
	/**
	 * Returns minigame types.
	 * 
	 * @return all minigame types.
	 */
	public List<MinigameType> getTypes();
	
	/**
	 * Returns arena by name.
	 * 
	 * @param name
	 *            arena's name
	 * @return
	 */
	@Deprecated
	public MinigameArena getArena(String name);
	
	/**
	 * Returns all arenas of this minigame.
	 * 
	 * @return
	 */
	@Deprecated
	public Collection<MinigameArena> getArenas();
	
	/**
	 * Returns the minigame lobby location.
	 * 
	 * @return location of minigame's lobby
	 */
	public Location getLobbyLocation();
}
