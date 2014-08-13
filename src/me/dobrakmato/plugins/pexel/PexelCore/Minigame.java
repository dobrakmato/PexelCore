package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.Collection;

import me.dobrakmato.plugins.pexel.TntTag.TntTagArena;

import org.bukkit.Location;

/**
 * Interface for minigame.
 * 
 * @author Mato Kormuth
 * 
 */
public interface Minigame
{
	public String getDisplayName();
	
	public String getName();
	
	public MinigameArena getArena(String name);
	
	public Collection<TntTagArena> getArenas();
	
	public Location getLobby();
}
