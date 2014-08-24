package me.dobrakmato.plugins.pexel.PexelNetworking;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Class used for comunicating with pexel compactibile servers.
 * 
 * @author Mato Kormuth
 * 
 */
public class PexelServerClient
{
	private InetSocketAddress	address;
	private PacketHandler		handler;
	
	public InetSocketAddress getAddress()
	{
		return this.address;
	}
	
	public void sendPacket(final PexelPacket packet)
	{
		try
		{
			this.handler.sendPacket(packet);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean isConnected()
	{
		// TODO Auto-generated method stub
		return false;
	}
}
