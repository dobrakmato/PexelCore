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
	
	/**
	 * Creates new Inventory menu object with specified type of inventory, title and list of items.
	 * 
	 * @see InventoryMenuItem
	 * @see InventoryMenuAction
	 * @see ItemUtils
	 * 
	 * @param type
	 *            type of inventory
	 * @param title
	 *            title of inventory
	 * @param items
	 *            items
	 */
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
	 * 
	 * Creates new Inventory menu object with specified size of inventory, title and list of items.
	 * 
	 * @param size
	 *            size of inventory
	 * @param title
	 *            inventory title
	 * @param items
	 *            items
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
	
	/**
	 * Same as calling {@link InventoryMenu#showTo(Player)}.
	 * 
	 * @see InventoryMenu#showTo(Player)
	 * 
	 * @param player
	 *            player to show menu to
	 */
	public void openInventory(final Player player)
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
		if (this.items.containsKey(slot))
		{
			this.items.get(slot).execute(player);
		}
		else
		{
			Log.warn("Player '" + player
					+ "' clicked on invalid item at slot '" + slot
					+ "' in inventoryMenu!");
		}
	}
}
