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
	/**
	 * Returns number of free slots in the game.
	 * 
	 * @return free slots count
	 */
	public int getFreeSlots();
	
	/**
	 * Returns number of all slots in game.
	 * 
	 * @return all slots count.
	 */
	public int getMaximumSlots();
	
	/**
	 * Returns game actual state.
	 * 
	 * @return
	 */
	public GameState getState();
	
	/**
	 * Returns game server locetion.
	 * 
	 * @return
	 */
	public ServerLocation getServerLocation();
	
	/**
	 * Returns list of players in game.
	 * 
	 * @return
	 */
	public List<Player> getPlayers();
	
	/**
	 * Returns number of player in game. Same as calling {@link MatchmakingRequest#getPlayers()}.size().
	 * 
	 * @return
	 */
	public int playerCount();
	
	/**
	 * Returns if one player can join game.
	 * 
	 * @return
	 */
	public boolean canJoin();
	
	/**
	 * Returns if specified amount of players can join game.
	 * 
	 * @param count
	 *            amount of players
	 * @return
	 */
	public boolean canJoin(int count);
	
	/**
	 * Called when player joins the game.
	 * 
	 * @param player
	 */
	public void onPlayerJoin(Player player);
	
	/**
	 * Called when player lefts the game.
	 * 
	 * @param player
	 */
	public void onPlayerLeft(Player player);
}
