package me.dobrakmato.plugins.pexel.ControlPoints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dobrakmato.plugins.pexel.PexelCore.Pexel;
import me.dobrakmato.plugins.pexel.PexelCore.arenas.AdvancedMinigameArena;
import me.dobrakmato.plugins.pexel.PexelCore.arenas.ArenaOption;
import me.dobrakmato.plugins.pexel.PexelCore.core.Region;
import me.dobrakmato.plugins.pexel.PexelCore.kits.Kit;
import me.dobrakmato.plugins.pexel.PexelCore.kits.KitInventoryMenu;
import me.dobrakmato.plugins.pexel.PexelCore.minigame.Minigame;
import me.dobrakmato.plugins.pexel.PexelCore.teams.Team;
import me.dobrakmato.plugins.pexel.PexelCore.teams.TeamManager;
import me.dobrakmato.plugins.pexel.PexelCore.utils.ItemUtils;
import me.dobrakmato.plugins.pexel.PexelCore.utils.ParametrizedRunnable;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Mato Kormuth
 * 
 */
public class ControlPointsArena extends AdvancedMinigameArena
{
	private final Map<Player, Kit>		kits		= new HashMap<Player, Kit>();
	
	private final Team					redTeam		= new Team(Color.RED,
															"Red team", 12);
	private final Team					blueTeam	= new Team(Color.BLUE,
															"Blue team", 12);
	private final TeamManager			manager;
	
	@ArenaOption(name = "redSpawn")
	public Location						redSpawn;
	@ArenaOption(name = "blueSpawn")
	public Location						blueSpawn;
	
	public Kit							kit1;
	public Kit							kit2;
	public Kit							kit3;
	public KitInventoryMenu				kitMenu;
	
	private int							taskId;
	
	//Control points
	private final List<ControlPoint>	points		= new ArrayList<ControlPoint>();
	
	public ControlPointsArena(final Minigame minigame, final String arenaName,
			final Region region, final int maxPlayers, final int minPlayers,
			final Location lobbyLocation, final Location gameSpawn)
	{
		super(minigame, arenaName, region, maxPlayers, minPlayers,
				lobbyLocation, gameSpawn);
		
		this.manager = new TeamManager(this);
		this.manager.addTeam(this.redTeam);
		this.manager.addTeam(this.blueTeam);
		
		this.playersCanRespawn = true;
		
		this.kit1 = new Kit(Arrays.asList(new ItemStack(Material.ARROW, 32),
				new ItemStack(Material.BOW, 1)), ItemUtils.namedItemStack(
				Material.ARROW, "Archer", null));
		
		this.kit2 = new Kit(Arrays.asList(new ItemStack(Material.IRON_SWORD)),
				ItemUtils.namedItemStack(Material.IRON_SWORD, "Swordman", null));
		
		this.kit3 = new Kit(Arrays.asList(new ItemStack(Material.POTION)),
				ItemUtils.namedItemStack(Material.POTION, "Witch", null));
		
		this.kitMenu = new KitInventoryMenu(this.kit1, this.kit2, this.kit3);
		
		this.kitMenu.setOnKitSelected(new ParametrizedRunnable() {
			@Override
			public void run(final Object... args)
			{
				ControlPointsArena.this.kits.put((Player) args[0],
						(Kit) args[1]);
			}
		});
		
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
		
		this.taskId = Pexel.schedule(new Runnable() {
			@Override
			public void run()
			{
				ControlPointsArena.this.checkPoints();
			}
		}, 0L, 5L);
	}
	
	protected void checkPoints()
	{
		for (ControlPoint cp : this.points)
		{
			int credits = 0;
			
			for (Player p : this.getNearByPlayers(cp.location, 4))
			{
				if (this.manager.getTeam(p) == this.blueTeam)
					credits += 1;
				else
					credits -= 1;
			}
			
			cp.addCredits(credits);
			cp.updateBlocks();
		}
	}
	
	public Team getOppositeTeam(final Team team)
	{
		if (team == this.redTeam)
			return this.blueTeam;
		else
			return this.redTeam;
	}
	
	public List<Player> getNearByPlayers(final Location loc, final int radius)
	{
		List<Player> players = new ArrayList<Player>();
		for (Player p : this.activePlayers)
			if (loc.distanceSquared(p.getLocation()) < radius * radius)
				players.add(p);
		return players;
	}
	
	public int getCPCountByTeam(final Team team)
	{
		int count = 0;
		for (ControlPoint cp : this.points)
			if (cp.getTeam() == team)
				count++;
		return count;
	}
	
	@Override
	public void onReset()
	{
		super.onReset();
		this.kits.clear();
		this.manager.reset();
		Pexel.cancelTask(this.taskId);
	}
	
	@Override
	public void onPlayerJoin(final Player player)
	{
		super.onPlayerJoin(player);
		this.kitMenu.showTo(player);
	}
	
	@EventHandler
	private void onPlayerRespawn(final PlayerRespawnEvent event)
	{
		if (this.activePlayers.contains(event.getPlayer()))
		{
			//Teleport to team spawn and apply armor.
			if (this.manager.getTeam(event.getPlayer()) == this.blueTeam)
			{
				event.getPlayer().teleport(this.blueSpawn);
				this.blueTeam.applyArmorTo(event.getPlayer());
			}
			else
			{
				event.getPlayer().teleport(this.redSpawn);
				this.redTeam.applyArmorTo(event.getPlayer());
			}
			//Apply kit.
			this.kits.get(event.getPlayer()).applyKit(event.getPlayer());
		}
	}
	
	protected class ControlPoint
	{
		private static final int	MAX_CREDITS	= 80;
		private static final int	MIN_CREDITS	= -80;
		
		public final Location		location;
		public int					credits;
		private Team				lastTeam;
		
		public ControlPoint(final Location location, final int credits,
				final Team lastTeam)
		{
			super();
			this.location = location;
			this.credits = credits;
			this.lastTeam = lastTeam;
		}
		
		public void addCredits(final int amount)
		{
			if (this.credits + amount > ControlPoint.MAX_CREDITS)
				this.credits = ControlPoint.MAX_CREDITS;
			else if (this.credits + amount < ControlPoint.MIN_CREDITS)
				this.credits = ControlPoint.MIN_CREDITS;
		}
		
		public Team getTeam()
		{
			if (this.credits < 0)
				return ControlPointsArena.this.redTeam;
			else
				return ControlPointsArena.this.blueTeam;
		}
		
		@SuppressWarnings("deprecation")
		public void updateBlocks()
		{
			//If need redraw
			if (this.lastTeam != this.getTeam())
			{
				this.lastTeam = this.getTeam();
				byte color = 0;
				if (this.lastTeam == ControlPointsArena.this.redTeam)
					color = 14;
				else
					color = 11;
				
				for (int x = this.location.getBlockX() - 3; x < this.location.getBlockX() + 3; x++)
				{
					for (int z = this.location.getBlockZ() - 3; z < this.location.getBlockZ() + 3; z++)
					{
						this.location.getWorld().getBlockAt(x,
								this.location.getBlockY(), z).setType(
								Material.WOOL);
						this.location.getWorld().getBlockAt(x,
								this.location.getBlockY(), z).setData(color);
					}
				}
			}
		}
	}
}
