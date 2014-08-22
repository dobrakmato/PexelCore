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
	
	/**
	 * Returns the main plugin instance.
	 * 
	 * @return
	 */
	public static final PexelCore getCore()
	{
		return Pexel.plugin;
	}
	
	/**
	 * Returns Matchmaking class.
	 * 
	 * @return
	 */
	public static Matchmaking getMatchmaking()
	{
		return Pexel.plugin.matchmaking;
	}
	
	/**
	 * Returns player freezer.
	 * 
	 * @return
	 */
	public static PlayerFreezer getFreezer()
	{
		return Pexel.plugin.freezer;
	}
	
	/**
	 * Schedules periodic task. Returns task id.
	 * 
	 * @param runnable
	 * @param delay
	 * @param period
	 * @return task id
	 */
	public static int schedule(final Runnable runnable, final long delay,
			final long period)
	{
		return Bukkit.getScheduler().scheduleSyncRepeatingTask(Pexel.plugin,
				runnable, delay, period);
	}
	
	/**
	 * Cancles task.
	 * 
	 * @param taskId
	 */
	public static void cancledTask(final int taskId)
	{
		Bukkit.getScheduler().cancelTask(taskId);
	}
	
	/**
	 * Returns instance of {@link Random}.
	 * 
	 * @return
	 */
	public static Random getRandom()
	{
		return Pexel.random;
	}
	
	public static EventProcessor getEventProcessor()
	{
		return Pexel.plugin.eventProcessor;
	}
	
	/**
	 * Returns pexel's magic clock class.
	 * 
	 * @return
	 */
	public static MagicClock getMagicClock()
	{
		return Pexel.plugin.magicClock;
	}
}
