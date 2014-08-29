package me.dobrakmato.plugins.pexel.TntMinecart;

import me.dobrakmato.plugins.pexel.PexelCore.AdvancedMinigameArena;
import me.dobrakmato.plugins.pexel.PexelCore.ArenaOption;
import me.dobrakmato.plugins.pexel.PexelCore.Minigame;
import me.dobrakmato.plugins.pexel.PexelCore.Pexel;
import me.dobrakmato.plugins.pexel.PexelCore.Region;
import me.dobrakmato.plugins.pexel.PexelCore.Team;
import me.dobrakmato.plugins.pexel.PexelCore.TeamManager;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.ExplosiveMinecart;

/**
 * Tnt minecart arena
 * 
 * @author Mato Kormuth
 * 
 */
public class TntMinecartArena extends AdvancedMinigameArena
{
	private final Team			redTeam		= new Team(Color.RED, "Red team", 8);
	private final Team			blueTeam	= new Team(Color.BLUE, "Blue team",
													8);
	private final TeamManager	manager;
	
	@ArenaOption(name = "redSpawn")
	public Location				redSpawn;
	@ArenaOption(name = "blueSpawn")
	public Location				blueSpawn;
	
	//Minecart
	protected ExplosiveMinecart	minecart;
	
	@ArenaOption(name = "minecartSpawn")
	protected Location			minecartSpawn;
	
	private int					taskId		= 0;
	
	public TntMinecartArena(final Minigame minigame, final String arenaName,
			final Region region, final int maxPlayers, final int minPlayers,
			final Location lobbyLocation, final Location gameSpawn)
	{
		super(minigame, arenaName, region, maxPlayers, minPlayers,
				lobbyLocation, gameSpawn);
		
		this.manager = new TeamManager(this);
		this.manager.addTeam(this.redTeam);
		this.manager.addTeam(this.blueTeam);
		
		this.countdownLenght = 60;
	}
	
	@Override
	public void onGameStart()
	{
		for (Player p : this.activePlayers)
			this.clearPlayer(p);
		
		this.redTeam.applyArmorAll();
		this.blueTeam.applyArmorAll();
		
		this.redTeam.teleportAll(this.redSpawn);
		this.blueTeam.teleportAll(this.blueSpawn);
		
		this.getWorld().spawnEntity(this.minecartSpawn, EntityType.MINECART_TNT);
		
		this.taskId = Pexel.schedule(new Runnable() {
			@Override
			public void run()
			{
				
			}
		}, 0L, 2L);
	}
	
	@Override
	public void onReset()
	{
		super.onReset();
		Pexel.cancelTask(this.taskId);
	}
	
}
