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

import java.util.Arrays;
import java.util.List;

import me.dobrakmato.plugins.pexel.PexelCore.core.StorageEngine;
import me.dobrakmato.plugins.pexel.PexelCore.minigame.Minigame;
import me.dobrakmato.plugins.pexel.PexelCore.util.ServerLocation;

import org.bukkit.entity.Player;

/**
 * Request for matchmaking.
 * 
 * @author Mato Kormuth
 * 
 */
public class MatchmakingRequest {
    /**
     * List of player in request.
     */
    private final List<Player>    players;
    /**
     * The minigame that the players want to play.
     */
    private final Minigame        minigame;
    /**
     * Arena that players want to play.
     */
    private final MatchmakingGame game;
    
    public MatchmakingRequest(final List<Player> players, final Minigame minigame,
            final MatchmakingGame game) {
        this.players = players;
        this.minigame = minigame;
        this.game = game;
    }
    
    /**
     * Creates new request with random game and arena.
     * 
     * @param player
     *            player
     * @return
     */
    public static MatchmakingRequest create(final Player player) {
        return new MatchmakingRequest(Arrays.asList(player), null, null);
    }
    
    /**
     * Creates new request with random game and arena.
     * 
     * @param player
     *            players
     * @return
     */
    public static MatchmakingRequest create(final Player... player) {
        return new MatchmakingRequest(Arrays.asList(player), null, null);
    }
    
    /**
     * Creates new request with specified game and random arena.
     * 
     * @param player
     *            player
     * @return
     */
    public static MatchmakingRequest create(final Player player, final Minigame minigame) {
        return new MatchmakingRequest(Arrays.asList(player), minigame, null);
    }
    
    /**
     * Creates new request with specified game and random arena.
     * 
     * @param player
     *            players
     * @return
     */
    public static MatchmakingRequest create(final Minigame minigame,
            final Player... player) {
        return new MatchmakingRequest(Arrays.asList(player), minigame, null);
    }
    
    /**
     * Updates server server location ({@link ServerLocation}) of players in request. Does not teleport players.
     * 
     * @param location
     * @deprecated
     */
    @Deprecated
    public void updateServerLocation(final ServerLocation location) {
        for (Player p : this.players)
            StorageEngine.getProfile(p.getUniqueId()).setServerLocation(location);
    }
    
    /**
     * Returns list of players in this request.
     * 
     * @return
     */
    public List<Player> getPlayers() {
        return this.players;
    }
    
    /**
     * Returns minigame of this request.
     * 
     * @return
     */
    public Minigame getMinigame() {
        return this.minigame;
    }
    
    /**
     * Returns arena of this request.
     * 
     * @return
     */
    public MatchmakingGame getGame() {
        return this.game;
    }
    
    /**
     * Returns player count in this request.
     * 
     * @return
     */
    public int playerCount() {
        return this.players.size();
    }
}
