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
	/**
	 * ItemStack of item.
	 */
	private final ItemStack	item;
	/**
	 * Action to execute when item is clicked.
	 */
	private final Action	action;
	/**
	 * Slot of inventory.
	 */
	private final int		slot;
	/**
	 * Specifies if the InventoryMenu have to close, after click.
	 */
	private final boolean	closeAfterClick;
	
	/**
	 * Creates new Inventory menu item from specified params.
	 * 
	 * @param item
	 *            itemstack to use as icon.
	 * @param action
	 *            action to execute when player clicks icon.
	 * @param slot
	 *            slot in inventory, where the itemstack should be.
	 * @param closeAfterClick
	 *            boolean, that specify, if the menu should close after click on this item.
	 */
	public InventoryMenuItem(final ItemStack item, final Action action,
			final int slot, final boolean closeAfterClick)
	{
		this.item = item;
		this.action = action;
		this.slot = slot;
		this.closeAfterClick = closeAfterClick;
	}
	
	/**
	 * Executes action with specified player(sender).
	 * 
	 * @param player
	 */
	public void execute(final Player player)
	{
		this.action.execute(player);
	}
	
	/**
	 * Returns bukkit compactibile ItemStack of this menu item.
	 * 
	 * @return
	 */
	public ItemStack getItemStack()
	{
		return this.item;
	}
	
	/**
	 * Returns slot in minecraft inventory.
	 * 
	 * @return
	 */
	public int getSlot()
	{
		return this.slot;
	}
	
	/**
	 * Returns if the menu should close after click.
	 * 
	 * @return
	 */
	public boolean isCloseAfterClick()
	{
		return this.closeAfterClick;
	}
}
