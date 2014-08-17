package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.ArrayList;
import java.util.List;

import me.dobrakmato.plugins.pexel.TntTag.TntTagMinigame;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
		
		ParticleEffect[] values = ParticleEffect.values();
		for (int i = 0; i < values.length; i++)
		{
			ParticleEffect effect = values[i];
			particleTypesMenuItems.add(new InventoryMenuItem(
					ItemUtils.getNamedItemStack(Material.NETHER_STAR,
							effect.toString(), null),
					new JavaInventoryMenuAction(new ParametrizedRunnable() {
						@Override
						public void run(final Object... args)
						{
							// TODO Auto-generated method stub
							
						}
					}), i, true));
		}
		
		this.particleTypesMenu = new InventoryMenu(54, "Particle type",
				particleTypesMenuItems);
		
		particleAmountMenuItems.add(new InventoryMenuItem(
				ItemUtils.getNamedItemStack(Material.BOOK, "Few particles",
						null), new JavaInventoryMenuAction(
						new ParametrizedRunnable() {
							@Override
							public void run(final Object... args)
							{
								// TODO Auto-generated method stub
								
							}
						}), 0, true));
		
		particleAmountMenuItems.add(new InventoryMenuItem(
				ItemUtils.getNamedItemStack(Material.BOOK, "The right amount",
						null), new JavaInventoryMenuAction(
						new ParametrizedRunnable() {
							@Override
							public void run(final Object... args)
							{
								// TODO Auto-generated method stub
								
							}
						}), 1, true));
		
		particleAmountMenuItems.add(new InventoryMenuItem(
				ItemUtils.getNamedItemStack(Material.BOOK, "Many particles",
						null), new JavaInventoryMenuAction(
						new ParametrizedRunnable() {
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
				ItemUtils.getNamedItemStack(Material.NETHER_STAR,
						"Particle types", null), new OpenInventoryMenuAction(
						this.particleTypesMenu), 0, false));
		particleEffectMenuItems.add(new InventoryMenuItem(
				ItemUtils.getNamedItemStack(Material.BOOK, "Particle amount",
						null), new OpenInventoryMenuAction(
						this.particleAmountMenu), 1, false));
		particleEffectMenuItems.add(new InventoryMenuItem(
				ItemUtils.getNamedItemStack(Material.FIRE,
						"Particle animation", null),
				new OpenInventoryMenuAction(this.particleAnimationMenu), 2,
				false));
		
		this.particleEffectMenu = new InventoryMenu(InventoryType.CHEST,
				"Particle effects", particleEffectMenuItems);
	}
	
	@EventHandler
	private void onPrepocessCommand(final PlayerCommandPreprocessEvent event)
	{
		String command = event.getMessage().toLowerCase().replace("/", "");
		Player sender = event.getPlayer();
		if (command.equalsIgnoreCase("getcock"))
		{
			sender.getInventory().addItem(Pexel.getMagicClock().getClock());
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
		else if (command.contains("tnttest"))
		{
			sender.sendMessage(ChatColor.RED
					+ "Pexel.getMatchmaking().registerRequest(new MatchmakingRequest(Arrays.asList(event.getPlayer()),StorageEngine.getMinigame(\"tnttag\"), null));");
			((TntTagMinigame) StorageEngine.getMinigame("tnttag")).trrtrtr().onPlayerJoin(
					event.getPlayer());
		}
		else if (command.contains("secretparticles"))
		{
			this.particleEffectMenu.showTo(sender);
		}
	}
}
