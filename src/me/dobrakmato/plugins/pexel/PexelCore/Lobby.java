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
	public Lobby(final String name)
	{
		super("lobby_" + name);
	}
	
	private Location	lobbySpawn;
	private int			taskId			= 0;
	
	private final long	checkInterval	= 40;	//40 ticks = 2 second.
	private final int	thresholdY		= 30;
	
	public Location getSpawn()
	{
		return this.lobbySpawn;
	}
	
	public void setSpawn(final Location location)
	{
		this.lobbySpawn = location;
	}
	
	private void updatePlayers()
	{
		for (Player player : this.getRegion().getPlayers())
		{
			//Lobby potion enhantsments.
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
					20 * 5, 2));
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,
					20 * 5, 2));
			
			//In-void lobby teleport.
			if (player.getLocation().getY() < this.thresholdY)
				player.teleport(this.lobbySpawn);
		}
	}
	
	@Override
	public void updateStart(final PexelCore plugin)
	{
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
}
