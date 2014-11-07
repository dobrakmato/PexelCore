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
import eu.matejkormuth.pexel.PexelCore.core.Achievements;
import eu.matejkormuth.pexel.PexelCore.core.Auth;
import eu.matejkormuth.pexel.PexelCore.core.MagicClock;
import eu.matejkormuth.pexel.PexelCore.core.PlayerProfile;
import eu.matejkormuth.pexel.PexelCore.core.Scheduler;
import eu.matejkormuth.pexel.PexelCore.core.StorageEngine;
import eu.matejkormuth.pexel.PexelCore.core.ValidityChecker;
import eu.matejkormuth.pexel.PexelCore.matchmaking.Matchmaking;
import eu.matejkormuth.pexel.PexelCore.matchmaking.MatchmakingSignUpdater;
import eu.matejkormuth.pexel.PexelCore.minigame.Minigame;
import eu.matejkormuth.pexel.PexelCore.util.AsyncWorker;
import eu.matejkormuth.pexel.PexelCore.util.PlayerFreezer;

/**
 * Class used for API calls.
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
    public static final Matchmaking getMatchmaking() {
        return Pexel.instance.matchmaking;
    }
    
    /**
     * Returns {@link Achievements} class.
     * 
     * @return achievements
     */
    public static final Achievements getAchievements() {
        return Pexel.instance.achievementsClient;
    }
    
    /**
     * Returns player freezer.
     * 
     * @return player freezer
     */
    public static final PlayerFreezer getPlayerFreezer() {
        return Pexel.instance.freezer;
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
    public final static Random getRandom() {
        return Pexel.random;
    }
    
    /**
     * Returns event processor.
     * 
     * @return {@link EventProcessor} instance.
     */
    public final static EventProcessor getEventProcessor() {
        return Pexel.instance.eventProcessor;
    }
    
    /**
     * Returns pexel's magic clock class.
     * 
     * @return {@link MagicClock} instance.
     */
    public final static MagicClock getMagicClock() {
        return Pexel.instance.magicClock;
    }
    
    /**
     * Returns pexel's async wokrer instance.
     * 
     * @return {@link AsyncWorker} instance.
     */
    public final static AsyncWorker getAsyncWorker() {
        return Pexel.instance.asyncWorker;
    }
    
    /**
     * Return's hub location.
     * 
     * @return hub lcoation
     */
    public final static Location getHubLocation() {
        return new Location(Bukkit.getWorld("world"), 9.5, 47.5, 262.5);
    }
    
    /**
     * Returns pexel's async wokrer instance.
     * 
     * @return {@link Auth} instance.
     */
    public final static Auth getAuth() {
        return Pexel.instance.auth;
    }
    
    /**
     * Returns pexel's scheduler instance.
     * 
     * @return {@link Scheduler} instance.
     */
    public final static Scheduler getScheduler() {
        return Pexel.instance.scheduler;
    }
    
    /**
     * @return Ban storage
     */
    public final static BanStorage getBans() {
        return Pexel.instance.banStorage;
    }
    
    /**
     * 
     */
    public static MatchmakingSignUpdater getMatchmakingSignUpdater() {
        return Pexel.instance.matchmakingSignUpdater;
    }
    
    /**
     * Registers minigame to Pexel.
     * 
     * @param minigame
     *            specififed minigame to register.
     */
    public static final void registerMinigame(final Minigame minigame) {
        ValidityChecker.checkMinigame(minigame);
        StorageEngine.addMinigame(minigame);
        Pexel.getMatchmaking().registerMinigame(minigame);
    }
    
    /**
     * Tries to register specified object to Pexel. If can't register object, throws RuntimeException.
     * 
     * @param obj
     */
    public static final void register(final Object obj) {
        if (obj instanceof Minigame) {
            Pexel.registerMinigame((Minigame) obj);
        }
        else {
            throw new IllegalArgumentException("Type " + obj.getClass().getSimpleName()
                    + " is not type, that is supported by Pexel.");
        }
    }
}
