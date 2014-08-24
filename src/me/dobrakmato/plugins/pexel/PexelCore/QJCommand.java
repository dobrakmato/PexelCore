package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Class that is here for /qj command.
 * 
 * @author Mato Kormuth
 * 
 */
public class QJCommand implements CommandExecutor
{
	@Override
	public boolean onCommand(final CommandSender sender, final Command command,
			final String paramString, final String[] args)
	{
		if (command.getName().equalsIgnoreCase("qj"))
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
				sender.sendMessage(ChatManager.error("This command is only avaiable for players!"));
			}
			return true;
		}
		sender.sendMessage(ChatManager.error("Wrong use!"));
		return true;
	}
	
	private void processCommand(final Player sender, final String[] args)
	{
		if (args.length == 0)
		{
			Pexel.getMatchmaking().registerRequest(
					new MatchmakingRequest(Arrays.asList(sender), null, null));
			sender.sendMessage(ChatManager.success("Successfully joined matchmaking!"));
		}
		else if (args.length == 1)
		{
			if (StorageEngine.getMinigame(args[0]) != null)
			{
				Pexel.getMatchmaking().registerRequest(
						new MatchmakingRequest(Arrays.asList(sender),
								StorageEngine.getMinigame(args[0]), null));
				sender.sendMessage(ChatManager.success("Successfully joined matchmaking!"));
			}
			else
			{
				sender.sendMessage(ChatManager.error("QJ: That minigame does not exists!"));
			}
		}
		else if (args.length == 2)
		{
			sender.sendMessage(ChatManager.error("QJ: Not avaiable at this time!"));
		}
		else
		{
			sender.sendMessage(ChatManager.error("/qj [minigame] [map]"));
		}
	}
	
	private void processOpCommand(final Player sender, final String[] args)
	{
		this.processCommand(sender, args);
	}
	
}
