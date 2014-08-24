package me.dobrakmato.plugins.pexel.PexelCore;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffectType;

/**
 * Arena that has built-in support for pre-game lobby and stuff... Also implements {@link Listener} and calls
 * {@link org.bukkit.plugin.PluginManager#registerEvents(Listener, org.bukkit.plugin.Plugin)} in constructor.
 * 
 * @author Mato Kormuth
 * 
 */
public class AdvancedMinigameArena extends MinigameArena implements Listener
{
	/**
	 * Amount of players, that is required to start the countdown.
	 */
	protected int		minimalPlayers				= 0;
	/**
	 * Lenght of countdown in seconds.
	 */
	protected int		countdownLenght				= 10;
	/**
	 * Location of this arena lobby.
	 */
	protected Location	lobbyLocation;
	/**
	 * Location of this arena game spawn.
	 */
	protected Location	gameSpawn;
	/**
	 * Specifies if the countdown should be canceled, if a player leaves arena and there is not enough players to start
	 * game, but the countdown is alredy running.
	 */
	protected boolean	countdownCanCancel			= true;
	/**
	 * Specifies if the players should be teleported to gameSpawn and lobbyLocation automaticaly.
	 */
	protected boolean	shouldTeleportPlayers		= true;
	/**
	 * Specifies if players can respawn in this arena, or not.
	 */
	protected boolean	playersCanRespawn			= true;
	/**
	 * Specifies if players can join the game after the game was started.
	 */
	protected boolean	playersCanJoinAfterStart	= false;
	/**
	 * Time left to game start.
	 */
	protected int		countdownTimeLeft			= 10;
	/**
	 * Specifies, if the arena should call <code>reset()</code> function automaticaly when game ends.
	 */
	protected boolean	autoReset					= false;
	/**
	 * Chat format for countdown message.
	 */
	protected String	countdownFormat				= "%timeleft% seconds to game start!";
	
	protected int		countdownTaskId				= 0;
	/**
	 * Identifies if the game has started.
	 */
	private boolean		gameStarted					= false;
	
	/**
	 * @param minigame
	 * @param arenaName
	 * @param region
	 * @param maxPlayers
	 */
	public AdvancedMinigameArena(final Minigame minigame, final String arenaName,
			final Region region, final int maxPlayers, final int minPlayers,
			final Location lobbyLocation, final Location gameSpawn)
	{
		super(minigame, arenaName, region, maxPlayers);
		
		this.minimalPlayers = minPlayers;
		this.lobbyLocation = lobbyLocation;
		this.gameSpawn = gameSpawn;
		
		Bukkit.getPluginManager().registerEvents(this, Pexel.getCore());
	}
	
	/**
	 * Clear player's inventory, removes effects and set game mode.
	 * 
	 * @param player
	 */
	public void clearPlayer(final Player player)
	{
		player.getInventory().clear();
		for (PotionEffectType effect : PotionEffectType.values())
			player.removePotionEffect(effect);
		player.setGameMode(this.defaultGameMode);
	}
	
	/**
	 * Teleports player to location by actual game state.
	 * 
	 * @param player
	 */
	public void teleportPlayer(final Player player)
	{
		if (this.state == GameState.WAITING_PLAYERS
				|| this.state == GameState.WAITING_EMPTY)
		{
			player.teleport(this.lobbyLocation);
		}
		else if (this.state == GameState.PLAYING_CANJOIN
				|| this.state == GameState.PLAYING_CANTJOIN)
		{
			player.teleport(this.gameSpawn);
		}
		else
		{
			throw new RuntimeException(
					"Arena is not in state, that supports teleporting players!");
		}
	}
	
	/**
	 * Teleports all players to specified location.
	 * 
	 * @param location
	 */
	public void teleportPlayers(final Location location)
	{
		for (Player p : this.activePlayers)
			p.teleport(location);
	}
	
	/**
	 * Returns boolean if is this arena prepeared for playing.
	 * 
	 * @return
	 */
	public boolean isPrepeared()
	{
		return this.gameSpawn != null && this.lobbyLocation != null
				&& this.minimalPlayers != 0;
	}
	
	/**
	 * Tries to start countdown.
	 */
	private void tryStartCountdown()
	{
		if (this.activePlayers.size() >= this.minimalPlayers)
			this.onCountdownStart();
		else
			this.onNotEnoughPlayers();
	}
	
	/**
	 * Tries to stop countdown.
	 */
	private void tryStopCountdown()
	{
		//Check if we can stop, once the countdown started.
		if (this.countdownCanCancel)
			this.onCountdownCancelled();
	}
	
	public World getWorld()
	{
		return this.gameSpawn.getWorld();
	}
	
	/**
	 * Called when countdown starts.
	 */
	public void onCountdownStart()
	{
		//Reset countdown time.
		this.countdownTimeLeft = this.countdownLenght;
		//Start countdown.
		this.countdownTaskId = Pexel.schedule(new Runnable() {
			@Override
			public void run()
			{
				AdvancedMinigameArena.this.countdownTick();
			}
		}, 0L, 20L);
	}
	
	private void onCountdownStop()
	{
		Bukkit.getScheduler().cancelTask(this.countdownTaskId);
	}
	
	/**
	 * Called once per second while countdown is running.
	 */
	private void countdownTick()
	{
		//Send a chat message.
		this.chatAll(ChatManager.minigame(
				this.minigame,
				this.countdownFormat.replace("%timeleft%",
						Integer.toString(this.countdownTimeLeft))));
		//If we reached zero.
		if (this.countdownTimeLeft <= 0)
		{
			this.gameStarted = true;
			//Start game.
			this.onGameStart();
			//Stop the countdown task.
			this.onCountdownStop();
		}
		//Decrement the time.
		this.countdownTimeLeft -= 1;
	}
	
