package me.dobrakmato.plugins.pexel.ZabiPitkesa;

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
public class ZabiPitkesaMinigame implements Minigame
{
	
	private final Location	lobbyLocation	= new Location(
													Bukkit.getWorld("world"),
													0, 0, 0);
	
	public ZabiPitkesaMinigame()
	{
		Pexel.getMatchmaking().registerMinigame(this);
		StorageEngine.addMinigame(this);
		StorageEngine.registerArenaAlias(ZabiPitkesaArena.class,
				"ZabiPitkesaArena");
		this.makeArenas();
	}
	
	private void makeArenas()
	{
		ZabiPitkesaArena arena1 = new ZabiPitkesaArena(this, "arena1",
				new Region(new Vector(-2072, 130, -1817), new Vector(-2137, 0,
						-1882), Bukkit.getWorld("world")), 16, 2, new Location(
						Bukkit.getWorld("world"), -2103, 110, -1853),
				new Location(Bukkit.getWorld("world"), -2103, 101, -1853));
		
		Pexel.getMatchmaking().registerArena(arena1);
	}
	
	@Override
	public String getDisplayName()
	{
		return "Zabi Pitkesa";
	}
	
	@Override
	public String getName()
	{
		return "zabipitkesa";
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
	
}
