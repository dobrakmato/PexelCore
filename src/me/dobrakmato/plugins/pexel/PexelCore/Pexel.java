// @formatter:off
/*
 * Pexel Project - Minecraft minigame server platform. 
 * Copyright (C) 2014 Matej Kormuth <http://www.matejkormuth.eu>
 * 
 * This file is part of Pexel.
 * 
 * Pexel is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * Pexel is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */
// @formatter:on
package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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
	public static void cancelTask(final int taskId)
	{
		Bukkit.getScheduler().cancelTask(taskId);
	}
	
	/**
	 * Retruns player's profile.
	 * 
	 * @param player
	 * @return
	 */
	public PlayerProfile getProfile(final Player player)
	{
		return StorageEngine.getProfile(player.getUniqueId());
	}
	
	/**
	 * Retruns player's profile.
	 * 
	 * @param player
	 * @return
	 */
	public PlayerProfile getProfile(final UUID player)
	{
		return StorageEngine.getProfile(player);
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
	
	/**
	 * Returns event processor.
	 * 
	 * @return
	 */
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
	
	/**
	 * Returns pexel's async wokrer instance.
	 */
	public static AsyncWorker getAsyncWorker()
	{
		return Pexel.plugin.asyncWorker;
	}
	
	/**
	 * Return's lobby location.
	 * 
	 * @return lobby lcoation
	 */
	public static Location getHubLocation()
	{
		return new Location(Bukkit.getWorld("world"), 9.5, 47.5, 262.5);
	}
}
