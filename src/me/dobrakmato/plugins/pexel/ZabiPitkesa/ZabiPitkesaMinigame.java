package me.dobrakmato.plugins.pexel.ZabiPitkesa;

import java.util.Collection;
import java.util.List;

import me.dobrakmato.plugins.pexel.PexelCore.Minigame;
import me.dobrakmato.plugins.pexel.PexelCore.MinigameArena;
import me.dobrakmato.plugins.pexel.PexelCore.MinigameCategory;
import me.dobrakmato.plugins.pexel.PexelCore.MinigameType;
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
	
	private final Location		lobbyLocation	= new Location(
														Bukkit.getWorld("world"),
														0, 0, 0);
	private ZabiPitkesaArena	a;
	
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
		this.a = new ZabiPitkesaArena(this, "arena1", new Region(new Vector(
				-2072, 130, -1817), new Vector(-2137, 0, -1882),
				Bukkit.getWorld("world")), 16, 2, new Location(
				Bukkit.getWorld("world"), -2103, 110, -1853), new Location(
				Bukkit.getWorld("world"), -2103, 101, -1853));
		
		Pexel.getMatchmaking().registerArena(this.a);
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
	
	public ZabiPitkesaArena trrtrtr()
	{
		return this.a;
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
