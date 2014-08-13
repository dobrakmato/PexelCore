package me.dobrakmato.plugins.pexel.PexelCore;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Alternate method of handling commands.
 * 
 * @author Mato Kormuth
 * 
 */
public class AlternateCommands implements Listener
{
	@EventHandler
	private void onPrepocessCommand(final PlayerCommandPreprocessEvent event)
	{
		String command = event.getMessage().toLowerCase();
		if(command.contains("getcock"))
		{
			event.getPlayer().getInventory().addItem()
		}
	}
}
