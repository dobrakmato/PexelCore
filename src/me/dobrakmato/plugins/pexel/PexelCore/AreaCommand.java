package me.dobrakmato.plugins.pexel.PexelCore;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

/**
 * @author Mato Kormuth
 * 
 */
public class AreaCommand implements CommandExecutor
{
	private final WorldEditPlugin	we;
	
	public AreaCommand()
	{
		this.we = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin(
				"WorldEdit");
	}
	
	@Override
	public boolean onCommand(final CommandSender sender, final Command command,
			final String paramString, final String[] args)
	{
		if (command.getName().equalsIgnoreCase("area"))
		{
			if (sender instanceof Player)
			{
				if (sender.isOp())
				{
					this.processOpCommand((Player) sender, args);
				}
				else
				{
					this.processCommand((Player) sender, args);
				}
			}
			else
			{
				sender.sendMessage(ChatFormat.error("This command is only avaiable for players!"));
			}
			return true;
		}
		return false;
	}
	
	private void processCommand(final Player sender, final String[] args)
	{
		sender.sendMessage(ChatFormat.error("Permission denied!"));
	}
	
	private void processOpCommand(final Player sender, final String[] args)
	{
		if (args.length > 1)
		{
			//String areaName = args[0];
			String actionName = args[1].toLowerCase();
			
			if (actionName.equalsIgnoreCase("create"))
			{
				if (this.checkSelection(sender))
				{
					//Region region = new Region(this.we.getSelection(sender));
				}
			}
		}
		else
		{
			sender.sendMessage(ChatFormat.error("/area <areaName> <action> [param1] [params...]"));
		}
	}
	
	private boolean checkSelection(final Player sender)
	{
		if (this.we.getSelection(sender) != null)
			return true;
		else
		{
			sender.sendMessage(ChatFormat.error("Make a WorldEdit selection first!"));
			return false;
		}
	}
}
