package me.dobrakmato.plugins.pexel.PexelCore;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Class that represents item in inventory menu.
 * 
 * @author Mato Kormuth
 * 
 */
public class InventoryMenuItem
{
	private final ItemStack				item;
	private final InventoryMenuAction	action;
	private final int					slot;
	
	public InventoryMenuItem(final ItemStack item,
			final InventoryMenuAction action, final int slot)
	{
		this.item = item;
		this.action = action;
		this.slot = slot;
	}
	
	public void execute(final Player player)
	{
		this.action.execute(player);
	}
	
	public ItemStack getItemStack()
	{
		return this.item;
	}
	
	public int getSlot()
	{
		return this.slot;
	}
}
