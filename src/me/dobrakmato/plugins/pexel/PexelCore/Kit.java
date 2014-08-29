package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Class used for applying kits on players.
 * 
 * @author Mato Kormuth
 * 
 */
public class Kit
{
	private final List<ItemStack>	items;
	private ItemStack				helmet;
	private ItemStack				chestplate;
	private ItemStack				leggings;
	private ItemStack				boots;
	private final ItemStack			icon;
	
	/**
	 * Creates a new kit with specified items.
	 * 
	 * @param items
	 *            items in inventory
	 */
	public Kit(final List<ItemStack> items, final ItemStack icon)
	{
		this.items = items;
		this.icon = icon;
	}
	
	/**
	 * Creates a new kit with specified armor and items.
	 * 
	 * @param items
	 *            items in inventory
	 * @param helmet
	 * @param chestplate
	 * @param leggings
	 * @param boots
	 */
	public Kit(final List<ItemStack> items, final ItemStack icon,
			final ItemStack helmet, final ItemStack chestplate,
			final ItemStack leggings, final ItemStack boots)
	{
		this.items = items;
		this.icon = icon;
		this.helmet = helmet;
		this.chestplate = chestplate;
		this.leggings = leggings;
		this.boots = boots;
	}
	
	/**
	 * Applies kit on specified player, removing all previous items.
	 * 
	 * @param player
	 *            player to give kit to
	 */
	public void applyKit(final Player player)
	{
		player.getInventory().clear();
		
		if (this.helmet != null)
			player.getInventory().setHelmet(this.helmet);
		if (this.chestplate != null)
			player.getInventory().setChestplate(this.chestplate);
		if (this.leggings != null)
			player.getInventory().setLeggings(this.leggings);
		if (this.boots != null)
			player.getInventory().setBoots(this.boots);
		
		for (ItemStack itemstack : this.items)
			player.getInventory().addItem(itemstack);
	}
	
	/**
	 * Returns item stack of this kit icon.
	 * 
	 * @return item stack representing icon
	 */
	public ItemStack getIcon()
	{
		return this.icon;
	}
}
