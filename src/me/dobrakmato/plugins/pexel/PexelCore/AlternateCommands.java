package me.dobrakmato.plugins.pexel.PexelCore;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
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
		String command = event.getMessage().toLowerCase().replace("/", "");
		Player sender = event.getPlayer();
		if (command.equalsIgnoreCase("getcock"))
		{
			sender.getInventory().addItem(Pexel.getMagicClock().getClock());
		}
		else if (command.equalsIgnoreCase("list_arena_aliases"))
		{
			for (String key : StorageEngine.getAliases().keySet())
			{
				sender.sendMessage(ChatColor.BLUE + key + ChatColor.WHITE
						+ " = " + ChatColor.GREEN
						+ StorageEngine.getByAlias(key).getName());
			}
		}
	}
}
