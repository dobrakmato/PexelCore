package me.dobrakmato.plugins.pexel.PexelCore;

import org.bukkit.entity.Player;

/**
 * Class that repsresents action in PEXEL.
 * 
 * @author Mato Kormuth
 * 
 */
public interface Action
{
	/**
	 * Should load menu action from string.
	 * 
	 * @param string
	 *            string that was previously serialized by action class.
	 */
	public void load(String string);
	
	/**
	 * Should save menu action to string.
	 * 
	 * @return serialized class
	 */
	public String save();
	
	/**
	 * Called when action should be executed on the player.
	 * 
	 * @param player
	 *            executor
	 */
	public void execute(Player player);
}