	/**
	 * Reseta arena basic things. <b>Calls {@link AdvancedMinigameArena#onReset()} at the end of this function!</b></br> If
	 * you want to extend reset function, override onReset() function.
	 */
	public final void reset()
	{
		this.state = GameState.RESETING;
		this.gameStarted = false;
		//Not many things happeing here. Leaving method for future.
		this.activePlayers.clear();
		//Invoke callback.
		this.onReset();
	}
	
	/**
	 * Called right after the arena resets it's basic things, after {@link AdvancedMinigameArena#reset()} was called.
	 * <b>Don't forget to change arena's state to {@link GameState#WAITING_EMPTY} after reset.</b>
	 */
	public void onReset()
	{
		
	}
	
	/**
	 * Called when countdown stops.
	 */
	public void onCountdownCancelled()
	{
		Bukkit.getScheduler().cancelTask(this.countdownTaskId);
	}
	
	/**
	 * Called each time, a player joins arena and there is not enough players for countdown start.
	 */
	public void onNotEnoughPlayers()
	{
		
	}
	
	/**
	 * Called when game should start its logic. Called when lobby countdown has reached zero and there is enough
	 * players.
	 */
	public void onGameStart()
	{
		
	}
	
	/**
	 * Called when last player lefts the arena. Should call {@link AdvancedMinigameArena#reset()} function if
	 * <code>autoReset</code> is set to <b>false</b> (false by default).
	 */
	public void onGameEnd()
	{
		
	}
	
	/**
	 * Called when players join the arena. Also checks if there are enough players, if so, calls
	 * {@link AdvancedMinigameArena#onGameStart()}. If not, calls {@link AdvancedMinigameArena#onNotEnoughPlayers()}.
	 */
	@Override
	public void onPlayerJoin(final Player player)
	{
		super.onPlayerJoin(player);
		
		this.tryStartCountdown();
		
		this.updateGameState();
	}
	
	/**
	 * Called when player left the arena. If is arena in LOBBY/WAITING_PLAYERS state, and flag
	 * {@link AdvancedMinigameArena#countdownCanCancel} is set to <b>true</b>, stops the countdown.
	 */
	@Override
	public void onPlayerLeft(final Player player)
	{
		super.onPlayerLeft(player);
		
		this.tryStopCountdown();
		
		this.checkForEnd();
		
		this.updateGameState();
	}
	
	/**
	 * Updates game state.
	 */
	private void updateGameState()
	{
		if (!this.gameStarted)
		{
			if (this.playerCount() == 0)
				this.state = GameState.WAITING_EMPTY;
			else
				this.state = GameState.WAITING_PLAYERS;
		}
	}
	
	/**
	 * Checks if there are no players in arena, and if arena is in PLAYING state. If so, the
	 * {@link AdvancedMinigameArena#onGameEnd()}
	 */
	private void checkForEnd()
	{
		if (this.activePlayers.size() == 0 && this.state.isPlaying())
		{
			this.onGameEnd();
			if (this.autoReset)
				this.reset();
		}
	}
	
	@EventHandler
	public void onPlayerRespawn(final PlayerRespawnEvent event)
	{
		if (this.playersCanRespawn)
		{
			//nothing.
		}
		else
		{
			//Kick from arena
			this.onPlayerLeft(event.getPlayer());
		}
	}
	
	public int getMinimalPlayers()
	{
		return this.minimalPlayers;
	}
	
	public void setMinimalPlayers(final int minimalPlayers)
	{
		this.minimalPlayers = minimalPlayers;
	}
	
	public int getCountdownLenght()
	{
		return this.countdownLenght;
	}
	
	public void setCountdownLenght(final int countdownLenght)
	{
		this.countdownLenght = countdownLenght;
	}
	
	public Location getLobbyLocation()
	{
		return this.lobbyLocation;
	}
	
	public void setLobbyLocation(final Location lobbyLocation)
	{
		this.lobbyLocation = lobbyLocation;
	}
	
	public Location getGameSpawn()
	{
		return this.gameSpawn;
	}
	
	public void setGameSpawn(final Location gameSpawn)
	{
		this.gameSpawn = gameSpawn;
	}
	
	public boolean countdownCanCancel()
	{
		return this.countdownCanCancel;
	}
	
	public void setCountdownCanCancel(final boolean countdownCanCancel)
	{
		this.countdownCanCancel = countdownCanCancel;
	}
	
	public boolean shouldTeleportPlayers()
	{
		return this.shouldTeleportPlayers;
	}
	
	public void setShouldTeleportPlayers(final boolean shouldTeleportPlayers)
	{
		this.shouldTeleportPlayers = shouldTeleportPlayers;
	}
	
	public boolean playersCanRespawn()
	{
		return this.playersCanRespawn;
	}
	
	public void setPlayersCanRespawn(final boolean playersCanRespawn)
	{
		this.playersCanRespawn = playersCanRespawn;
	}
	
	public int getCountdownTimeLeft()
	{
		return this.countdownTimeLeft;
	}
	
	public String getCountdownFormat()
	{
		return this.countdownFormat;
	}
	
	public void setCountdownFormat(final String countdownFormat)
	{
		this.countdownFormat = countdownFormat;
	}
	
	public boolean isPlayersCanJoinAfterStart()
	{
		return this.playersCanJoinAfterStart;
	}
	
	public void setPlayersCanJoinAfterStart(
			final boolean playersCanJoinAfterStart)
	{
		this.playersCanJoinAfterStart = playersCanJoinAfterStart;
	}
}
