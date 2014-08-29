package me.dobrakmato.plugins.pexel.TntMinecart;

import java.util.List;

import me.dobrakmato.plugins.pexel.PexelCore.AdvancedMinigameArena;
import me.dobrakmato.plugins.pexel.PexelCore.ArenaOption;
import me.dobrakmato.plugins.pexel.PexelCore.Minigame;
import me.dobrakmato.plugins.pexel.PexelCore.Pexel;
import me.dobrakmato.plugins.pexel.PexelCore.Region;
import me.dobrakmato.plugins.pexel.PexelCore.Team;
import me.dobrakmato.plugins.pexel.PexelCore.TeamManager;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.util.Vector;

/**
 * Tnt minecart arena
 * 
 * @author Mato Kormuth
 * 
 */
public class TntMinecartArena extends AdvancedMinigameArena
{
	private final Team			redTeam			= new Team(Color.RED,
														"Red team", 12);
	private final Team			blueTeam		= new Team(Color.BLUE,
														"Blue team", 12);
	private final TeamManager	manager;
	
	@ArenaOption(name = "redSpawn")
	public Location				redSpawn;
	@ArenaOption(name = "blueSpawn")
	public Location				blueSpawn;
	
	//Minecart
	protected ExplosiveMinecart	minecart;
	
	@ArenaOption(name = "minecartSpawn")
	protected Location			minecartSpawn;
	
	@ArenaOption(name = "minecartRadius")
	public int					minecartRadius	= 4;
	@ArenaOption(name = "minecartSpeed")
	public float				minecartSpeed	= 0.1F;
	
	private int					taskId			= 0;
	
	public TntMinecartArena(final Minigame minigame, final String arenaName,
			final Region region, final int maxPlayers, final int minPlayers,
			final Location lobbyLocation, final Location gameSpawn)
	{
		super(minigame, arenaName, region, 24, 2, lobbyLocation, gameSpawn);
		
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
				TntMinecartArena.this.checkMinecart();
			}
		}, 0L, 2L);
	}
	
	protected void checkMinecart()
	{
		List<Entity> entities = this.minecart.getNearbyEntities(
				this.minecartRadius, this.minecartRadius, this.minecartRadius);
		
		float smer = 0F;
		
		for (Entity e : entities)
			if (e instanceof Player)
			{
				if (this.manager.getTeam((Player) e) == this.redTeam)
					smer += this.minecartSpeed;
				else
					smer -= this.minecartSpeed;
			}
		
		this.minecart.setVelocity(new Vector(smer, 0, 0));
	}
	
	@Override
	public void onReset()
	{
		super.onReset();
		Pexel.cancelTask(this.taskId);
	}
	
	@EventHandler
	private void onMineCartMove(final VehicleMoveEvent event)
	{
		if (event.getVehicle() == this.minecart)
			event.getVehicle().teleport(event.getFrom());
	}
	
	@EventHandler
	private void onEntityDamage(final EntityDamageByEntityEvent event)
	{
		if (event.getEntity() == this.minecart)
		{
			event.setDamage(0D);
			event.setCancelled(true);
		}
	}
	
}
