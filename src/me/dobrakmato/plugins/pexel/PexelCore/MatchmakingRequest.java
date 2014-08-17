package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;

/**
 * Request for matchmaking.
 * 
 * @author Mato Kormuth
 * 
 */
public class MatchmakingRequest
{
	/**
	 * List of player in request.
	 */
	private final List<Player>		players;
	/**
	 * The minigame that the players want to play.
	 */
	private final Minigame			minigame;
	/**
	 * Arena that players want to play.
	 */
	private final MatchmakingGame	game;
	
	public MatchmakingRequest(final List<Player> players,
			final Minigame minigame, final MatchmakingGame game)
	{
		this.players = players;
		this.minigame = minigame;
		this.game = game;
	}
	
	/**
	 * Creates new request with random game and arena.
	 * 
	 * @param player
	 *            player
	 * @return
	 */
	public static MatchmakingRequest create(final Player player)
	{
		return new MatchmakingRequest(Arrays.asList(player), null, null);
	}
	
	/**
	 * Creates new request with random game and arena.
	 * 
	 * @param player
	 *            players
	 * @return
	 */
	public static MatchmakingRequest create(final Player... player)
	{
		return new MatchmakingRequest(Arrays.asList(player), null, null);
	}
	
	/**
	 * Creates new request with specified game and random arena.
	 * 
	 * @param player
	 *            player
	 * @return
	 */
	public static MatchmakingRequest create(final Player player,
			final Minigame minigame)
	{
		return new MatchmakingRequest(Arrays.asList(player), minigame, null);
	}
	
	/**
	 * Creates new request with specified game and random arena.
	 * 
	 * @param player
	 *            players
	 * @return
	 */
	public static MatchmakingRequest create(final Minigame minigame,
			final Player... player)
	{
		return new MatchmakingRequest(Arrays.asList(player), minigame, null);
	}
	
	/**
	 * Updates server server location ({@link ServerLocation}) of players in request. Does not teleport players.
	 * 
	 * @param location
	 */
	public void updateServerLocation(final ServerLocation location)
	{
		for (Player p : this.players)
			StorageEngine.getProfile(p.getUniqueId()).setServerLocation(
					location);
	}
	
	/**
	 * Returns list of players in this request.
	 * 
	 * @return
	 */
	public List<Player> getPlayers()
	{
		return this.players;
	}
	
	/**
	 * Returns minigame of this request.
	 * 
	 * @return
	 */
	public Minigame getMinigame()
	{
		return this.minigame;
	}
	
	/**
	 * Returns arena of this request.
	 * 
	 * @return
	 */
	public MatchmakingGame getGame()
	{
		return this.game;
	}
	
	/**
	 * Returns player count in this request.
	 * 
	 * @return
	 */
	public int playerCount()
	{
		return this.players.size();
	}
}
