package me.dobrakmato.plugins.pexel.PexelNetworking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import me.dobrakmato.plugins.pexel.PexelNetworking.packets.CrossServerTeleportPacket;

import org.apache.commons.lang.NullArgumentException;

/**
 * packet handler.
 * 
 * @author Mato Kormuth
 * 
 */
public class PacketHandler
{
	private DataInputStream		inputStream;
	private DataOutputStream	outputStream;
	private boolean				isMaster;
	
	public void handlePacket(final AbstractPacket packet)
	{
		if (packet != null)
			if (this.isMaster)
			{
				if (packet.isBroadcasted())
					this.broadcast(packet);
				
				packet.handleLocal();
			}
			else
				packet.handleLocal();
		else
			throw new NullArgumentException("packet");
	}
	
	private void broadcast(final AbstractPacket packet)
	{
		
	}
	
	public boolean isConnected()
	{
		return false;
	}
	
	public void readPacket() throws IOException
	{
		//Packet header.
		PacketType packetType = PacketType.fromShort(this.inputStream.readShort());
		//Handle packet by type.
		this.handlePacket(this.getPacket(packetType));
	}
	
	private AbstractPacket getPacket(final PacketType packetType)
			throws IOException
	{
		switch (packetType)
		{
			case CrossServerTeleportPacket:
				return CrossServerTeleportPacket.read(this.inputStream);
			default:
				return null;
		}
	}
	
	public void sendPacket(final PexelPacket packet) throws IOException
	{
		//Write packet header
		this.outputStream.writeShort(PacketType.fromClass(packet.getClass()).getId());
		//Write packet body
		packet.write(this.outputStream);
	}
}
