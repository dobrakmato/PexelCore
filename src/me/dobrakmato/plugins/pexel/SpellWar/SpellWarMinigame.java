package me.dobrakmato.plugins.pexel.SpellWar;

import java.util.Collection;
import java.util.List;

import me.dobrakmato.plugins.pexel.PexelCore.arenas.MinigameArena;
import me.dobrakmato.plugins.pexel.PexelCore.minigame.Minigame;
import me.dobrakmato.plugins.pexel.PexelCore.minigame.MinigameCategory;
import me.dobrakmato.plugins.pexel.PexelCore.minigame.MinigameType;

import org.bukkit.Location;

/**
 * @author Mato Kormuth
 * 
 */
public class SpellWarMinigame implements Minigame
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
		return MinigameCategory.ARCADE;
	}
	
	@Override
	public List<MinigameType> getTypes()
	{
		return MinigameType.makeTypes();
	}
	
}
