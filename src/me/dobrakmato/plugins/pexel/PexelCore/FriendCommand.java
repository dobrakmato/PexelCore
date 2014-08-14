package me.dobrakmato.plugins.pexel.PexelCore;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Class used for handling 'friends' command.
 * 
 * @author Mato Kormuth
 * 
 */
public class FriendCommand implements CommandExecutor
{
	@Override
	public boolean onCommand(final CommandSender sender, final Command command,
			final String paramString, final String[] args)
	{
		if (command.getName().equalsIgnoreCase("friend"))
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
		if (args.length > 1)
		{
			String action = args[0];
			//String name = args[1];
			
			if (action.equalsIgnoreCase("add"))
			{
				
			}
			else if (action.equalsIgnoreCase("info"))
			{
				
			}
			else if (action.equalsIgnoreCase("remove"))
			{
				
			}
		}
	}
	
	private void processOpCommand(final Player sender, final String[] args)
	{
		//No op commands.
		this.processCommand(sender, args);
	}
}
