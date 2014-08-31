package me.dobrakmato.plugins.pexel.PexelCore;

import me.dobrakmato.plugins.pexel.ColorWar.ColorWarArena;
import me.dobrakmato.plugins.pexel.ColorWar.ColorWarMinigame;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Mato Kormuth
 * 
 */
public class PCMDCommand implements CommandExecutor
{
	@Override
	public boolean onCommand(final CommandSender sender, final Command command,
			final String label, final String[] args)
	{
		if (sender instanceof Player)
		{
			Player psender = (Player) sender;
			if (args.length == 1)
			{
				String arg_command = args[0];
				if (arg_command.equalsIgnoreCase("cwtest"))
				{
					ColorWarArena arena = ((ColorWarMinigame) StorageEngine.getMinigame("colorwar")).trrtrtr();
					if (arena.canJoin())
					{
						sender.sendMessage(ChatColor.GREEN
								+ "Joining ColorWar...");
						arena.onPlayerJoin(psender);
					}
					else
					{
						sender.sendMessage(ChatColor.RED
								+ "Arena is in progress now!");
					}
				}
			}
		}
		return true;
	}
	
}
