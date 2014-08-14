package me.dobrakmato.plugins.pexel.PexelCore;

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
}
