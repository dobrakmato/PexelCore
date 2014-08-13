package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dobrakmato.plugins.pexel.TntTag.TntTagArena;

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
	private final Map<String, Minigame>					minigames			= new HashMap<String, Minigame>();
	private final Map<Minigame, List<MinigameArena>>	arenas				= new HashMap<Minigame, List<MinigameArena>>();
	private int											taskId				= 0;
	public static final ServerLocation					QUICKJOIN_LOCATION	= new ServerLocation(
																					"QuickJoin",
																					ServerLocationType.QUICKJOIN);
	
	private final long									matchMakingInterval	= 40L;											//40 ticks = 2 second
	private final List<MatchmakingRequest>				requests			= new ArrayList<MatchmakingRequest>();
	private final List<MatchmakingRequest>				removing			= new ArrayList<MatchmakingRequest>();
	
	public void registerMinigame(final Minigame minigame)
	{
		Log.info("Matchmaking found a new minigame: " + minigame.getName());
		this.minigames.put(minigame.getName(), minigame);
	}
	
	public void registerArena(final TntTagArena arena)
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
		}
		else
		{
			throw new RuntimeException(
					"Can't register arena of minigame before minigame is registered!");
		}
	}
	
	public void registerRequest(final MatchmakingRequest request)
	{
		this.requests.add(request);
		request.updateLocation(Matchmaking.QUICKJOIN_LOCATION);
		
	}
	
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
			
			iterations++;
		}
		
		//Vymaz spracovane poziadavky zo zoznamu.
		for (MatchmakingRequest request : this.removing)
		{
			this.requests.remove(request);
		}
	}
	
	@Override
	public void updateStart(final PexelCore plugin)
	{
		Log.partEnable("Matchmaking");
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
}
