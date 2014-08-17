package me.dobrakmato.plugins.pexel.PexelCore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

/**
 * @author Mato Kormuth
 * 
 */
public class GateCommand implements CommandExecutor
{
	
	private final WorldEditPlugin	we;
	
	public GateCommand()
	{
		this.we = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin(
				"WorldEdit");
	}
	
	@Override
	public boolean onCommand(final CommandSender sender, final Command command,
			final String paramString, final String[] args)
	{
		if (command.getName().equalsIgnoreCase("gate"))
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
		//sender.sendMessage(ChatFormat.error("Wrong use!"));
		return true;
	}
	
	private void processOpCommand(final Player sender, final String[] args)
	{
		if (args.length >= 2)
		{
			String action = args[0];
			String name = args[1];
			
			if (action.equalsIgnoreCase("create"))
			{
				if (args.length == 4)
				{
					String actionType = args[2];
					String actionContent = args[3];
					
					if (actionType.equalsIgnoreCase("command"))
					{
						actionContent = "";
						for (int i = 3; i < args.length; i++)
							actionContent += args[i] + " ";
					}
					
					if (this.checkSelection(sender))
					{
						if (StorageEngine.getGate(name) == null)
						{
							StorageEngine.addGate(name, new TeleportGate(
									new Region(this.we.getSelection(sender)),
									actionType, actionContent));
							sender.sendMessage(ChatFormat.success("Gate '"
									+ name + "' has been created!"));
						}
						else
						{
							sender.sendMessage(ChatFormat.error("Gate with name '"
									+ name + "' already exists!"));
						}
					}
				}
				else
				{
					sender.sendMessage(ChatFormat.error("Wrong use! Type /gate to help!"));
				}
			}
			else if (action.equalsIgnoreCase("modify"))
			{
				if (args.length == 4)
				{
					String actionType = args[2];
					String actionContent = args[3];
					
					if (StorageEngine.getGate(name) != null)
					{
						StorageEngine.getGate(name).setType(actionType);
						StorageEngine.getGate(name).setContent(actionContent);
						sender.sendMessage(ChatFormat.success("Gate '" + name
								+ "' has been modified!"));
					}
					else
					{
						sender.sendMessage(ChatFormat.error("Gate with name '"
								+ name + "' does not exists!"));
					}
				}
				else
				{
					sender.sendMessage(ChatFormat.error("Wrong use! Type /gate to help!"));
				}
			}
			else if (action.equalsIgnoreCase("remove"))
			{
				if (StorageEngine.getGate(name) != null)
				{
					StorageEngine.removeGate(name);
					sender.sendMessage(ChatFormat.success("Gate '" + name
							+ "' has been removed!"));
				}
				else
				{
					sender.sendMessage(ChatFormat.error("Gate '" + name
							+ "' not found!"));
				}
			}
			else
			{
				sender.sendMessage(ChatFormat.error("Invalid action!"));
			}
		}
		else
		{
			sender.sendMessage(ChatColor.RED
					+ "/gate create <name> <actionType> <actionContent>");
			sender.sendMessage(ChatColor.RED
					+ "/gate modify <name> <actionType> <actionContent>");
			sender.sendMessage(ChatColor.RED + "/gate remove <name>");
			sender.sendMessage(ChatColor.LIGHT_PURPLE
					+ "--------------------------------------");
			sender.sendMessage(ChatColor.GREEN + "====== ACTION TYPES ======");
			sender.sendMessage(ChatColor.GOLD + "Type: commmand");
			sender.sendMessage(ChatColor.YELLOW
					+ "Executes command as player. You can use %player% in content - it will be replaced with his name.");
			sender.sendMessage(ChatColor.BLUE
					+ "Example: /gate create <name> command \"server staving\"");
			sender.sendMessage(ChatColor.GOLD + "Type: teleport");
			sender.sendMessage(ChatColor.YELLOW
					+ "Teleports player to specified location. Use syntax 'x,y,z,yaw,pitch,world'");
			sender.sendMessage(ChatColor.BLUE
					+ "Example: /gate create <name> teleport 15,85,41,0,90,world");
		}
	}
	
	private void processCommand(final Player sender, final String[] args)
	{
		sender.sendMessage(ChatFormat.error("Permission denied!"));
	}
	
	private boolean checkSelection(final Player sender)
	{
		if (this.we.getSelection(sender) != null)
			return true;
		else
		{
			sender.sendMessage(ChatFormat.error("Make a WorldEdit selection first!"));
			return true;
		}
	}
}
