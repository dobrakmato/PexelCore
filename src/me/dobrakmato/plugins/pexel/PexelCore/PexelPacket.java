package me.dobrakmato.plugins.pexel.PexelCore;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Packet for pexel server protocol.
 * 
 * @author Mato Kormuth
 * 
 */
public interface PexelPacket
{
	public void write(DataOutputStream stream);
	
	public void read(DataInputStream stream);
}
