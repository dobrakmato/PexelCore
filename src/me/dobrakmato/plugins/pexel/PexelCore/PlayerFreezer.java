package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Utility class that help freezing of players.
 * 
 * @author Mato Kormuth
 * 
 */
public class PlayerFreezer implements Listener
{
	/**
	 * List of frozen textures.
	 */
	private final List<Player>	frozen	= new ArrayList<Player>();
	
	public PlayerFreezer()
	{
		Bukkit.getPluginManager().registerEvents(this, Pexel.getCore());
	}
	
	/**
	 * Freezes (disables movement) the player.
	 * 
	 * @param player
	 */
	public void freeze(final Player player)
	{
		this.frozen.add(player);
	}
	
	/**
	 * Unfreezes (enables movement) the player.
	 * 
	 * @param player
	 */
	public void unfreeze(final Player player)
	{
		this.frozen.remove(player);
	}
	
	@EventHandler
	private void onPlayerMove(final PlayerMoveEvent event)
	{
		if (this.frozen.contains(event.getPlayer()))
			event.setTo(event.getFrom());
	}
}
