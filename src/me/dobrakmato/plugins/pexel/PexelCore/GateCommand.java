// @formatter:off
/*
 * Pexel Project - Minecraft minigame server platform. 
 * Copyright (C) 2014 Matej Kormuth <http://www.matejkormuth.eu>
 * 
 * This file is part of Pexel.
 * 
 * Pexel is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * Pexel is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */
// @formatter:on
package me.dobrakmato.plugins.pexel.PexelCore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

/**
 * Class used for /gate commands.
 * 
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
				sender.sendMessage(ChatManager.error("This command is only avaiable for players!"));
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
					//String actionType = args[2];
					//String actionContent = args[3];
					
					//if (actionType.equalsIgnoreCase("command"))
					//{
					//	actionContent = "";
					//	for (int i = 3; i < args.length; i++)
					//		actionContent += args[i] + " ";
					//}
					
					if (this.checkSelection(sender))
					{
						if (StorageEngine.getGate(name) == null)
						{
							sender.sendMessage(ChatManager.error("Can't configure gate!"));
							/*
							 * StorageEngine.addGate(name, new TeleportGate( new Region(this.we.getSelection(sender)),
							 * actionType, actionContent)); sender.sendMessage(ChatFormat.success("Gate '" + name +
							 * "' has been created!"));
							 */
						}
						else
						{
							sender.sendMessage(ChatManager.error("Gate with name '"
									+ name + "' already exists!"));
						}
					}
				}
				else
				{
					sender.sendMessage(ChatManager.error("Wrong use! Type /gate to help!"));
				}
			}
			else if (action.equalsIgnoreCase("modify"))
			{
				if (args.length == 4)
				{
					//String actionType = args[2];
					//String actionContent = args[3];
					
					if (StorageEngine.getGate(name) != null)
					{
						sender.sendMessage(ChatManager.error("Can't configure gate by command in this build."));
						/*
						 * StorageEngine.getGate(name).setType(actionType);
						 * StorageEngine.getGate(name).setContent(actionContent);
						 * sender.sendMessage(ChatFormat.success("Gate '" + name + "' has been modified!"));
						 */
					}
					else
					{
						sender.sendMessage(ChatManager.error("Gate with name '"
								+ name + "' does not exists!"));
					}
				}
				else
				{
					sender.sendMessage(ChatManager.error("Wrong use! Type /gate to help!"));
				}
			}
			else if (action.equalsIgnoreCase("remove"))
			{
				if (StorageEngine.getGate(name) != null)
				{
					StorageEngine.removeGate(name);
					sender.sendMessage(ChatManager.success("Gate '" + name
							+ "' has been removed!"));
				}
				else
				{
					sender.sendMessage(ChatManager.error("Gate '" + name
							+ "' not found!"));
				}
			}
			else
			{
				sender.sendMessage(ChatManager.error("Invalid action!"));
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
		sender.sendMessage(ChatManager.error("Permission denied!"));
	}
	
	private boolean checkSelection(final Player sender)
	{
		if (this.we.getSelection(sender) != null)
			return true;
		else
		{
			sender.sendMessage(ChatManager.error("Make a WorldEdit selection first!"));
			return true;
		}
	}
}
