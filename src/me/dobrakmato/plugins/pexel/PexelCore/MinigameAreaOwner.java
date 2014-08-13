package me.dobrakmato.plugins.pexel.PexelCore;

/**
 * @author Mato Kormuth
 * 
 */
public class MinigameAreaOwner implements AreaOwner
{
	private Minigame	owner;
	
	@Override
	public String getName()
	{
		return this.owner.getName();
	}
}
