package me.dobrakmato.plugins.pexel.PexelNetworking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import me.dobrakmato.plugins.pexel.PexelNetworking.AbstractPacket;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * @author Mato Kormuth
 * 
 */
public class CrossServerTeleportPacket extends AbstractPacket
{
	public UUID		player;
	public Location	loc;
	
	public CrossServerTeleportPacket(final UUID player, final Location location)
	{
		this.player = player;
		this.loc = location;
	}
	
	private CrossServerTeleportPacket()
	{
	}
	
	@Override
	public void write(final DataOutputStream stream) throws IOException
	{
		stream.writeUTF(this.player.toString());
		stream.writeDouble(this.loc.getX());
		stream.writeDouble(this.loc.getY());
		stream.writeDouble(this.loc.getZ());
		stream.writeFloat(this.loc.getYaw());
		stream.writeFloat(this.loc.getPitch());
		stream.writeUTF(this.loc.getWorld().getName());
	}
	
	public static CrossServerTeleportPacket read(final DataInputStream stream)
			throws IOException
	{
		CrossServerTeleportPacket packet = new CrossServerTeleportPacket();
		
		packet.player = UUID.fromString(stream.readUTF());
		
		double x = stream.readDouble();
		double y = stream.readDouble();
		double z = stream.readDouble();
		float yaw = stream.readFloat();
		float pitch = stream.readFloat();
		String worldName = stream.readUTF();
		
		packet.loc = new Location(Bukkit.getWorld(worldName), x, y, z, yaw,
				pitch);
		
		return packet;
	}
	
	@Override
	public void handleClient()
	{
		//Add to teleport queue, expire in 30 seconds.
	}
	
	@Override
	public void handleServer()
	{
		// TODO Auto-generated method stub
		
	}
}
