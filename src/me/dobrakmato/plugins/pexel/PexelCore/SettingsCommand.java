package me.dobrakmato.plugins.pexel.PexelCore;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Class used for settings.
 * 
 * @author Mato Kormuth
 * 
 */
public class SettingsCommand implements CommandExecutor
{
	@Override
	public boolean onCommand(final CommandSender sender, final Command command,
			final String paramString, final String[] args)
	{
		if (sender instanceof Player)
		{
			if (args.length == 2)
			{
				try
				{
					Setting setting = Setting.valueOf(args[0]);
					Boolean value = Boolean.parseBoolean(args[1]);
					
					StorageEngine.getProfile(((Player) sender).getUniqueId()).setSetting(
							setting, value);
					
				} catch (Exception ex)
				{
					sender.sendMessage(ChatFormat.error(ex.toString()));
				}
			}
			else
			{
				sender.sendMessage(ChatFormat.error("/settings <setting> <false/true>"));
				String avaiable = ChatColor.GOLD + "Avaiable settings: "
						+ ChatColor.YELLOW;
				for (Setting s : Setting.values())
				{
					avaiable += s.toString() + ", ";
				}
				sender.sendMessage(avaiable);
			}
		}
		return false;
	}
}
