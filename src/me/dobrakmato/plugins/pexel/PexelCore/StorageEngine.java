package me.dobrakmato.plugins.pexel.PexelCore;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

/**
 * All data of plugin is stored in this class.
 * 
 * @author Mato Kormuth
 * 
 */
public class StorageEngine
{
	private static final Map<UUID, PlayerProfile>	profiles	= new HashMap<UUID, PlayerProfile>();
	private static final Map<String, Minigame>		minigames	= new HashMap<String, Minigame>();
	private static final Map<String, ProtectedArea>	areas		= new HashMap<String, ProtectedArea>();
	private static final Map<String, MinigameArena>	arenas		= new HashMap<String, MinigameArena>();
	@SuppressWarnings("rawtypes")
	private static final Map<String, Class>			aliases		= new HashMap<String, Class>();
	private static final Map<String, Lobby>			lobbies		= new HashMap<String, Lobby>();
	private static boolean							initialized	= false;
	
	public static void initialize(final PexelCore core)
	{
		if (!StorageEngine.initialized)
			StorageEngine.initialized = true;
	}
	
	public static List<UUID> getFriends(final Player player)
	{
		return StorageEngine.profiles.get(player.getUniqueId()).getFriends();
	}
	
	public static List<UUID> getFoes(final Player player)
	{
		return StorageEngine.profiles.get(player.getUniqueId()).getFoes();
	}
	
	protected static Map<String, ProtectedArea> getAreas()
	{
		return StorageEngine.areas;
	}
	
	/**
	 * Returns profile of specified player.
	 * 
	 * @param player
	 * @return
	 */
	public static PlayerProfile getProfile(final UUID player)
	{
		return profiles.get(player);
	}
	
	public static Minigame getMinigame(final String name)
	{
		return StorageEngine.minigames.get(name);
	}
	
	public static void addMinigame(final Minigame minigame)
	{
		StorageEngine.minigames.put(minigame.getName(), minigame);
	}
	
	public static void addArena(final MinigameArena arena)
	{
		StorageEngine.arenas.put(arena.getName(), arena);
		StorageEngine.areas.put(arena.getName(), arena);
	}
	
	public static int getMinigameArenasCount()
	{
		return StorageEngine.arenas.size();
	}
	
	public static int getMinigamesCount()
	{
		return StorageEngine.minigames.size();
	}
	
	protected static Map<String, Minigame> getMinigames()
	{
		return StorageEngine.minigames;
	}
	
	protected static Map<String, MinigameArena> getArenas()
	{
		return StorageEngine.arenas;
	}
	
	public static MinigameArena getArena(final String arenaName)
	{
		return StorageEngine.arenas.get(arenaName);
	}
	
	@SuppressWarnings("rawtypes")
	public static void registerArenaAlias(final Class arenaClass,
			final String alias)
	{
		StorageEngine.aliases.put(alias, arenaClass);
	}
	
	@SuppressWarnings("rawtypes")
	public static Class getByAlias(final String alias)
	{
		return StorageEngine.aliases.get(alias);
	}
	
	@SuppressWarnings("rawtypes")
	protected static Map<String, Class> getAliases()
	{
		return StorageEngine.aliases;
	}
	
	public static void addLobby(final Lobby lobby)
	{
		StorageEngine.lobbies.put(lobby.getName(), lobby);
	}
	
	public static Lobby getLobby(final String lobbyName)
	{
		return StorageEngine.lobbies.get(lobbyName);
	}
	
	/**
	 * Saves player's profile to file.
	 * 
	 * @param uniqueId
	 */
	public static void saveProfile(final UUID uniqueId)
	{
		Log.info("Saving profile for " + uniqueId.toString() + " to disk...");
		StorageEngine.profiles.get(uniqueId).save(Paths.playerProfile(uniqueId));
	}
	
	/**
	 * Loads player profile from disk or creates an empty one.
	 * 
	 * @param uniqueId
	 */
	public static void loadProfile(final UUID uniqueId)
	{
		File f = new File(Paths.playerProfile(uniqueId));
		if (f.exists())
		{
			Log.info("Load profile for " + uniqueId + "...");
			StorageEngine.profiles.put(uniqueId,
					PlayerProfile.load(Paths.playerProfile(uniqueId)));
		}
		else
		{
			Log.info("Creating new profile for " + uniqueId.toString());
			StorageEngine.profiles.put(uniqueId, new PlayerProfile(uniqueId));
		}
	}
}
