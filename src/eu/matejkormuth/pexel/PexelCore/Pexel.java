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
package eu.matejkormuth.pexel.PexelCore;

import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import eu.matejkormuth.pexel.PexelCore.bans.BanStorage;
import eu.matejkormuth.pexel.PexelCore.core.Auth;
import eu.matejkormuth.pexel.PexelCore.core.MagicClock;
import eu.matejkormuth.pexel.PexelCore.core.PlayerProfile;
import eu.matejkormuth.pexel.PexelCore.core.Scheduler;
import eu.matejkormuth.pexel.PexelCore.core.StorageEngine;
import eu.matejkormuth.pexel.PexelCore.matchmaking.Matchmaking;
import eu.matejkormuth.pexel.PexelCore.util.AsyncWorker;
import eu.matejkormuth.pexel.PexelCore.util.PlayerFreezer;

/**
 * Class for static calls.
 * 
 * @author Mato Kormuth
 * 
 */
public final class Pexel {
    //Pexel plugin.
    private static PexelCore instance;
    //Instance of random. 
    private static Random    random = new Random();
    
    protected final static void initialize(final PexelCore plugin) {
        if (Pexel.instance == null)
            Pexel.instance = plugin;
        else
            throw new RuntimeException("Pexel object already initialized!");
    }
    
    /**
     * Returns the main plugin instance.
     * 
     * @return core
     */
    public static final PexelCore getCore() {
        return Pexel.instance;
    }
    
    /**
     * Returns Matchmaking class.
     * 
     * @return matchmaking
     */
    public static Matchmaking getMatchmaking() {
        return Pexel.instance.matchmaking;
    }
    
    /**
     * Returns player freezer.
     * 
     * @return player freezer
     */
    public static PlayerFreezer getFreezer() {
        return Pexel.instance.freezer;
    }
    
    /**
     * Schedules periodic task. Returns task id.
     * 
     * @param runnable
     *            runnable, that will be executed periodically.
     * @param delay
     *            delay before first execution in server ticks.
     * @param period
     *            period in server ticks.
     * @return task id
     */
    public static int schedule(final Runnable runnable, final long delay,
            final long period) {
        return Pexel.instance.scheduler.scheduleSyncRepeatingTask(runnable, delay,
                period);
    }
    
    /**
     * Cancles task.
     * 
     * @param taskId
     *            task id
     */
    public static void cancelTask(final int taskId) {
        Pexel.instance.scheduler.cancelTask(taskId);
    }
    
    /**
     * Retruns player's profile.
     * 
     * @param player
     * @return profile of specified player
     */
    public PlayerProfile getProfile(final Player player) {
        return StorageEngine.getProfile(player.getUniqueId());
    }
    
    /**
     * Retruns player's profile.
     * 
     * @param player
     * @return profile of specified player
     */
    public PlayerProfile getProfile(final UUID player) {
        return StorageEngine.getProfile(player);
    }
    
    /**
     * Returns instance of {@link Random}.
     * 
     * @return pexel's {@link Random}.
     */
    public static Random getRandom() {
        return Pexel.random;
    }
    
    /**
     * Returns event processor.
     * 
     * @return {@link EventProcessor} instance.
     */
    public static EventProcessor getEventProcessor() {
        return Pexel.instance.eventProcessor;
    }
    
    /**
     * Returns pexel's magic clock class.
     * 
     * @return {@link MagicClock} instance.
     */
    public static MagicClock getMagicClock() {
        return Pexel.instance.magicClock;
    }
    
    /**
     * Returns pexel's async wokrer instance.
     * 
     * @return {@link AsyncWorker} instance.
     */
    public static AsyncWorker getAsyncWorker() {
        return Pexel.instance.asyncWorker;
    }
    
    /**
     * Return's hub location.
     * 
     * @return hub lcoation
     */
    public static Location getHubLocation() {
        return new Location(Bukkit.getWorld("world"), 9.5, 47.5, 262.5);
    }
    
    /**
     * Returns pexel's async wokrer instance.
     * 
     * @return {@link Auth} instance.
     */
    public static Auth getAuth() {
        return Pexel.instance.auth;
    }
    
    /**
     * Returns pexel's scheduler instance.
     * 
     * @return {@link Scheduler} instance.
     */
    public static Scheduler getScheduler() {
        return Pexel.instance.scheduler;
    }
    
    /**
     * @return
     */
    public static BanStorage getBans() {
        return Pexel.instance.banStorage;
    }
}
