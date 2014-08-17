package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Class used for Inventory Menu.
 * 
 * @author Mato Kormuth
 * 
 */
public class InventoryMenu implements InventoryHolder
{
	/**
	 * Inventory of this menu.
	 */
	private final Inventory							inventory;
	/**
	 * Items in inventory.
	 */
	private final Map<Integer, InventoryMenuItem>	items	= new HashMap<Integer, InventoryMenuItem>();
	
	public InventoryMenu(final InventoryType type, final String title,
			final List<InventoryMenuItem> items)
	{
		for (InventoryMenuItem item : items)
			if (!this.items.containsKey(item.getSlot()))
				this.items.put(item.getSlot(), item);
			else
				throw new RuntimeException(
						"Can't put "
								+ item.getItemStack().toString()
								+ " to slot "
								+ item.getSlot()
								+ "! Slot "
								+ item.getSlot()
								+ " is alredy occupied by "
								+ this.items.get(item.getSlot()).getItemStack().toString());
		
		this.inventory = Bukkit.createInventory(this, type, title);
		for (InventoryMenuItem item : this.items.values())
			this.inventory.setItem(item.getSlot(), item.getItemStack());
	}
	
	/**
	 * @param i
	 * @param title2
	 * @param particleTypesMenuItems
	 */
	public InventoryMenu(final int size, final String title,
			final List<InventoryMenuItem> items)
	{
		
		for (InventoryMenuItem item : items)
			if (!this.items.containsKey(item.getSlot()))
				this.items.put(item.getSlot(), item);
			else
				throw new RuntimeException(
						"Can't put "
								+ item.getItemStack().toString()
								+ " to slot "
								+ item.getSlot()
								+ "! Slot "
								+ item.getSlot()
								+ " is alredy occupied by "
								+ this.items.get(item.getSlot()).getItemStack().toString());
		
		this.inventory = Bukkit.createInventory(this, size, title);
		for (InventoryMenuItem item : this.items.values())
			this.inventory.setItem(item.getSlot(), item.getItemStack());
	}
	
	/**
	 * Opens this inventory menu to specified player.
	 * 
	 * @param player
	 *            player to show menu to
	 */
	public void showTo(final Player player)
	{
		player.openInventory(this.getInventory());
	}
	
	@Override
	public Inventory getInventory()
	{
		return this.inventory;
	}
	
	public boolean shouldClose(final int slot)
	{
		return this.items.get(slot).isCloseAfterClick();
	}
	
	/**
	 * Called when somebody clicks item in this inventory.
	 * 
	 * @param player
	 * @param item
	 */
	protected void inventoryClick(final Player player, final int slot)
	{
		this.items.get(slot).execute(player);
	}
}
