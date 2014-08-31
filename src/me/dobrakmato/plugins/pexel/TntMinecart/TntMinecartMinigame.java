package me.dobrakmato.plugins.pexel.TntMinecart;

import java.util.Collection;
import java.util.List;

import me.dobrakmato.plugins.pexel.PexelCore.Minigame;
import me.dobrakmato.plugins.pexel.PexelCore.MinigameArena;
import me.dobrakmato.plugins.pexel.PexelCore.MinigameCategory;
import me.dobrakmato.plugins.pexel.PexelCore.MinigameType;

import org.bukkit.Location;

/**
 * @author Mato Kormuth
 * 
 */
public class TntMinecartMinigame implements Minigame
{
	
	@Override
	public String getDisplayName()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public MinigameArena getArena(final String name)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Collection<MinigameArena> getArenas()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Location getLobbyLocation()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public MinigameCategory getCategory()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<MinigameType> getTypes()
	{
		return MinigameType.makeTypes(MinigameType.TNT);
	}
	
}
