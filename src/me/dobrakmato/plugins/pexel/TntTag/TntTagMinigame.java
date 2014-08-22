package me.dobrakmato.plugins.pexel.TntTag;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import me.dobrakmato.plugins.pexel.PexelCore.Minigame;
import me.dobrakmato.plugins.pexel.PexelCore.MinigameArena;
import me.dobrakmato.plugins.pexel.PexelCore.Pexel;
import me.dobrakmato.plugins.pexel.PexelCore.Region;
import me.dobrakmato.plugins.pexel.PexelCore.StorageEngine;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Recreation of Tnt Tag minigame plugin.
 * 
 * @author Mato Kormuth
 * 
 */
public class TntTagMinigame implements Minigame
{
	private final Map<String, TntTagArena>	arenas			= new HashMap<String, TntTagArena>();
	private final Location					minigameLobby	= new Location(
																	Bukkit.getWorld("world"),
																	-2034,
																	147.5,
																	-1931);
	
	private TntTagArena						are;
	
	public TntTagMinigame()
	{
		Pexel.getMatchmaking().registerMinigame(this);
		StorageEngine.addMinigame(this);
		StorageEngine.registerArenaAlias(TntTagArena.class, "TntTagArena");
		this.makeArenas();
	}
	
	public void makeArenas()
	{
		TntTagArena arena1 = new TntTagArena(
				new Region(new Vector(-1953, 0, -1946), new Vector(-2089, 255,
						-2089), Bukkit.getWorld("world")), this, "arena1");
		arena1.setGameSpawn(new Location(Bukkit.getWorld("world"), -2010,
				112.5, -1997));
		arena1.setLobbySpawn(new Location(Bukkit.getWorld("world"), -2010,
				138.5, -1997));
		arena1.reset();
		
		this.are = arena1;
		
		Pexel.getMatchmaking().registerArena(arena1);
	}
	
	public TntTagArena trrtrtr()
	{
		return this.are;
	}
	
	@Override
	public String getDisplayName()
	{
		return "TNT Tag";
	}
	
	@Override
	public String getName()
	{
		return "tnttag";
	}
	
	@Override
	public MinigameArena getArena(final String name)
	{
		return this.arenas.get(name);
	}
	
	@Override
	public Collection<MinigameArena> getArenas()
	{
		return null;
	}
	
	@Override
	public Location getLobbyLocation()
	{
		return this.minigameLobby;
	}
}
