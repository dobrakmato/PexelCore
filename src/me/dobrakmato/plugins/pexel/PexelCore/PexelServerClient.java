package me.dobrakmato.plugins.pexel.PexelCore;

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
	
	public InetSocketAddress getAddress()
	{
		return this.address;
	}
	
	public void sendPacket(final PexelPacket packet)
	{
		
	}
	
	public boolean isConnected()
	{
		// TODO Auto-generated method stub
		return false;
	}
}
