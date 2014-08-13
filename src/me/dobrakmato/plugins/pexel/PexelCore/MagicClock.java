package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Class used for MagicClock.
 * 
 * @auhtor Mato Kormuth
 * 
 */
public class MagicClock implements Listener
{
	private InventoryMenu	im;
	
	public void buildInventoryMenu()
	{
		InventoryMenuItem everybodyItem = new InventoryMenuItem(
				InventoryMenuHelper.itemStack(Material.EYE_OF_ENDER,
						"Everybody", null), new JavaInventoryMenuAction(
						new ParametrizedRunnable() {
							@Override
							public void run(final Object... args)
							{
								for (Player onlinePlayer : Bukkit.getOnlinePlayers())
								{
									((Player) args[0]).showPlayer(onlinePlayer);
								}
							}
						}), 0);
		
		InventoryMenuItem nobodyItem = new InventoryMenuItem(
				InventoryMenuHelper.itemStack(Material.ENDER_PEARL, "Nobody",
						null), new JavaInventoryMenuAction(
						new ParametrizedRunnable() {
							@Override
							public void run(final Object... args)
							{
								for (Player onlinePlayer : Bukkit.getOnlinePlayers())
								{
									if (StorageEngine.getProfile(
											((Player) args[0]).getUniqueId()).isFriend(
											onlinePlayer.getUniqueId()))
										((Player) args[0]).showPlayer(onlinePlayer);
									else
										((Player) args[0]).hidePlayer(onlinePlayer);
								}
							}
						}), 1);
		
		InventoryMenuItem kickItem = new InventoryMenuItem(
				InventoryMenuHelper.itemStack(Material.APPLE, "Kick me", null),
				new KickInventoryMenuAction(), 1);
		
		InventoryMenuItem teleportItem = new InventoryMenuItem(
				InventoryMenuHelper.itemStack(Material.ENDER_PEARL, "Nobody",
						null), new TeleportInventoryMenuAction(new Location(
						Bukkit.getWorld("world"), 0, 255, 0)), 1);
		
		this.im = new InventoryMenu(
				InventoryType.CHEST,
				"Player visibility",
				Arrays.asList(everybodyItem, nobodyItem, kickItem, teleportItem));
	}
	
	public MagicClock()
	{
		Bukkit.getPluginManager().registerEvents(this, Pexel.getCore());
		this.buildInventoryMenu();
	}
	
	@EventHandler
	private void onPlayerInteract(final PlayerInteractEvent event)
	{
		if (event.getPlayer().getItemInHand() != null)
			if (event.getPlayer().getItemInHand().getType() == Material.WATCH)
				this.im.showTo(event.getPlayer());
		
		/*
		 * if (event.getPlayer().getItemInHand() != null) if (event.getPlayer().getItemInHand().getType() ==
		 * Material.WATCH) { ItemMeta meta = event.getPlayer().getItemInHand().getItemMeta(); String rezim =
		 * meta.getLore().get(0).substring(7);
		 * 
		 * if (rezim.equalsIgnoreCase("Vsetci")) { for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
		 * event.getPlayer().showPlayer(onlinePlayer); }
		 * 
		 * meta.setLore(Arrays.asList("Rezim: Priatelia", "Pouzi a skry/zobraz skaredych hracov.")); } else if
		 * (rezim.equalsIgnoreCase("Priatelia")) { for (Player onlinePlayer : Bukkit.getOnlinePlayers()) { if
		 * (StorageEngine.getProfile( event.getPlayer().getUniqueId()).isFriend( onlinePlayer.getUniqueId()))
		 * event.getPlayer().showPlayer(onlinePlayer); else event.getPlayer().hidePlayer(onlinePlayer); }
		 * 
		 * meta.setLore(Arrays.asList("Rezim: Nikto", "Pouzi a skry/zobraz skaredych hracov.")); } else if
		 * (rezim.equalsIgnoreCase("Nikto")) { for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
		 * event.getPlayer().hidePlayer(onlinePlayer); }
		 * 
		 * meta.setLore(Arrays.asList("Rezim: Vsetci", "Pouzi a skry/zobraz skaredych hracov.")); }
		 * 
		 * event.getPlayer().getItemInHand().setItemMeta(meta); }
		 */
	}
	
	public ItemStack getClock()
	{
		ItemStack itemstack = new ItemStack(Material.WATCH);
		ItemMeta meta = itemstack.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Magic Cock");
		meta.setLore(Arrays.asList("Rezim: Vsetci",
				"Pouzi a skry/zobraz skaredych hracov."));
		itemstack.setItemMeta(meta);
		return itemstack;
	}
}
