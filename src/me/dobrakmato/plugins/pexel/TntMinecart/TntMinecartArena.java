package me.dobrakmato.plugins.pexel.TntMinecart;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dobrakmato.plugins.pexel.PexelCore.Pexel;
import me.dobrakmato.plugins.pexel.PexelCore.arenas.AdvancedMinigameArena;
import me.dobrakmato.plugins.pexel.PexelCore.arenas.ArenaOption;
import me.dobrakmato.plugins.pexel.PexelCore.chat.ChatManager;
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
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 * Tnt minecart arena
 * 
 * @author Mato Kormuth
 * 
 */
public class TntMinecartArena extends AdvancedMinigameArena
{
	private final Map<Player, Kit>	kits			= new HashMap<Player, Kit>();
	
	private final Team				redTeam			= new Team(Color.RED,
															"Red team", 12);
	private final Team				blueTeam		= new Team(Color.BLUE,
															"Blue team", 12);
	private final TeamManager		manager;
	
	@ArenaOption(name = "redSpawn")
	public Location					redSpawn;
	@ArenaOption(name = "blueSpawn")
	public Location					blueSpawn;
	
	public Kit						kit1;
	public Kit						kit2;
	public Kit						kit3;
	public KitInventoryMenu			kitMenu;
	
	//Minecart
	protected ExplosiveMinecart		minecart;
	
	@ArenaOption(name = "minecartSpawn")
	protected Location				minecartSpawn;
	
	@ArenaOption(name = "minecartRadius")
	public int						minecartRadius	= 4;
	@ArenaOption(name = "minecartSpeed")
	public float					minecartSpeed	= 0.1F;
	
	private int						taskId			= 0;
	
	public TntMinecartArena(final Minigame minigame, final String arenaName,
			final Region region, final int maxPlayers, final int minPlayers,
			final Location lobbyLocation, final Location gameSpawn)
	{
		super(minigame, arenaName, region, 24, 2, lobbyLocation, gameSpawn);
		
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
				TntMinecartArena.this.kits.put((Player) args[0], (Kit) args[1]);
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
		
		this.getWorld().spawnEntity(this.minecartSpawn, EntityType.MINECART_TNT);
		
		this.taskId = Pexel.schedule(new Runnable() {
			@Override
			public void run()
			{
				TntMinecartArena.this.checkMinecart();
			}
		}, 0L, 5L);
	}
	
	protected void checkMinecart()
	{
		//Evaulate minecart movement.
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
	
	@EventHandler
	private void onEntityExplode(final EntityExplodeEvent event)
	{
		if (event.getEntity() == this.minecart)
		{
			if (this.minecart.getLocation().distanceSquared(this.blueSpawn) < this.minecart.getLocation().distanceSquared(
					this.redSpawn))
			{
				//Red team won
				for (Player p : this.redTeam.getPlayers())
					p.sendMessage(ChatManager.success("You have won!"));
			}
			else
			{
				//Blue team won
				for (Player p : this.blueTeam.getPlayers())
					p.sendMessage(ChatManager.success("You have successfully lost the match!"));
			}
			
			//Kick players
			for (Player p : this.activePlayers)
				this.onPlayerLeft(p);
		}
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
