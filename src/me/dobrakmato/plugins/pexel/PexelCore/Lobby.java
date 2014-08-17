package me.dobrakmato.plugins.pexel.PexelCore;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Lobby is protected area with some special functions.
 * 
 * @author Mato Kormuth
 * 
 */
public class Lobby extends ProtectedArea implements UpdatedPart
{
	/**
	 * Creates new lobby object with specified name and region.
	 * 
	 * @param name
	 * @param region
	 */
	public Lobby(final String name, final Region region)
	{
		super(name, region);
		//Wierd call, isn't it?
		this.updateStart(Pexel.getCore());
	}
	
	/**
	 * Location of lobby spawn.
	 */
	private Location	lobbySpawn;
	private int			taskId			= 0;
	
	/**
	 * How often should lobby check for players.
	 */
	private final long	checkInterval	= 20;	//40 ticks = 2 second.
	/**
	 * The minimal Y coordinate value, after the lobby will teleport players to its spawn.
	 */
	private final int	thresholdY		= 50;
	
	/**
	 * Returns lobby spawn.
	 * 
	 * @return spawn
	 */
	public Location getSpawn()
	{
		return this.lobbySpawn;
	}
	
	/**
	 * Sets lobby spawn.
	 * 
	 * @param location
	 */
	public void setSpawn(final Location location)
	{
		this.lobbySpawn = location;
	}
	
	/**
	 * Updates players. Adds potion effects and teleports them if needed.
	 */
	private void updatePlayers()
	{
		for (Player player : this.getRegion().getPlayersXZ())
		{
			//Lobby potion enhantsments.
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
					20 * 30, 2));
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,
					20 * 30, 1));
			
			//In-void lobby teleport.
			if (player.getLocation().getY() < this.thresholdY)
				player.teleport(this.lobbySpawn);
		}
	}
	
	@Override
	public void updateStart(final PexelCore plugin)
	{
		UpdatedParts.registerPart(this);
		this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin,
				new Runnable() {
					@Override
					public void run()
					{
						Lobby.this.updatePlayers();
					}
				}, 0, this.checkInterval);
	}
	
	@Override
	public void updateStop()
	{
		Bukkit.getScheduler().cancelTask(this.taskId);
	}
	
	/**
	 * @return the thresholdY
	 */
	public int getThresholdY()
	{
		return this.thresholdY;
	}
	
	/**
	 * @return the checkInterval
	 */
	public long getCheckInterval()
	{
		return this.checkInterval;
	}
}
