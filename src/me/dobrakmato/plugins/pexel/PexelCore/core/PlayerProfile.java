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
package me.dobrakmato.plugins.pexel.PexelCore.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.dobrakmato.plugins.pexel.PexelCore.util.ParticleEffect2;
import me.dobrakmato.plugins.pexel.PexelCore.util.ServerLocation;
import me.dobrakmato.plugins.pexel.PexelCore.util.ServerLocationType;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;

/**
 * Object for storing player's friends and unfriends.
 * 
 * @author Mato Kormuth
 * 
 */
public class PlayerProfile {
    /**
     * Player's UUID.
     */
    protected final UUID                   player;
    /**
     * Player's friends.
     */
    protected final List<UUID>             friends        = new ArrayList<UUID>();
    /**
     * Player's foes.
     */
    protected final List<UUID>             foes           = new ArrayList<UUID>();
    /**
     * Player's settings.
     */
    protected final Map<Settings, Boolean> settings       = new HashMap<Settings, Boolean>();
    
    /**
     * Spectating status.
     */
    protected boolean                      spectating     = false;
    /**
     * Player's location.
     */
    protected ServerLocation               serverLocation = new ServerLocation(
                                                                  "Main Lobby",
                                                                  ServerLocationType.LOBBY);
    //Dont mind this one...
    private ParticleEffect2                particleType;
    
    /**
     * Last known name of this player.
     */
    protected String                       lastKnownName  = "";
    /**
     * Amount of player's points (probably in-game currency or whatever).
     */
    protected int                          coins          = 0;
    protected int                          warnCount      = 0;
    
    /**
     * Creates player profile from Player object.
     * 
     * @param player
     */
    public PlayerProfile(final Player player) {
        this.player = player.getUniqueId();
    }
    
    /**
     * Creates player profile from UUID.
     * 
     * @param player
     */
    public PlayerProfile(final UUID player) {
        this.player = player;
    }
    
    /**
     * Adds friend.
     * 
     * @param player
     */
    public void addFriend(final UUID player) {
        this.friends.add(player);
    }
    
    /**
     * Removes friend.
     * 
     * @param player
     */
    public void removeFriend(final UUID player) {
        this.friends.remove(player);
    }
    
    public void addFoe(final UUID player) {
        this.foes.add(player);
    }
    
    public void removeFoe(final UUID player) {
        this.foes.remove(player);
    }
    
    /**
     * Returns UUID of profile.
     * 
     * @return
     */
    public UUID getUniqueId() {
        return this.player;
    }
    
    /**
     * Return list of player's friends.
     * 
     * @return
     */
    public List<UUID> getFriends() {
        return ImmutableList.copyOf(this.friends);
    }
    
    public List<UUID> getFoes() {
        return ImmutableList.copyOf(this.foes);
    }
    
    /**
     * Sets player's server location.
     * 
     * @param location
     */
    public void setServerLocation(final ServerLocation location) {
        this.serverLocation = location;
    }
    
    /**
     * Returns current player's server location.
     * 
     * @return
     */
    public ServerLocation getServerLocation() {
        return this.serverLocation;
    }
    
    /**
     * Returns whatever is this player friend with the specified.
     * 
     * @param uniqueId
     * @return
     */
    public boolean isFriend(final UUID uniqueId) {
        return this.friends.contains(uniqueId);
    }
    
    public boolean isFoe(final UUID uniqueId) {
        return this.foes.contains(uniqueId);
    }
    
    /**
     * Returns setting value.
     * 
     * @param setting
     * @return
     */
    public boolean getSetting(final Settings setting) {
        if (this.settings.containsKey(setting))
            return this.settings.get(setting);
        else
            return true;
    }
    
    /**
     * Set players settings.
     * 
     * @param setting
     * @param value
     */
    public void setSetting(final Settings setting, final boolean value) {
        this.settings.put(setting, value);
    }
    
    /**
     * Return's whether is player spectating.
     * 
     * @return
     */
    public boolean isSpectating() {
        return this.spectating;
    }
    
    /**
     * Sets player's spectating state.
     * 
     * @param spectating
     */
    public void setSpectating(final boolean spectating) {
        this.spectating = spectating;
    }
    
    /**
     * Saves player's profile to file.
     * 
     * @param path
     *            path to save
     */
    public void save(final String path) {
        YamlConfiguration yaml = new YamlConfiguration();
        
        yaml.set("player.uuid", this.player.toString());
        yaml.set("player.points", this.coins);
        yaml.set("player.warnCount", this.warnCount);
        yaml.set("player.lastKnownName", this.lastKnownName);
        yaml.set("player.friends", this.friends);
        yaml.set("player.foes", this.foes);
        
        try {
            yaml.save(new File(path));
        } catch (IOException e) {
            Log.addProblem("Can't save player profile: " + e.toString());
            e.printStackTrace();
        }
    }
    
    /**
     * Loads player profile from specified path.
     * 
     * @param playerProfile
     * @return
     */
    public static PlayerProfile load(final String path) {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(new File(path));
        UUID uuid = UUID.fromString(yaml.getString("player.uuid"));
        
        PlayerProfile profile = new PlayerProfile(uuid);
        
        profile.coins = yaml.getInt("player.points");
        profile.lastKnownName = yaml.getString("player.lastKnownName");
        
        List<?> friends = yaml.getList("player.friends");
        List<?> foes = yaml.getList("player.foes");
        
        for (Object obj : friends)
            profile.addFriend(UUID.fromString(obj.toString()));
        for (Object obj : foes)
            profile.addFoe(UUID.fromString(obj.toString()));
        
        return profile;
    }
    
    public void setParticleType(final ParticleEffect2 effect) {
        this.particleType = effect;
    }
    
    public ParticleEffect2 getParticleType() {
        return this.particleType;
    }
    
    /**
     * Return the number of points.
     * 
     * @return
     */
    public int getPoints() {
        return this.coins;
    }
    
    /**
     * Add specified amount of points.
     * 
     * @param points
     */
    public void addPoints(final int points) {
        this.coins += points;
    }
}
