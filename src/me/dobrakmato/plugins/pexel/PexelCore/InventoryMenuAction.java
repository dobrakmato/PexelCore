package me.dobrakmato.plugins.pexel.PexelCore;

import org.bukkit.entity.Player;

/**
 * Class that repsresents action in inventory menu.
 * 
 * @author Mato Kormuth
 * 
 */
public interface InventoryMenuAction
{
	/**
	 * Should load menu action from string.
	 * 
	 * @param string
	 */
	public void load(String string);
	
	/**
	 * Should save menu action to string.
	 * 
	 * @return
	 */
	public String save();
	
	/**
	 * Called when action should be executed on the player.
	 * 
	 * @param player
	 */
	public void execute(Player player);
}
