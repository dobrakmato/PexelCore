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
				new Region(new Vector(0, 0, 0), new Vector(128, 128, 128),
						Bukkit.getWorld("world")), 16, 3, new Location(
						Bukkit.getWorld("world"), 64, 129, 64), new Location(
						Bukkit.getWorld("world"), 64, 5, 64));
		
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
