package me.dobrakmato.plugins.pexel.PexelNetworking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
	
	@Override
	public boolean isBroadcasted()
	{
		return true;
	}
	
	public static CrossServerChatMessage read(final DataInputStream stream)
			throws IOException
	{
		CrossServerChatMessage packet = new CrossServerChatMessage();
		
		packet.message = stream.readUTF();
		
		return packet;
	}
	
	@Override
	public void handleLocal()
	{
		Bukkit.broadcastMessage(this.message);
	}
}
