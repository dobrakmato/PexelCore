// @formatter:off
/*
 * Pexel Project - Minecraft minigame server platform. 
 * Copyright (C) 2014 Matej Kormuth <http://www.matejkormuth.eu>
 * 
 * This file is part of Pexel.
 * 
 * Pexel is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * Pexel is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */
// @formatter:on
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
