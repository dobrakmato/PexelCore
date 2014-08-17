package me.dobrakmato.plugins.pexel.PexelCore;

import org.bukkit.entity.Player;

/**
 * Inventory menu action that opens another inventory menu.
 * 
 * @author Mato Kormuth
 * 
 */
public class OpenInventoryMenuAction implements InventoryMenuAction
{
	private final InventoryMenu	im;
	
	public OpenInventoryMenuAction(final InventoryMenu im)
	{
		this.im = im;
	}
	
	@Override
	public void load(final String string)
	{
		
	}
	
	@Override
	public String save()
	{
		return null;
	}
	
	@Override
	public void execute(final Player player)
	{
		this.im.showTo(player);
	}
}
