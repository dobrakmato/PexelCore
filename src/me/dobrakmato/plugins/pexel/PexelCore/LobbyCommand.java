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
public class LobbyCommand implements CommandExecutor
{
	private final WorldEditPlugin	we;
	
	public LobbyCommand()
	{
		this.we = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin(
				"WorldEdit");
	}
	
	@Override
	public boolean onCommand(final CommandSender sender, final Command command,
			final String label, final String[] args)
	{
		if (command.getName().equalsIgnoreCase("lobbyarena"))
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
		sender.sendMessage(ChatFormat.error("Wrong use!"));
		return true;
	}
	
	private void processCommand(final Player sender, final String[] args)
	{
		sender.sendMessage(ChatFormat.error("Permission denied!"));
	}
	
	private void processOpCommand(final Player sender, final String[] args)
	{
		if (args.length > 2)
		{
			String actionName = args[0];
			String lobbyName = args[1];
			
			if (actionName.equalsIgnoreCase("create"))
			{
				if (this.checkSelection(sender))
				{
					Region region = new Region(this.we.getSelection(sender));
					StorageEngine.addLobby(new Lobby(lobbyName, region));
					sender.sendMessage(ChatFormat.success("Lobby '" + lobbyName
							+ "' has been created!"));
				}
			}
			else if (actionName.equalsIgnoreCase("setspawn"))
			{
				StorageEngine.getLobby(lobbyName).setSpawn(sender.getLocation());
				sender.sendMessage(ChatFormat.success("Spawn of lobby '"
						+ lobbyName + "' has been set to your position."));
			}
			else
			{
				sender.sendMessage(ChatFormat.error("Invalid action!"));
			}
		}
		else
		{
			sender.sendMessage(ChatFormat.error("/lobby create <name>"));
			sender.sendMessage(ChatFormat.error("/lobby setspawn <name>"));
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
