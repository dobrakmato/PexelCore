package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Class used for matchmaking.
 * 
 * @author Mato Kormuth
 * 
 */
public class Matchmaking implements UpdatedPart
{
	/**
	 * List of registered minigames.
	 */
	private final Map<String, Minigame>					minigames			= new HashMap<String, Minigame>();
	/**
	 * List of registered arenas.
	 */
	private final Map<Minigame, List<MinigameArena>>	arenas				= new HashMap<Minigame, List<MinigameArena>>();
	private int											taskId				= 0;
	/**
	 * Matchmaking server location.
	 */
	public static final ServerLocation					QUICKJOIN_LOCATION	= new ServerLocation(
																					"QuickJoin",
																					ServerLocationType.QUICKJOIN);
	/**
	 * How often should server try to find match.
	 */
	private final long									matchMakingInterval	= 40L;											//40 ticks = 2 second
	/**
	 * Pending matchmaking request.
	 */
	private final List<MatchmakingRequest>				requests			= new ArrayList<MatchmakingRequest>();
	/**
	 * List of request being removed in this iteration.
	 */
	private final List<MatchmakingRequest>				removing			= new ArrayList<MatchmakingRequest>();
	
	/**
	 * Registers minigame to Pexel matchmaking.
	 * 
	 * @param minigame
	 *            minigame
	 */
	public void registerMinigame(final Minigame minigame)
	{
		Log.info("Matchmaking found a new minigame: " + minigame.getName());
		this.minigames.put(minigame.getName(), minigame);
		StorageEngine.addMinigame(minigame);
	}
	
	/**
	 * Registers arena to Pexel matchmaking.
	 * 
	 * @param arena
	 *            minigame arena
	 */
	public void registerArena(final MinigameArena arena)
	{
		if (this.minigames.containsValue(arena.getMinigame()))
		{
			Log.info("Matchmaking found a new arena: " + arena.getName() + "-"
					+ arena.getMinigame().getName());
			if (this.arenas.containsKey(arena.getMinigame()))
				this.arenas.get(arena.getMinigame()).add(arena);
			else
			{
				List<MinigameArena> list = new ArrayList<MinigameArena>();
				list.add(arena);
				this.arenas.put(arena.getMinigame(), list);
			}
			StorageEngine.addArena(arena);
		}
		else
		{
			throw new RuntimeException(
					"Can't register arena of minigame before minigame is registered!");
		}
	}
	
	/**
	 * Registers new matchmaking request.
	 * 
	 * @param request
	 */
	public void registerRequest(final MatchmakingRequest request)
	{
		this.requests.add(request);
		request.updateServerLocation(Matchmaking.QUICKJOIN_LOCATION);
	}
	
	/**
	 * Tries to find ideal matches for requests.
	 */
	public void makeMatches()
	{
		this.removing.clear();
		
		int iterations = 0;
		int maxIterations = 256;
		
		//Pokus sa sparovat vsetky poziadavky.
		for (MatchmakingRequest request : this.requests)
		{
			//Ak sme neprekrocili limit.
			if (iterations < maxIterations)
				break;
			
			if (request.getGame() != null)
			{
				this.makeMatchesBySpecifiedMinigameAndMminigameArena(request);
			}
			else
			{
				List<MinigameArena> minigame_arenas = this.arenas.get(request.getMinigame());
				
				for (MinigameArena arena : minigame_arenas)
				{
					//Ak nie je prazdna a je v nej pre nich miesto.
					if (!arena.empty() && arena.canJoin(request.playerCount()))
					{
						//Pripoj kazdeho.
						for (Player player : request.getPlayers())
							arena.onPlayerJoin(player);
						//Odstran request zo zoznamu.
						this.removing.add(request);
						break;
					}
				}
				
				for (MinigameArena arena : minigame_arenas)
				{
					//Ak nie je prazdna a je v nej pre nich miesto.
					if (arena.canJoin(request.playerCount()))
					{
						//Pripoj kazdeho.
						for (Player player : request.getPlayers())
							arena.onPlayerJoin(player);
						//Odstran request zo zoznamu.
						this.removing.add(request);
						break;
					}
				}
			}
			
			iterations++;
		}
		
		//Vymaz spracovane poziadavky zo zoznamu.
		for (MatchmakingRequest request : this.removing)
		{
			this.requests.remove(request);
		}
	}
	
	private void makeMatchesBySpecifiedMinigameAndMminigameArena(final MatchmakingRequest request)
	{
		for (MinigameArena arena : this.arenas.get(request.getGame()))
		{
			//Ak nie je prazdna a je v nej pre nich miesto.
			if (!arena.empty() && arena.canJoin(request.playerCount()))
			{
				//Pripoj kazdeho.
				for (Player player : request.getPlayers())
					arena.onPlayerJoin(player);
				//Odstran request zo zoznamu.
				this.removing.add(request);
				break;
			}
		}
		
		for (MinigameArena arena : this.arenas.get(request.getGame()))
		{
			//Ak nie je prazdna a je v nej pre nich miesto.
			if (arena.canJoin(request.playerCount()))
			{
				//Pripoj kazdeho.
				for (Player player : request.getPlayers())
					arena.onPlayerJoin(player);
				//Odstran request zo zoznamu.
				this.removing.add(request);
				break;
			}
		}
	}
	
	@Override
	public void updateStart(final PexelCore plugin)
	{
		Log.partEnable("Matchmaking");
		UpdatedParts.registerPart(this);
		this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin,
				new Runnable() {
					@Override
					public void run()
					{
						Matchmaking.this.makeMatches();
					}
				}, 0, this.matchMakingInterval);
	}
	
	@Override
	public void updateStop()
	{
		Log.partDisable("Matchmaking");
		Bukkit.getScheduler().cancelTask(this.taskId);
	}
	
	public void processSign(final String[] lines, final Player player)
	{
		String minigame = lines[1];
		//String map = lines[2];
		//String arena = lines[3];
		
		MatchmakingRequest request = new MatchmakingRequest(
				Arrays.asList(player), StorageEngine.getMinigame(minigame),
				null);
		this.registerRequest(request);
	}
}
