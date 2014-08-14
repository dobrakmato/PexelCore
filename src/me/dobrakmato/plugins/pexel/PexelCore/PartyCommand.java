package me.dobrakmato.plugins.pexel.PexelCore;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Command party.
 * 
 * @author Mato Kormuth
 * 
 */
public class PartyCommand implements CommandExecutor
{
	@Override
	public boolean onCommand(final CommandSender sender, final Command command,
			final String arg2, final String[] args)
	{
		sender.sendMessage("Hey let's have a party all night long!");
		return true;
	}
}
