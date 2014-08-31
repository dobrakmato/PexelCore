package me.dobrakmato.plugins.pexel.PexelCore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.dobrakmato.plugins.pexel.ColorWar.ColorWarMinigame;
import me.dobrakmato.plugins.pexel.ZabiPitkesa.ZabiPitkesaMinigame;
import net.minecraft.util.org.apache.commons.io.IOUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Alternate method of handling commands.
 * 
 * @author Mato Kormuth
 * 
 */
public class AlternativeCommands implements Listener
{
	InventoryMenu	particleEffectMenu;
	InventoryMenu	particleTypesMenu;
	InventoryMenu	particleAmountMenu;
	InventoryMenu	particleAnimationMenu;
	
	public AlternativeCommands()
	{
		Bukkit.getPluginManager().registerEvents(this, Pexel.getCore());
		
		List<InventoryMenuItem> particleEffectMenuItems = new ArrayList<InventoryMenuItem>();
		List<InventoryMenuItem> particleTypesMenuItems = new ArrayList<InventoryMenuItem>();
		List<InventoryMenuItem> particleAmountMenuItems = new ArrayList<InventoryMenuItem>();
		List<InventoryMenuItem> particleAnimationMenuItems = new ArrayList<InventoryMenuItem>();
		
		ParticleEffect2[] values = ParticleEffect2.values();
		for (int i = 0; i < values.length; i++)
		{
			final ParticleEffect2 effect = values[i];
			particleTypesMenuItems.add(new InventoryMenuItem(
					ItemUtils.namedItemStack(Material.NETHER_STAR,
							effect.toString(), null), new JavaArbitraryAction(
							new ParametrizedRunnable() {
								@Override
								public void run(final Object... args)
								{
									StorageEngine.getProfile(
											(((Player) args[0]).getUniqueId())).setParticleType(
											effect);
								}
							}), i, true));
		}
		
		this.particleTypesMenu = new InventoryMenu(54, "Particle type",
				particleTypesMenuItems);
		
		particleAmountMenuItems.add(new InventoryMenuItem(
				ItemUtils.namedItemStack(Material.BOOK, "Few particles", null),
				new JavaArbitraryAction(new ParametrizedRunnable() {
					@Override
					public void run(final Object... args)
					{
						// TODO Auto-generated method stub
						
					}
				}), 0, true));
		
		particleAmountMenuItems.add(new InventoryMenuItem(
				ItemUtils.namedItemStack(Material.BOOK, "The right amount",
						null), new JavaArbitraryAction(
						new ParametrizedRunnable() {
							@Override
							public void run(final Object... args)
							{
								// TODO Auto-generated method stub
								
							}
						}), 1, true));
		
		particleAmountMenuItems.add(new InventoryMenuItem(
				ItemUtils.namedItemStack(Material.BOOK, "Many particles", null),
				new JavaArbitraryAction(new ParametrizedRunnable() {
					@Override
					public void run(final Object... args)
					{
						// TODO Auto-generated method stub
						
					}
				}), 2, true));
		
		this.particleAmountMenu = new InventoryMenu(InventoryType.CHEST,
				"Particle amount", particleAmountMenuItems);
		
		this.particleAnimationMenu = new InventoryMenu(InventoryType.CHEST,
				"Particle animation", particleAnimationMenuItems);
		
		particleEffectMenuItems.add(new InventoryMenuItem(
				ItemUtils.namedItemStack(Material.NETHER_STAR,
						"Particle types", null), new OpenInventoryMenuAction(
						this.particleTypesMenu), 0, false));
		particleEffectMenuItems.add(new InventoryMenuItem(
				ItemUtils.namedItemStack(Material.BOOK, "Particle amount", null),
				new OpenInventoryMenuAction(this.particleAmountMenu), 1, false));
		particleEffectMenuItems.add(new InventoryMenuItem(
				ItemUtils.namedItemStack(Material.FIRE, "Particle animation",
						null), new OpenInventoryMenuAction(
						this.particleAnimationMenu), 2, false));
		
		this.particleEffectMenu = new InventoryMenu(InventoryType.CHEST,
				"Particle effects", particleEffectMenuItems);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	private void onPrepocessCommand(final PlayerCommandPreprocessEvent event)
	{
		List<String> args = new ArrayList<String>();
		String[] d = event.getMessage().split(" ");
		for (int i = 1; i < d.length; i++)
		{
			args.add(d[i]);
		}
		Player sender = event.getPlayer();
		String command = d[0];
		
		Log.info(sender.getName() + " issued command " + event.getMessage()
				+ " at " + event.getPlayer().getLocation().getX() + ","
				+ event.getPlayer().getLocation().getY() + ","
				+ event.getPlayer().getLocation().getZ() + ","
				+ event.getPlayer().getLocation().getWorld().getName());
		
		if (command.contains("/getcock"))
		{
			sender.getInventory().addItem(Pexel.getMagicClock().getClock());
		}
		else if (command.equalsIgnoreCase("/leave")
				|| command.equalsIgnoreCase("/lobby"))
		{
			for (MinigameArena arena : StorageEngine.getArenas().values())
				if (arena.containsPlayer(event.getPlayer()))
				{
					arena.onPlayerLeft(event.getPlayer());
					event.getPlayer().teleport(Pexel.getLobby());
				}
		}
		else if (command.equalsIgnoreCase("list_arena_aliases"))
		{
			for (String key : StorageEngine.getAliases().keySet())
			{
				sender.sendMessage(ChatColor.BLUE + key + ChatColor.WHITE
						+ " = " + ChatColor.GREEN
						+ StorageEngine.getByAlias(key).getName());
			}
		}
		else if (command.contains("/version") || command.contains("/pcversion"))
		{
			try
			{
				String version = IOUtils.toString(this.getClass().getResourceAsStream(
						"versionFile.txt"));
				sender.sendMessage(ChatColor.DARK_RED
						+ "This server is running Pexel-Core version "
						+ version);
			} catch (IOException e)
			{
				e.printStackTrace();
				sender.sendMessage(ChatColor.RED
						+ "Error while trying to get version! Check your build!");
			}
		}
		else if (command.contains("/cwtest"))
		{
			sender.sendMessage(ChatColor.GREEN
					+ "((ColorWarMinigame) StorageEngine.getMinigame(\"colorwar\")).trrtrtr().onPlayerJoin(event.getPlayer());");
			((ColorWarMinigame) StorageEngine.getMinigame("colorwar")).trrtrtr().onPlayerJoin(
					event.getPlayer());
		}
		else if (command.contains("/zptest"))
		{
			sender.sendMessage(ChatColor.GREEN
					+ "((ZabiPitkesaMinigame) StorageEngine.getMinigame(\"zabipitkesa\")).trrtrtr().onPlayerJoin(event.getPlayer());");
			((ZabiPitkesaMinigame) StorageEngine.getMinigame("zabipitkesa")).trrtrtr().onPlayerJoin(
					event.getPlayer());
		}
		else if (command.contains("/particles"))
		{
			this.particleEffectMenu.showTo(sender);
		}
		else if (command.contains("/grassgen"))
		{
			int i = 3;
			boolean remove = false;
			boolean flowers = false;
			boolean longgrass = false;
			
			World w = sender.getWorld();
			
			Location pLoc = sender.getLocation();
			
			sender.sendMessage(ChatColor.GREEN
					+ "/grassgen <radius> <flowers?> <longgrass?>");
			sender.sendMessage(ChatColor.YELLOW
					+ "Specify negative radius for remove, positive for generation.");
			
			if (args.size() > 0)
			{
				i = Integer.parseInt(args.get(0));
				if (i < 0)
				{
					remove = true;
					i = -i;
				}
			}
			
			if (args.size() == 3)
			{
				flowers = Boolean.parseBoolean(args.get(1));
				longgrass = Boolean.parseBoolean(args.get(2));
			}
			
			if (!remove)
			{
				sender.sendMessage(ChatColor.YELLOW + "Generating..");
				for (int x = -i; x <= i; x++)
				{
					for (int z = -i; z <= i; z++)
					{
						double cX = pLoc.getX() + (x);
						double cZ = pLoc.getZ() + (z);
						double cY = sender.getWorld().getHighestBlockYAt(
								(int) cX, (int) cZ);
						
						Block b = w.getBlockAt((int) cX, (int) cY, (int) cZ);
						
						//Generate grass and flowers.
						switch (Pexel.getRandom().nextInt(6))
						{
							case 0:
								//Air
								break;
							case 1:
								b.setType(Material.LONG_GRASS);
								b.setData((byte) 1);
								break;
							case 2:
								if (longgrass)
								{
									if (Pexel.getRandom().nextBoolean())
									{
										b.setType(Material.DOUBLE_PLANT);
										b.setData((byte) 2);
										b.getRelative(BlockFace.UP).setType(
												Material.DOUBLE_PLANT);
										b.getRelative(BlockFace.UP).setData(
												(byte) 8);
									}
									else
									{
										b.setType(Material.DOUBLE_PLANT);
										b.setData((byte) 3);
										b.getRelative(BlockFace.UP).setType(
												Material.DOUBLE_PLANT);
										b.getRelative(BlockFace.UP).setData(
												(byte) 8);
									}
								}
								else
								{
									b.setType(Material.LONG_GRASS);
									b.setData((byte) 1);
								}
								break;
							case 3:
								b.setType(Material.LONG_GRASS);
								b.setData((byte) 1);
								break;
							case 4:
								b.setType(Material.LONG_GRASS);
								b.setData((byte) 1);
								break;
							case 5:
								if (flowers)
								{
									switch (Pexel.getRandom().nextInt(9))
									{
										case 0:
											b.setType(Material.RED_ROSE);
											break;
										case 1:
											b.setType(Material.YELLOW_FLOWER);
											break;
										case 2:
											b.setType(Material.RED_ROSE);
											b.setData((byte) 1);
											break;
										case 3:
											b.setType(Material.RED_ROSE);
											b.setData((byte) 2);
											break;
										case 4:
											b.setType(Material.RED_ROSE);
											b.setData((byte) 3);
											break;
										case 5:
											b.setType(Material.RED_ROSE);
											b.setData((byte) 4);
											break;
										case 6:
											b.setType(Material.RED_ROSE);
											b.setData((byte) 5);
											break;
										case 7:
											b.setType(Material.RED_ROSE);
											b.setData((byte) 6);
											break;
										case 8:
											b.setType(Material.RED_ROSE);
											b.setData((byte) 7);
											break;
										case 9:
											b.setType(Material.RED_ROSE);
											b.setData((byte) 8);
											break;
									}
								}
								else
								{
									b.setType(Material.LONG_GRASS);
									b.setData((byte) 2);
								}
								break;
							case 6:
								//Air
								break;
						}
					}
				}
			}
			else
			{
				sender.sendMessage(ChatColor.YELLOW + "Removing..");
				//Remove everything
				for (int x = -i; x <= i; x++)
				{
					for (int z = -i; z <= i; z++)
					{
						double cX = pLoc.getX() + (x);
						double cZ = pLoc.getZ() + (z);
						double cY = sender.getWorld().getHighestBlockYAt(
								(int) cX, (int) cZ);
						
						Block b = w.getBlockAt((int) cX, (int) cY, (int) cZ);
						Material mat = b.getType();
						
						if (mat == Material.LONG_GRASS
								|| mat == Material.DOUBLE_PLANT
								|| mat == Material.RED_ROSE
								|| mat == Material.YELLOW_FLOWER)
						{
							b.setType(Material.AIR);
						}
					}
				}
			}
		}
	}
}
