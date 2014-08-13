package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

/**
 * Minigame arena.
 * 
 * @author Mato Kormuth
 * 
 */
public class MinigameArena extends ProtectedArea implements MatchmakingGame
{
	protected final int				slots;
	protected GameState				state			= GameState.WAITING_PLAYERS;
	protected final List<Player>	players			= new ArrayList<Player>();
	protected final Minigame		minigame;
	protected ServerLocation		serverLocation	= new ServerLocation(
															"Arena: "
																	+ this.areaName,
															ServerLocationType.MINIGAME);
	
	public MinigameArena(final Minigame minigame, final String arenaName,
			final int slots)
	{
		super(minigame.getName() + "_" + arenaName);
		this.minigame = minigame;
		this.slots = slots;
	}
	
	public void chatAll(final String msg)
	{
		for (Player p : this.players)
			p.chat(msg);
	}
	
	@Override
	public int getFreeSlots()
	{
		return this.slots - this.players.size();
	}
	
	@Override
	public int getMaximumSlots()
	{
		return this.slots;
	}
	
	@Override
	public GameState getState()
	{
		return this.state;
	}
	
	@Override
	public List<Player> getPlayers()
	{
		return this.players;
	}
	
	@Override
	public int playerCount()
	{
		return this.players.size();
	}
	
	@Override
	public boolean canJoin()
	{
		return this.getFreeSlots() >= 1
				&& (this.state == GameState.WAITING_PLAYERS || this.state == GameState.PLAYING_CANJOIN);
	}
	
	@Override
	public boolean canJoin(final int count)
	{
		return this.getFreeSlots() >= count
				&& (this.state == GameState.WAITING_PLAYERS || this.state == GameState.PLAYING_CANJOIN);
	}
	
	@Override
	public void onPlayerJoin(final Player player)
	{
		this.players.add(player);
	}
	
	@Override
	public void onPlayerLeft(final Player player)
	{
		this.players.remove(player);
	}
	
	public boolean empty()
	{
		return this.players.size() == 0;
	}
	
	public Minigame getMinigame()
	{
		return this.minigame;
	}
	
	@Override
	public ServerLocation getServerLocation()
	{
		return this.serverLocation;
	}
}
