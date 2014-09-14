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
package me.dobrakmato.plugins.pexel.PexelCore.matchmaking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dobrakmato.plugins.pexel.PexelCore.Pexel;
import me.dobrakmato.plugins.pexel.PexelCore.arenas.MinigameArena;
import me.dobrakmato.plugins.pexel.PexelCore.chat.ChatManager;
import me.dobrakmato.plugins.pexel.PexelCore.core.Log;
import me.dobrakmato.plugins.pexel.PexelCore.core.Party;
import me.dobrakmato.plugins.pexel.PexelCore.core.StorageEngine;
import me.dobrakmato.plugins.pexel.PexelCore.core.Updatable;
import me.dobrakmato.plugins.pexel.PexelCore.core.UpdatedParts;
import me.dobrakmato.plugins.pexel.PexelCore.minigame.Minigame;
import me.dobrakmato.plugins.pexel.PexelCore.util.ServerLocation;
import me.dobrakmato.plugins.pexel.PexelCore.util.ServerLocationType;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Class used for matchmaking.
 * 
 * @author Mato Kormuth
 * 
 */
public class Matchmaking implements Updatable {
    /**
     * List of registered minigames.
     */
    private final Map<String, Minigame>              minigames           = new HashMap<String, Minigame>();
    /**
     * List of registered arenas.
     */
    private final Map<Minigame, List<MinigameArena>> arenas              = new HashMap<Minigame, List<MinigameArena>>();
    private int                                      taskId              = 0;
    /**
     * Matchmaking server location.
     */
    public static final ServerLocation               QUICKJOIN_LOCATION  = new ServerLocation(
                                                                                 "QuickJoin",
                                                                                 ServerLocationType.QUICKJOIN);
    /**
     * How often should server try to find match.
     */
    private final long                               matchMakingInterval = 40L;                                         //40 ticks = 2 second
    /**
     * Pending matchmaking request.
     */
    private final List<MatchmakingRequest>           requests            = new ArrayList<MatchmakingRequest>();
    /**
     * List of request being removed in this iteration.
     */
    private final List<MatchmakingRequest>           removing            = new ArrayList<MatchmakingRequest>();
    
    /**
     * Registers minigame to Pexel matchmaking.
     * 
     * @param minigame
     *            minigame
     */
    public void registerMinigame(final Minigame minigame) {
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
    public void registerArena(final MinigameArena arena) {
        if (this.minigames.containsValue(arena.getMinigame())) {
            Log.info("Matchmaking found a new arena: " + arena.getName() + "-"
                    + arena.getMinigame().getName());
            if (this.arenas.containsKey(arena.getMinigame()))
                this.arenas.get(arena.getMinigame()).add(arena);
            else {
                List<MinigameArena> list = new ArrayList<MinigameArena>();
                list.add(arena);
                this.arenas.put(arena.getMinigame(), list);
            }
            StorageEngine.addArena(arena);
        }
        else {
            throw new RuntimeException(
                    "Can't register arena of minigame before minigame is registered!");
        }
    }
    
    /**
     * Registers new matchmaking request.
     * 
     * @param request
     *            the request
     */
    public void registerRequest(final MatchmakingRequest request) {
        this.requests.add(request);
    }
    
    /**
     * Tries to find ideal matches for requests.
     */
    public void makeMatches() {
        this.removing.clear();
        
        int iterations = 0;
        int maxIterations = 256;
        
        //Pokus sa sparovat vsetky poziadavky.
        for (MatchmakingRequest request : this.requests) {
            //Ak sme neprekrocili limit.
            if (iterations < maxIterations)
                break;
            
            if (request.getGame() != null) {
                this.makeMatchesBySpecifiedMinigameAndMminigameArena(request);
            }
            else {
                List<MinigameArena> minigame_arenas = this.arenas.get(request.getMinigame());
                
                for (MinigameArena arena : minigame_arenas) {
                    // If is not empty, and there is a place for them
                    if (!arena.empty() && arena.canJoin(request.playerCount())) {
                        // Connect all of them
                        for (Player player : request.getPlayers())
                            arena.onPlayerJoin(player);
                        //Odstran request zo zoznamu.
                        this.removing.add(request);
                        break;
                    }
                }
                
                for (MinigameArena arena : minigame_arenas) {
                    // If is not empty, and there is a place for them
                    if (arena.canJoin(request.playerCount())) {
                        // Connect all of them
                        for (Player player : request.getPlayers())
                            arena.onPlayerJoin(player);
                        // Remove request from queue.
                        this.removing.add(request);
                        break;
                    }
                }
            }
            
            iterations++;
        }
        
        //Vymaz spracovane poziadavky zo zoznamu.
        for (MatchmakingRequest request : this.removing) {
            this.requests.remove(request);
        }
    }
    
    private void makeMatchesBySpecifiedMinigameAndMminigameArena(
            final MatchmakingRequest request) {
        for (MinigameArena arena : this.arenas.get(request.getMinigame())) {
            // If is not empty, and there is a place for them
            if (!arena.empty() && arena.canJoin(request.playerCount())) {
                // Connect all of them
                for (Player player : request.getPlayers())
                    arena.onPlayerJoin(player);
                // Remove request from queue.
                this.removing.add(request);
                break;
            }
        }
        
        for (MinigameArena arena : this.arenas.get(request.getMinigame())) {
            // If is not empty, and there is a place for them
            if (arena.canJoin(request.playerCount())) {
                // Connect all of them
                for (Player player : request.getPlayers())
                    arena.onPlayerJoin(player);
                // Remove request from queue.
                this.removing.add(request);
                break;
            }
        }
    }
    
    @Override
    public void updateStart() {
        Log.partEnable("Matchmaking");
        UpdatedParts.registerPart(this);
        this.taskId = Pexel.getScheduler().scheduleSyncRepeatingTask(new Runnable() {
            @Override
            public void run() {
                Matchmaking.this.makeMatches();
            }
        }, 0, this.matchMakingInterval);
    }
    
    @Override
    public void updateStop() {
        Log.partDisable("Matchmaking");
        Pexel.getScheduler().cancelTask(this.taskId);
    }
    
    public void processSign(final String[] lines, final Player player) {
        String minigame = lines[1];
        
        // Currently not used.
        //String map = lines[2];
        //String arena = lines[3];
        if (StorageEngine.getProfile(player.getUniqueId()).getParty() != null) {
            Party party = StorageEngine.getProfile(player.getUniqueId()).getParty();
            if (party.isOwner(player)) {
                for (Player p : party.getPlayers())
                    p.sendMessage(ChatColor.YELLOW + "Your party joined matchmaking!");
                this.registerRequest(party.toRequest(
                        StorageEngine.getMinigame(minigame), null));
            }
            else {
                player.sendMessage(ChatManager.error("You cannot join games while you are in party!"));
            }
        }
        else {
            player.sendMessage(ChatColor.YELLOW + "Your have joined matchmaking!");
            MatchmakingRequest request = new MatchmakingRequest(Arrays.asList(player),
                    StorageEngine.getMinigame(minigame), null);
            this.registerRequest(request);
        }
    }
}
