package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Minigame arena.
 * 
 * @author Mato Kormuth
 * 
 */
public class MinigameArena extends ProtectedArea implements MatchmakingGame
{
	/**
	 * Number of slots.
	 */
	protected int					slots;
	/**
	 * The actual state of the arena.
	 */
	protected GameState				state				= GameState.WAITING_PLAYERS;
	/**
	 * The game mode that players should get, when they join the game.
	 */
	protected GameMode				defaultGameMode		= GameMode.ADVENTURE;
	/**
	 * List of active players in arena.
	 */
	protected final List<Player>	activePlayers		= new ArrayList<Player>();
	/**
	 * List of spectating players in arena.
	 */
	protected final List<Player>	spectatingPlayers	= new ArrayList<Player>();
	/**
	 * Reference to minigame.
	 */
	protected final Minigame		minigame;
	/**
	 * Server location.
	 */
	protected ServerLocation		serverLocation		= new ServerLocation(
																"Arena: "
																		+ this.areaName,
																ServerLocationType.MINIGAME);
	
	public MinigameArena(final Minigame minigame, final String arenaName,
			final Region region, final int slots)
	{
		super(minigame.getName() + "_" + arenaName, region);
		this.minigame = minigame;
		this.slots = slots;
	}
	
	/**
	 * Sends a chat message to all player in arena.
	 * 
	 * @param msg
	 */
	public void chatAll(final String msg)
	{
		for (Player p : this.activePlayers)
			p.sendMessage(msg);
	}
	
	@Override
	public int getFreeSlots()
	{
		return this.slots - this.activePlayers.size();
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
		return this.activePlayers;
	}
	
	@Override
	public int playerCount()
	{
		return this.activePlayers.size();
	}
	
	@Override
	public boolean canJoin()
	{
		return this.getFreeSlots() >= 1
				&& (this.state == GameState.WAITING_PLAYERS
						|| this.state == GameState.WAITING_EMPTY || this.state == GameState.PLAYING_CANJOIN);
	}
	
	@Override
	public boolean canJoin(final int count)
	{
		return this.getFreeSlots() >= count
				&& (this.state == GameState.WAITING_PLAYERS
						|| this.state == GameState.WAITING_EMPTY || this.state == GameState.PLAYING_CANJOIN);
	}
	
	@Override
	public void onPlayerJoin(final Player player)
	{
		this.activePlayers.add(player);
		player.setGameMode(this.defaultGameMode);
	}
	
	@Override
	public void onPlayerLeft(final Player player)
	{
		this.activePlayers.remove(player);
		//this.setSpectating(player, false);
	}
	
	/**
	 * Plays sound for all players in arena.
	 * 
	 * @param sound
	 *            sound to play
	 * @param volume
	 *            volume
	 * @param pitch
	 *            pitch
	 */
	public void playSoundAll(final Sound sound, final float volume,
			final float pitch)
	{
		for (Player p : this.activePlayers)
			p.playSound(p.getLocation(), sound, volume, pitch);
	}
	
	/**
	 * Sets spectating mode for player in this arena.
	 * 
	 * @param player
	 *            player
	 * @param spectating
	 *            the value if the player should be spectating or not.
	 */
	public void setSpectating(final Player player, final boolean spectating)
	{
		if (spectating)
		{
			if (!StorageEngine.getProfile(player.getUniqueId()).isSpectating())
			{
				player.sendMessage(ChatManager.success("You are now spectating!"));
				StorageEngine.getProfile(player.getUniqueId()).setSpectating(
						true);
				player.getInventory().clear();
				player.getInventory().addItem(
						ItemUtils.namedItemStack(Material.COMPASS,
								ChatColor.YELLOW + "Spectating", null));
				player.setGameMode(GameMode.ADVENTURE);
				player.addPotionEffect(new PotionEffect(
						PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
				player.addPotionEffect(new PotionEffect(
						PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
				player.setAllowFlight(true);
				player.setFlying(true);
				this.spectatingPlayers.add(player);
			}
			else
			{
				//Player is already spectating.
				Log.warn("Player '" + player.getName()
						+ "' can't be moved to spectating mode by game '"
						+ this.getMinigame().getName()
						+ "': Player is already in spectating mode!");
			}
		}
		else
		{
			if (StorageEngine.getProfile(player.getUniqueId()).isSpectating())
			{
				player.sendMessage(ChatManager.success("You are no longer spectating!"));
				StorageEngine.getProfile(player.getUniqueId()).setSpectating(
						false);
				player.getInventory().clear();
				player.setGameMode(this.defaultGameMode);
				player.removePotionEffect(PotionEffectType.NIGHT_VISION);
				player.removePotionEffect(PotionEffectType.INVISIBILITY);
				player.setAllowFlight(false);
				player.setFlying(false);
				this.spectatingPlayers.remove(player);
			}
			else
			{
				//Player is not spectating.
				Log.warn("Player '" + player.getName()
						+ "' can't be moved from spectating mode by game '"
						+ this.getMinigame().getName()
						+ "': Player is not in spectating mode!");
			}
		}
	}
	
	/**
	 * Returns whatever is arena empty.
	 * 
	 * @return
	 */
	public boolean empty()
	{
		return this.activePlayers.size() == 0;
	}
	
	/**
	 * Kicks all players from arena.
	 */
	public void kickAll()
	{
		for (Player p : this.activePlayers)
			this.onPlayerLeft(p);
	}
	
	/**
	 * Sends a message to all players and kicks them.
	 * 
	 * @param message
	 *            message to send
	 */
	public void kickAll(final String message)
	{
		for (Player p : this.activePlayers)
		{
			p.sendMessage(message);
			this.onPlayerLeft(p);
		}
	}
	
	/**
	 * Return minigame running in this arena.
	 * 
	 * @return
	 */
	public Minigame getMinigame()
	{
		return this.minigame;
	}
	
	@Override
	public ServerLocation getServerLocation()
	{
		return this.serverLocation;
	}
	
	public void setSlots(final int slots)
	{
		this.slots = slots;
	}
}
