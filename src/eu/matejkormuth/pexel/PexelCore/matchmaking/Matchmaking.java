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
package eu.matejkormuth.pexel.PexelCore.matchmaking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.util.com.google.gson.Gson;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import eu.matejkormuth.pexel.PexelCore.Pexel;
import eu.matejkormuth.pexel.PexelCore.arenas.MinigameArena;
import eu.matejkormuth.pexel.PexelCore.chat.ChatManager;
import eu.matejkormuth.pexel.PexelCore.core.Log;
import eu.matejkormuth.pexel.PexelCore.core.Party;
import eu.matejkormuth.pexel.PexelCore.core.StorageEngine;
import eu.matejkormuth.pexel.PexelCore.core.Updatable;
import eu.matejkormuth.pexel.PexelCore.core.UpdatedParts;
import eu.matejkormuth.pexel.PexelCore.minigame.Minigame;
import eu.matejkormuth.pexel.PexelCore.util.ServerLocation;
import eu.matejkormuth.pexel.PexelCore.util.ServerLocationType;

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
    /**
     * List of players in matchmaking.
     */
    private final List<Player>                       players             = new ArrayList<Player>();
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
    private HttpHandler                              httphandler;
    
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
        boolean safe = true;
        String playername = null;
        for (Player p : request.getPlayers()) {
            if (this.players.contains(p)) {
                p.sendMessage(ChatManager.error("Can't register matchmaking request while in queue with another one! Type /leave to leave all requests."));
                safe = false;
                if (playername == null) {
                    playername = p.getDisplayName();
                }
                else {
                    playername += ", " + p.getDisplayName();
                }
            }
        }
        
        if (safe) {
            request.tries = 0;
            this.requests.add(request);
            this.players.addAll(request.getPlayers());
        }
        else {
            for (Player p : request.getPlayers()) {
                p.sendMessage(ChatManager.error("Matchmaking failed! Player(s) '"
                        + playername + ChatColor.RED
                        + "' are in another matchmaking request!"));
            }
        }
    }
    
    /**
     * Tries to find ideal matches for requests.
     */
    public void makeMatches() {
        this.removing.clear();
        
        int iterations = 0;
        int maxIterations = 256;
        int playercount = 0;
        int matchcount = 0;
        
        //Pokus sa sparovat vsetky poziadavky.
        for (MatchmakingRequest request : this.requests) {
            for (Player p : request.getPlayers()) {
                p.sendMessage(ChatColor.GOLD + "Finding best matches ("
                        + (request.tries + 1) + ")... Please, be patient!");
            }
            
            request.tries++;
            if (request.tries >= 20) {
                for (Player p : request.getPlayers()) {
                    p.sendMessage(ChatManager.error("Matchmaking failed!"));
                }
                this.removing.add(request);
            }
            
            //Ak sme neprekrocili limit.
            if (iterations > maxIterations)
                break;
            
            if (request.getGame() != null) {
                //The best function.
                this.makeMatchesBySpecifiedMinigameAndMminigameArenaFromMatchMakingRequest_Version_1_0_0_0_1(request);
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
            playercount += request.playerCount();
            matchcount++;
            this.players.removeAll(request.getPlayers());
            this.requests.remove(request);
        }
        
        if (playercount != 0)
            Log.info("[MM] Processed " + playercount + " players in " + matchcount
                    + " matches! " + this.requests.size() + " requests left.");
    }
    
    private void makeMatchesBySpecifiedMinigameAndMminigameArenaFromMatchMakingRequest_Version_1_0_0_0_1(
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
    
    public HttpHandler getHttpHandler() {
        @SuppressWarnings("unused")
        class JSONArena {
            public String   name;
            public String   minigame;
            public String[] players;
            public String   state;
            public int      maxPlayers;
        }
        
        if (this.httphandler == null) {
            this.httphandler = new HttpHandler() {
                @Override
                public void handle(final HttpExchange conn) throws IOException {
                    List<JSONArena> arenas = new ArrayList<JSONArena>();
                    for (List<MinigameArena> minigameArenas : Matchmaking.this.arenas.values()) {
                        for (MinigameArena arena : minigameArenas) {
                            JSONArena a = new JSONArena();
                            a.name = arena.getName();
                            a.minigame = arena.getMinigame().getName();
                            a.maxPlayers = arena.getMaximumSlots();
                            a.state = arena.getState().name();
                            a.players = new String[arena.getPlayerCount()];
                            int i = 0;
                            for (Player p : arena.getPlayers()) {
                                a.players[i] = p.getName() + "/"
                                        + p.getUniqueId().toString();
                                i++;
                            }
                        }
                    }
                    String response = new Gson().toJson(arenas.toArray());
                    conn.sendResponseHeaders(200, response.length());
                    conn.getResponseBody().write(response.getBytes());
                    conn.close();
                }
            };
        }
        return this.httphandler;
    }
}
