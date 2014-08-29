package me.dobrakmato.plugins.pexel.ColorWar;

import java.util.Collection;

import me.dobrakmato.plugins.pexel.PexelCore.Minigame;
import me.dobrakmato.plugins.pexel.PexelCore.MinigameArena;
import me.dobrakmato.plugins.pexel.PexelCore.Pexel;
import me.dobrakmato.plugins.pexel.PexelCore.Region;
import me.dobrakmato.plugins.pexel.PexelCore.StorageEngine;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * @author Mato Kormuth
 * 
 */
public class ColorWarMinigame implements Minigame
{
	
	private final Location				lobbyLocation	= new Location(
																Bukkit.getWorld("world"),
																-1965, 141.5D,
																-1857);
	private final ColorWarArena	ar;
	
	public ColorWarMinigame()
	{
		StorageEngine.addMinigame(this);
		Pexel.getMatchmaking().registerMinigame(this);
		StorageEngine.registerArenaAlias(ColorWarArena.class,
				"ColorWarMinigameArena");
		ColorWarArena arena1 = new ColorWarArena(this,
				"cw_arena1",
				new Region(new Vector(-1961, 138, -1853), new Vector(-2030,
						103, -1799), Bukkit.getWorld("world")), new Location(
						Bukkit.getWorld("world"), -1996, 126.5D, -1826),
				this.lobbyLocation);
		Pexel.getMatchmaking().registerArena(arena1);
		this.ar = arena1;
	}
	
	@Override
	public String getDisplayName()
	{
		return "Color War";
	}
	
	@Override
	public String getName()
	{
		return "colorwar";
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
		return this.lobbyLocation;
	}
	
	public ColorWarArena trrtrtr()
	{
		return this.ar;
	}
	
}
