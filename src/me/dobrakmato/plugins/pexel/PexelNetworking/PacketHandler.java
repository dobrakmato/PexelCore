package me.dobrakmato.plugins.pexel.PexelNetworking;

import java.io.DataInputStream;
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
	private final DataInputStream	inputStream;
	private boolean					serverInstance	= false;
	
	public PacketHandler(final DataInputStream inputStream,
			final boolean serverInstance)
	{
		this.inputStream = inputStream;
		this.serverInstance = serverInstance;
	}
	
	private void processPacket(final AbstractPacket packet)
	{
		if (packet != null)
			if (this.serverInstance)
				packet.handleServer();
			else
				packet.handleClient();
		else
			throw new NullArgumentException("packet");
	}
	
	public boolean isConnected()
	{
		return false;
	}
	
	public void handlePacket() throws IOException
	{
		//Packet header.
		PacketType packetType = PacketType.fromShort(this.inputStream.readShort());
		//Handle packet by type.
		this.processPacket(this.getPacket(packetType));
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
	
	public void sendPacket(final PexelPacket packet,
			final PexelServerClient client) throws IOException
	{
		//Write packet header
		client.getOutputStream().writeShort(
				PacketType.fromClass(packet.getClass()).getId());
		//Write packet body
		packet.write(client.getOutputStream());
	}
}
