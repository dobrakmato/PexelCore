package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.List;

import org.bukkit.entity.Player;

/**
 * Specifies that the object is game participating in matchmaking.
 * 
 * @author Mato Kormuth
 * 
 */
public interface MatchmakingGame
{
	public int getFreeSlots();
	
	public int getMaximumSlots();
	
	public GameState getState();
	
	public ServerLocation getServerLocation();
	
	public List<Player> getPlayers();
	
	public int playerCount();
	
	public boolean canJoin();
	
	public boolean canJoin(int count);
	
	public void onPlayerJoin(Player player);
	
	public void onPlayerLeft(Player player);
}
