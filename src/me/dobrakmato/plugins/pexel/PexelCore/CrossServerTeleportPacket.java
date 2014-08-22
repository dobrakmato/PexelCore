package me.dobrakmato.plugins.pexel.PexelCore;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import org.bukkit.entity.Player;

/**
 * @author Mato Kormuth
 * 
 */
public class CrossServerTeleportPacket implements PexelPacket
{
	
	/**
	 * @param player
	 * @param actionContent
	 */
	public CrossServerTeleportPacket(final Player player,
			final String actionContent)
	{
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void write(final DataOutputStream stream)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void read(final DataInputStream stream)
	{
		// TODO Auto-generated method stub
		
	}
	
}
