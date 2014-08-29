package me.dobrakmato.plugins.pexel.PexelCore;

import me.dobrakmato.plugins.pexel.PexelNetworking.Server;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

/**
 * Inventory action that teleports player to specified location.
 * 
 * @author Mato Kormuth
 * 
 */
public class TeleportAction implements Action
{
	/**
	 * Target location.
	 */
	private Location	location;
	/**
	 * Target server.
	 */
	private Server		server;
	
	/**
	 * Creates a new action.
	 * 
	 * @param location
	 */
	public TeleportAction(final Location location)
	{
		this.location = location;
	}
	
	/**
	 * Creates a new action.
	 * 
	 * @param location
	 */
	public TeleportAction(final Location location, final Server server)
	{
		this.location = location;
		this.server = server;
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
		if (this.server.isLocalServer())
		{
			//Just teleport player to target location.
			player.teleport(this.location);
		}
		else
		{
			//TODO: Add teleport to location. Perform server-wide teleport
			
			//Teleport to other server
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Connect");
			out.writeUTF(this.server.getBungeeName());
			player.sendPluginMessage(Pexel.getCore(), "BungeeCord",
					out.toByteArray());
		}
	}
}
