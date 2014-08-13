package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/**
 * Class used for Inventory Menu.
 * 
 * @author Mato Kormuth
 * 
 */
public class InventoryMenu implements InventoryHolder
{
	private Inventory								inventory;
	
	private final InventoryType						type;
	private final String							title;
	private final Map<ItemStack, InventoryMenuItem>	items	= new HashMap<ItemStack, InventoryMenuItem>();
	
	public InventoryMenu(final InventoryType type, final String title,
			final List<InventoryMenuItem> items)
	{
		this.type = type;
		this.title = title;
		
		for (InventoryMenuItem item : items)
			this.items.put(item.getItemStack(), item);
		
		this.build();
	}
	
	private void build()
	{
		this.inventory = Bukkit.createInventory(this, this.type, this.title);
		for (InventoryMenuItem item : this.items.values())
			this.inventory.setItem(item.getSlot(), item.getItemStack());
	}
	
	public void showTo(final Player player)
	{
		player.openInventory(this.getInventory());
	}
	
	@Override
	public Inventory getInventory()
	{
		return this.inventory;
	}
	
	protected void inventoryClick(final Player player, final ItemStack item)
	{
		this.items.get(item).execute(player);
	}
}
