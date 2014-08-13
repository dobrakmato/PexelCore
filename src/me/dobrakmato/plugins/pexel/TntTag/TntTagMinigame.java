package me.dobrakmato.plugins.pexel.TntTag;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import me.dobrakmato.plugins.pexel.PexelCore.Minigame;
import me.dobrakmato.plugins.pexel.PexelCore.MinigameArena;
import me.dobrakmato.plugins.pexel.PexelCore.Pexel;
import me.dobrakmato.plugins.pexel.PexelCore.Region;

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
																	0, 0, 0);
	
	public TntTagMinigame()
	{
		Pexel.getMatchmaking().registerMinigame(this);
		
		this.makeArenas();
	}
	
	public void makeArenas()
	{
		TntTagArena arena1 = new TntTagArena(new Region(new Vector(0, 0, 0),
				new Vector(128, 128, 128), Bukkit.getWorld("world")), this,
				"arena1");
		arena1.reset();
		
		Pexel.getMatchmaking().registerArena(arena1);
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
	public Location getLobby()
	{
		return this.minigameLobby;
	}
}
