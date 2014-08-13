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
	private final List<Player>		players;
	private final Minigame			minigame;
	private final MatchmakingGame	game;
	
	public MatchmakingRequest(final List<Player> players,
			final Minigame minigame, final MatchmakingGame game)
	{
		this.players = players;
		this.minigame = minigame;
		this.game = game;
	}
	
	public static MatchmakingRequest create(final Player player)
	{
		return new MatchmakingRequest(Arrays.asList(player), null, null);
	}
	
	public static MatchmakingRequest create(final Player... player)
	{
		return new MatchmakingRequest(Arrays.asList(player), null, null);
	}
	
	public static MatchmakingRequest create(final Player player,
			final Minigame minigame)
	{
		return new MatchmakingRequest(Arrays.asList(player), minigame, null);
	}
	
	public static MatchmakingRequest create(final Minigame minigame,
			final Player... player)
	{
		return new MatchmakingRequest(Arrays.asList(player), minigame, null);
	}
	
	public void updateLocation(final ServerLocation location)
	{
		for (Player p : this.players)
			StorageEngine.getProfile(p.getUniqueId()).setServerLocation(
					location);
	}
	
	public List<Player> getPlayers()
	{
		return this.players;
	}
	
	public Minigame getMinigame()
	{
		return this.minigame;
	}
	
	public MatchmakingGame getGame()
	{
		return this.game;
	}
	
	public int playerCount()
	{
		return this.players.size();
	}
}
