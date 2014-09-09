package me.dobrakmato.plugins.pexel.SpellWar;

import me.dobrakmato.plugins.pexel.PexelCore.arenas.AdvancedMinigameArena;
import me.dobrakmato.plugins.pexel.PexelCore.core.Region;
import me.dobrakmato.plugins.pexel.PexelCore.minigame.Minigame;

import org.bukkit.Location;

/**
 * @author Mato Kormuth
 * 
 */
public class SpellWarArena extends AdvancedMinigameArena
{
	public SpellWarArena(final Minigame minigame, final String arenaName,
			final Region region, final Location lobbyLocation,
			final Location gameSpawn)
	{
		super(minigame, arenaName, region, 32, 2, lobbyLocation, gameSpawn);
		
	}
	
	@Override
	public void onGameStart()
	{
		this.teleportPlayers(this.gameSpawn);
	}
}
