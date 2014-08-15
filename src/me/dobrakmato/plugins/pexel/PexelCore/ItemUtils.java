package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Class that contains many useful functions for working with InventoryMenus.
 * 
 * @author Mato Kormuth
 * 
 */
public class ItemUtils
{
	public static ItemStack itemStack(final Material material,
			final int amount, final byte data, final short damage,
			final String displayName, final List<String> lore)
	{
		@SuppressWarnings("deprecation") ItemStack is = new ItemStack(material,
				amount, damage, data);
		ItemMeta im = is.getItemMeta();
		if (im != null)
			im.setDisplayName(displayName);
		if (lore != null)
			im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}
	
	public static ItemStack itemStack(final Material material,
			final int amount, final String displayName, final List<String> lore)
	{
		ItemStack is = new ItemStack(material, amount);
		ItemMeta im = is.getItemMeta();
		if (im != null)
			im.setDisplayName(displayName);
		if (lore != null)
			im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}
	
	public static ItemStack getNamedItemStack(final Material material,
			final String displayName, final List<String> lore)
	{
		ItemStack is = new ItemStack(material);
		ItemMeta im = is.getItemMeta();
		if (im != null)
			im.setDisplayName(displayName);
		if (lore != null)
			im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}
}
