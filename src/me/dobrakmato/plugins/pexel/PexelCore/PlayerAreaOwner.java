package me.dobrakmato.plugins.pexel.PexelCore;

import org.bukkit.entity.Player;

/**
 * @author Mato Kormuth
 * 
 */
public class PlayerAreaOwner implements AreaOwner
{
	/**
	 * Player who is owner.
	 */
	private Player	owner;
	
	@Override
	public String getName()
	{
		return this.owner.getName();
	}
}
