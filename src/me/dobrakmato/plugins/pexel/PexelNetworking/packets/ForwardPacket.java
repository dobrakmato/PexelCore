package me.dobrakmato.plugins.pexel.PexelNetworking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import me.dobrakmato.plugins.pexel.PexelNetworking.AbstractPacket;

/**
 * @author Mato Kormuth
 * 
 */
public class ForwardPacket extends AbstractPacket
{
	public AbstractPacket	packet;
	public String			serverName;
	
	@Override
	public void write(final DataOutputStream stream) throws IOException
	{
		
	}
	
	public static ForwardPacket read(final DataInputStream stream)
			throws IOException
	{
		ForwardPacket packet = new ForwardPacket();
		
		return packet;
	}
	
	@Override
	public void handleClient()
	{
		//No handling.
	}
	
	@Override
	public void handleServer()
	{
		//Forward to specified client.
		
	}
	
}
