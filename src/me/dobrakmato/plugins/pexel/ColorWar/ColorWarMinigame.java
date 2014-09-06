package me.dobrakmato.plugins.pexel.ColorWar;

import java.util.Collection;
import java.util.List;

import me.dobrakmato.plugins.pexel.PexelCore.arenas.MinigameArena;
import me.dobrakmato.plugins.pexel.PexelCore.core.Pexel;
import me.dobrakmato.plugins.pexel.PexelCore.core.Region;
import me.dobrakmato.plugins.pexel.PexelCore.core.StorageEngine;
import me.dobrakmato.plugins.pexel.PexelCore.minigame.Minigame;
import me.dobrakmato.plugins.pexel.PexelCore.minigame.MinigameCategory;
import me.dobrakmato.plugins.pexel.PexelCore.minigame.MinigameType;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

/**
 * @author Mato Kormuth
 * 
 */
public class ColorWarMinigame implements Minigame
{
	
	private final Location		lobbyLocation	= new Location(
														Bukkit.getWorld("world"),
														-1965, 141.5D, -1857);
	private final ColorWarArena	ar;
	
	public ColorWarMinigame()
	{
		StorageEngine.addMinigame(this);
		Pexel.getMatchmaking().registerMinigame(this);
		StorageEngine.registerArenaAlias(ColorWarArena.class,
				"ColorWarMinigameArena");
		
		ColorWarArena demo_arena = new ColorWarArena(this, "cw_arena1",
				new Region(new Vector(-1961, 138, -1853), new Vector(-2030,
						103, -1799), Bukkit.getWorld("world")), new Location(
						Bukkit.getWorld("world"), -1996, 126.5D, -1826),
				this.lobbyLocation);
		
		ColorWarArena arena1 = new ColorWarArena(this, "arena1", new Region(
				new Vector(-688, 25, -1058), new Vector(-758, 150, -1128),
				Bukkit.getWorld("Minigame")), new Location(
				Bukkit.getWorld("Minigame"), -722, 57.5D, -1092), new Location(
				Bukkit.getWorld("Minigame"), -722.5, 57D, -1092.5));
		
		World w = Bukkit.getWorld("Minigame");
		
		arena1.redSpawn = new Location(w, -693.5, 67, -1063.5);
		arena1.blueSpawn = new Location(w, -753.5, 67, -1063.5);
		arena1.greenSpawn = new Location(w, -753.5, 67, -1123.5);
		arena1.yellowSpawn = new Location(w, -692.5, 67, -1123.5);
		
		Pexel.getMatchmaking().registerArena(demo_arena);
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
	
	@Override
	public MinigameCategory getCategory()
	{
		return MinigameCategory.ARCADE;
	}
	
	@Override
	public List<MinigameType> getTypes()
	{
		return MinigameType.makeTypes(MinigameType.TNT);
	}
	
}
