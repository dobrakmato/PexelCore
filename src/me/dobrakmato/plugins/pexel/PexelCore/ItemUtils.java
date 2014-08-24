package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

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
	
	/**
	 * Just a utility, that returns names item stack with amount of 1.
	 * 
	 * <code><br><br>
	 * ItemStack is = new ItemStack(material);<br>
	 * 	ItemMeta im = is.getItemMeta();<br>
	 * 	if (im != null)<br>
	 * 		im.setDisplayName(displayName);<br>
	 * 	if (lore != null)<br>
	 * 		im.setLore(lore);<br>
	 * 	is.setItemMeta(im);<br>
	 * 	return is;<br>
	 * </code>
	 * 
	 * @param material
	 * @param displayName
	 * @param lore
	 * @return
	 */
	public static ItemStack namedItemStack(final Material material,
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
	
	public static ItemStack coloredLetherArmor(final Material material,
			final Color color)
	{
		ItemStack larmor = new ItemStack(material, 1);
		LeatherArmorMeta lam = (LeatherArmorMeta) larmor.getItemMeta();
		lam.setColor(color);
		larmor.setItemMeta(lam);
		return larmor;
	}
}
