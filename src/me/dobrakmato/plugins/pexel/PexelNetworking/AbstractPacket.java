package me.dobrakmato.plugins.pexel.PexelNetworking;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.util.org.apache.commons.lang3.NotImplementedException;

/**
 * @author Mato Kormuth
 * 
 */
public abstract class AbstractPacket implements PexelPacket
{
	public short	packetId;
	
	public abstract void handleLocal();
	
	public boolean isBroadcasted()
	{
		return false;
	}
	
	public static AbstractPacket read(final DataInputStream stream)
			throws IOException
	{
		throw new NotImplementedException("Read method not implemented!");
	}
}
