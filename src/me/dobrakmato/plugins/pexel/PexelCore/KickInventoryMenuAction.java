package me.dobrakmato.plugins.pexel.PexelCore;

import org.bukkit.entity.Player;

/**
 * Menu action, that will kick player from server.
 * 
 * @author Mato Kormuth
 * 
 */
public class KickInventoryMenuAction implements InventoryMenuAction
{
	@Override
	public void load(final String string)
	{
	}
	
	@Override
	public String save()
	{
		return null;
	}
	
	@Override
	public void execute(final Player player)
	{
		player.kickPlayer("Goodbye!");
	}
}
