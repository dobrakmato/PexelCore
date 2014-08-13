package me.dobrakmato.plugins.pexel.PexelCore;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Inventory action that teleports player to specified location.
 * 
 * @author Mato Kormuth
 * 
 */
public class TeleportInventoryMenuAction implements InventoryMenuAction
{
	/**
	 * Target location.
	 */
	private Location	location;
	
	/**
	 * Creates a new action.
	 * 
	 * @param location
	 */
	public TeleportInventoryMenuAction(final Location location)
	{
		this.location = location;
	}
	
	@Override
	public void load(final String string)
	{
		//Split string to elements.
		String[] elements = string.split("\\|");
		double x = Double.parseDouble(elements[0]);
		double y = Double.parseDouble(elements[1]);
		double z = Double.parseDouble(elements[2]);
		float pitch = Float.parseFloat(elements[3]);
		float yaw = Float.parseFloat(elements[4]);
		String name = elements[5];
		//Build location from elements.
		this.location = new Location(Bukkit.getWorld(name), x, y, z, yaw, pitch);
	}
	
	@Override
	public String save()
	{
		//Serialize location data to string.
		return this.location.getX() + "|" + this.location.getY() + "|"
				+ this.location.getZ() + "|" + this.location.getPitch() + "|"
				+ this.location.getYaw() + "|"
				+ this.location.getWorld().getName();
	}
	
	@Override
	public void execute(final Player player)
	{
		//Just teleport player to target location.
		player.teleport(this.location);
	}
}
