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
package me.dobrakmato.plugins.pexel.PexelNetworking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import me.dobrakmato.plugins.pexel.PexelCore.Pexel;
import me.dobrakmato.plugins.pexel.PexelNetworking.AbstractPacket;

import org.bukkit.Bukkit;

/**
 * Server wide chat message.
 * 
 * @author Mato Kormuth
 * 
 */
public class CrossServerChatMessage extends AbstractPacket
{
	public String	message;
	
	@Override
	public void write(final DataOutputStream stream) throws IOException
	{
		stream.writeUTF(this.message);
	}
	
	public static CrossServerChatMessage read(final DataInputStream stream)
			throws IOException
	{
		CrossServerChatMessage packet = new CrossServerChatMessage();
		
		packet.message = stream.readUTF();
		
		return packet;
	}
	
	@Override
	public void handleClient()
	{
		Bukkit.broadcastMessage(this.message);
	}
	
	@Override
	public void handleServer()
	{
		Pexel.getCore().pexelserver.broadcast(this);
	}
}
