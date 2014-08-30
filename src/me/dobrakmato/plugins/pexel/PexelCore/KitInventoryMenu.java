package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

/**
 * Class representating inventory menu for kits.
 * 
 * @author Mato Kormuth
 * 
 */
public class KitInventoryMenu
{
	private final InventoryMenu		menu;
	private ParametrizedRunnable	onKitSelected;
	
	/**
	 * Creates a new inventory menu for selecting kit.
	 * 
	 * @param kits
	 *            kits in inventory menu.
	 */
	public KitInventoryMenu(final Kit... kits)
	{
		List<InventoryMenuItem> items = new ArrayList<InventoryMenuItem>();
		for (int i = 0; i < kits.length; i++)
		{
			final Kit kit = kits[i];
			items.add(new InventoryMenuItem(kit.getIcon(),
					new JavaArbitraryAction(new ParametrizedRunnable() {
						@Override
						public void run(final Object... args)
						{
							kit.applyKit(((Player) args[0]));
							KitInventoryMenu.this.onKitSelected.run(args[0],
									kit);
						}
					}), i, false));
		}
		
		this.menu = new InventoryMenu(9, "Select kit: ", items);
	}
	
	public void setOnKitSelected(final ParametrizedRunnable runnable)
	{
		this.onKitSelected = runnable;
	}
	
	/**
	 * Shows inventory to player.
	 * 
	 * @param player
	 *            player to show menu to
	 */
	public void showTo(final Player player)
	{
		this.menu.showTo(player);
	}
}
