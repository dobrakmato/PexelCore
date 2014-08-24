package me.dobrakmato.plugins.pexel.PexelNetworking;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Packet for pexel server protocol.
 * 
 * @author Mato Kormuth
 * 
 */
public interface PexelPacket
{
	public void write(DataOutputStream stream) throws IOException;
}
