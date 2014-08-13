package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.Random;

import org.bukkit.Bukkit;

/**
 * Class for static calls.
 * 
 * @author Mato Kormuth
 * 
 */
public final class Pexel
{
	//Pexel plugin.
	private static PexelCore	plugin;
	//Instance of random.
	private static Random		random	= new Random();
	
	protected final static void initialize(final PexelCore plugin)
	{
		Pexel.plugin = plugin;
	}
	
	public static final PexelCore getCore()
	{
		return Pexel.plugin;
	}
	
	public static Matchmaking getMatchmaking()
	{
		return Pexel.plugin.matchmaking;
	}
	
	public static PlayerFreezer getFreezer()
	{
		return Pexel.plugin.freezer;
	}
	
	public static int schedule(final Runnable runnable, final long delay,
			final long period)
	{
		return Bukkit.getScheduler().scheduleSyncRepeatingTask(Pexel.plugin,
				runnable, delay, period);
	}
	
	public static Random getRandom()
	{
		return Pexel.random;
	}
	
	public static EventProcessor getEventProcessor()
	{
		return Pexel.plugin.eventProcessor;
	}
}
