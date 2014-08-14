package me.dobrakmato.plugins.pexel.PexelCore;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.UUID;

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
public class ArenaCommand implements CommandExecutor
{
	private final WorldEditPlugin	we;
	
	public ArenaCommand()
	{
		this.we = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin(
				"WorldEdit");
	}
	
	@Override
	public boolean onCommand(final CommandSender sender, final Command command,
			final String paramString, final String[] args)
	{
		if (command.getName().equalsIgnoreCase("arena"))
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
	
	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	private void processOpCommand(final Player sender, final String[] args)
	{
		if (args.length > 2)
		{
			String actionName = args[0].toLowerCase();
			String arenaName = args[1].toLowerCase();
			
			if (actionName.equalsIgnoreCase("create"))
			{
				if (args.length == 4)
				{
					if (this.checkSelection(sender))
					{
						Region region = new Region(this.we.getSelection(sender));
						String arenaType = args[2];
						String minigameName = args[3];
						//Check if minigame exists.
						if (StorageEngine.getMinigame(minigameName) == null)
						{
							sender.sendMessage(ChatFormat.error("Minigame not defined: "
									+ minigameName));
							return;
						}
						
						String className = null;
						Class classType = null;
						//Try to get by alias
						if ((classType = StorageEngine.getByAlias(arenaType)) == null)
							className = arenaType;
						//Build object.
						try
						{
							Class c = null;
							if (className != null)
								c = Class.forName(className);
							else
								c = classType;
							//Create instance
							MinigameArena newArena = (MinigameArena) c.getDeclaredConstructor(
									Minigame.class, String.class, Region.class,
									int.class).newInstance(
									StorageEngine.getMinigame(minigameName),
									arenaName, region, 16);
							sender.sendMessage(ChatFormat.success("Created new arena with 16 slots."));
							//Register arena to plugin.
							Pexel.getMatchmaking().registerArena(newArena);
							StorageEngine.addArena(newArena);
						} catch (Exception e)
						{
							e.printStackTrace();
							sender.sendMessage(ChatFormat.error("Create command failed: "
									+ e.toString()));
						}
					}
				}
				else
				{
					sender.sendMessage(ChatFormat.error("/arena create <name> <arenaClass> <minigameClass>"));
				}
			}
			else if (actionName.equalsIgnoreCase("edit"))
			{
				String editAction = args[2];
				if (editAction.equalsIgnoreCase("gflag"))
				{
					if (args.length == 4)
					{
						String flagName = args[3];
						try
						{
							Boolean flagValue = Boolean.parseBoolean(args[4]);
							if (StorageEngine.getArena(arenaName) != null)
							{
								StorageEngine.getArena(arenaName).setGlobalFlag(
										AreaFlag.valueOf(flagName), flagValue);
								sender.sendMessage(ChatFormat.success("Flag '"
										+ flagName + "' set to '" + flagValue
										+ "' in arena " + arenaName));
							}
							else
							{
								throw new RuntimeException("Arena not found: "
										+ arenaName);
							}
						} catch (Exception ex)
						{
							sender.sendMessage(ChatFormat.error("Edit command failed: "
									+ ex.toString()));
						}
					}
					else
					{
						sender.sendMessage(ChatFormat.error("/arena edit <name> gflag <flag> <value>"));
					}
				}
				else if (editAction.equalsIgnoreCase("pflag"))
				{
					if (args.length == 5)
					{
						String flagName = args[3];
						String playerName = args[4];
						try
						{
							Boolean flagValue = Boolean.parseBoolean(args[5]);
							UUID uuid = null;
							if (Bukkit.getPlayerExact(playerName).isOnline())
							{
								uuid = Bukkit.getPlayerExact(playerName).getUniqueId();
							}
							else
							{
								throw new RuntimeException("Play offline: "
										+ playerName);
							}
							
							if (StorageEngine.getArena(arenaName) != null)
							{
								StorageEngine.getArena(arenaName).setPlayerFlag(
										AreaFlag.valueOf(flagName), flagValue,
										uuid);
								sender.sendMessage(ChatFormat.success("Flag '"
										+ flagName + "' set to '" + flagValue
										+ "' in arena " + arenaName));
							}
							else
							{
								throw new RuntimeException("Arena not found: "
										+ arenaName);
							}
						} catch (Exception ex)
						{
							sender.sendMessage(ChatFormat.error("Edit command failed: "
									+ ex.toString()));
						}
					}
					else
					{
						sender.sendMessage(ChatFormat.error("/arena edit <name> pflag <flag> <value>"));
					}
				}
				else if (editAction.equalsIgnoreCase("slots"))
				{
					if (args.length == 4)
					{
						Integer slotCount = Integer.parseInt(args[3]);
						try
						{
							if (StorageEngine.getArena(arenaName) != null)
							{
								StorageEngine.getArena(arenaName).setSlots(
										slotCount);
								sender.sendMessage(ChatFormat.success("Slots set to '"
										+ slotCount + "' in arena " + arenaName));
							}
							else
							{
								throw new RuntimeException("Arena not found: "
										+ arenaName);
							}
						} catch (Exception ex)
						{
							sender.sendMessage(ChatFormat.error("Slots command failed: "
									+ ex.toString()));
						}
					}
					else
					{
						sender.sendMessage(ChatFormat.error("/arena edit <name> slots <count>"));
					}
				}
				else if (editAction.equalsIgnoreCase("option"))
				{
					if (args.length == 5)
					{
						String optionName = args[3];
						String optionValue = args[4];
						
						try
						{
							if (StorageEngine.getArena(arenaName) != null)
							{
								MinigameArena arena = StorageEngine.getArena(arenaName);
								//Try to find option type.
								
								//Convert optionValue to right type.
								
								//Set value.
							}
							else
							{
								throw new RuntimeException("Arena not found: "
										+ arenaName);
							}
						} catch (Exception ex)
						{
							sender.sendMessage(ChatFormat.error("Option command failed: "
									+ ex.toString()));
						}
					}
					else
					{
						sender.sendMessage(ChatFormat.error("/arena edit <name> option <optionName> <optionValue>"));
					}
				}
				else if (editAction.equalsIgnoreCase("options"))
				{
					try
					{
						if (StorageEngine.getArena(arenaName) != null)
						{
							MinigameArena arena = StorageEngine.getArena(arenaName);
							
							for (Field f : arena.getClass().getDeclaredFields())
							{
								if (Modifier.isFinal(f.getModifiers()))
									sender.sendMessage(ChatColor.RED
											+ "[READONLY]" + ChatColor.YELLOW
											+ f.getName() + ChatColor.WHITE
											+ " = " + ChatColor.GREEN
											+ f.get(arena).toString());
								else
									sender.sendMessage(ChatColor.GREEN
											+ f.getName() + ChatColor.WHITE
											+ " = " + ChatColor.GREEN
											+ f.get(arena).toString());
							}
						}
						else
						{
							throw new RuntimeException("Arena not found: "
									+ arenaName);
						}
					} catch (Exception ex)
					{
						sender.sendMessage(ChatFormat.error("Options command failed: "
								+ ex.toString()));
					}
				}
				else if (editAction.equalsIgnoreCase("state"))
				{
					if (args.length == 4)
					{
						String state = args[3];
						GameState stateToSet;
						try
						{
							if (state.equalsIgnoreCase("open"))
							{
								stateToSet = GameState.WAITING_EMPTY;
							}
							else if (state.equalsIgnoreCase("closed"))
							{
								stateToSet = GameState.DISABLED;
							}
							else
							{
								stateToSet = GameState.valueOf(state);
							}
							
							if (StorageEngine.getArena(arenaName) != null)
							{
								StorageEngine.getArena(arenaName).state = stateToSet;
								sender.sendMessage(ChatFormat.success("State set to '"
										+ stateToSet.toString()
										+ "' in arena "
										+ arenaName));
							}
							else
							{
								throw new RuntimeException("Arena not found: "
										+ arenaName);
							}
						} catch (Exception ex)
						{
							sender.sendMessage(ChatFormat.error("State command failed: "
									+ ex.toString()));
						}
					}
					else
					{
						sender.sendMessage(ChatFormat.error("/arena edit <name> state (open/closed)/(WAITING_EMPTY)"));
					}
				}
				else
				{
					sender.sendMessage(ChatFormat.error("Unknown command!"));
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
