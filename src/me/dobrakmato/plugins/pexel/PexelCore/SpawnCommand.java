package me.dobrakmato.plugins.pexel.PexelCore;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Mato Kormuth
 * 
 */
public class SpawnCommand implements CommandExecutor
{
	
	@Override
	public boolean onCommand(final CommandSender paramCommandSender,
			final Command paramCommand, final String paramString,
			final String[] paramArrayOfString)
	{
		if (paramCommand.getName().equalsIgnoreCase("spawn"))
		{
			((Player) paramCommandSender).teleport(Pexel.getHubLocation());
			return true;
		}
		return false;
	}
}
