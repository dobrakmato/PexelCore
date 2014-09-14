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
package me.dobrakmato.plugins.pexel.PexelCore.arenas;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import me.dobrakmato.plugins.pexel.PexelCore.areas.ProtectedArea;
import me.dobrakmato.plugins.pexel.PexelCore.chat.ChatManager;
import me.dobrakmato.plugins.pexel.PexelCore.core.Log;
import me.dobrakmato.plugins.pexel.PexelCore.core.PlayerHolder;
import me.dobrakmato.plugins.pexel.PexelCore.core.Region;
import me.dobrakmato.plugins.pexel.PexelCore.core.StorageEngine;
import me.dobrakmato.plugins.pexel.PexelCore.matchmaking.GameState;
import me.dobrakmato.plugins.pexel.PexelCore.matchmaking.MatchmakingGame;
import me.dobrakmato.plugins.pexel.PexelCore.minigame.Minigame;
import me.dobrakmato.plugins.pexel.PexelCore.util.ItemUtils;
import me.dobrakmato.plugins.pexel.PexelCore.util.ServerLocation;
import me.dobrakmato.plugins.pexel.PexelCore.util.ServerLocationType;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Minigame arena.
 * 
 * @author Mato Kormuth
 * 
 */
public class MinigameArena extends ProtectedArea implements MatchmakingGame,
        PlayerHolder {
    /**
     * Number of slots.
     */
    @ArenaOption(name = "maximumPlayers")
    protected int                slots;
    /**
     * The actual state of the arena.
     */
    protected GameState          state             = GameState.WAITING_PLAYERS;
    /**
     * The game mode that players should get, when they join the game.
     */
    protected GameMode           defaultGameMode   = GameMode.ADVENTURE;
    /**
     * List of active players in arena.
     */
    protected final List<Player> activePlayers     = new ArrayList<Player>();
    /**
     * List of spectating players in arena.
     */
    protected final List<Player> spectatingPlayers = new ArrayList<Player>();
    /**
     * Reference to minigame.
     */
    protected final Minigame     minigame;
    /**
     * Map that is currenlty played on this arena.
     */
    protected ArenaMap           map;
    
    public MinigameArena(final Minigame minigame, final String arenaName,
            final Region region, final int slots) {
        super(minigame.getName() + "_" + arenaName, region);
        this.minigame = minigame;
        this.slots = slots;
    }
    
    /**
     * Sends a chat message to all player in arena.
     * 
     * @param msg
     *            message to be send
     */
    public void chatAll(final String msg) {
        for (Player p : this.activePlayers)
            p.sendMessage(msg);
    }
    
    @Override
    public int getFreeSlots() {
        return this.slots - this.activePlayers.size();
    }
    
    @Override
    public int getMaximumSlots() {
        return this.slots;
    }
    
    @Override
    public GameState getState() {
        return this.state;
    }
    
    @Override
    public List<Player> getPlayers() {
        return this.activePlayers;
    }
    
    @Override
    public boolean canJoin() {
        return this.getFreeSlots() >= 1
                && (this.state == GameState.WAITING_PLAYERS
                        || this.state == GameState.WAITING_EMPTY || this.state == GameState.PLAYING_CANJOIN);
    }
    
    @Override
    public boolean canJoin(final int count) {
        return this.getFreeSlots() >= count
                && (this.state == GameState.WAITING_PLAYERS
                        || this.state == GameState.WAITING_EMPTY || this.state == GameState.PLAYING_CANJOIN);
    }
    
    @Override
    public void onPlayerJoin(final Player player) {
        this.activePlayers.add(player);
        player.setGameMode(this.defaultGameMode);
    }
    
    @Override
    public void onPlayerLeft(final Player player) {
        this.activePlayers.remove(player);
        //this.setSpectating(player, false);
    }
    
    /**
     * Plays sound for all players in arena.
     * 
     * @param sound
     *            sound to play
     * @param volume
     *            volume
     * @param pitch
     *            pitch
     */
    public void playSoundAll(final Sound sound, final float volume, final float pitch) {
        for (Player p : this.activePlayers)
            p.playSound(p.getLocation(), sound, volume, pitch);
    }
    
    /**
     * Sets spectating mode for player in this arena.
     * 
     * @param player
     *            player
     * @param spectating
     *            the value if the player should be spectating or not.
     */
    public void setSpectating(final Player player, final boolean spectating) {
        if (spectating) {
            if (!StorageEngine.getProfile(player.getUniqueId()).isSpectating()) {
                player.sendMessage(ChatManager.success("You are now spectating!"));
                StorageEngine.getProfile(player.getUniqueId()).setSpectating(true);
                player.getInventory().clear();
                player.getInventory().addItem(
                        ItemUtils.namedItemStack(Material.COMPASS, ChatColor.YELLOW
                                + "Spectating", null));
                player.setGameMode(GameMode.ADVENTURE);
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,
                        Integer.MAX_VALUE, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,
                        Integer.MAX_VALUE, 0));
                player.setAllowFlight(true);
                player.setFlying(true);
                this.spectatingPlayers.add(player);
            }
            else {
                //Player is already spectating.
                Log.warn("Player '" + player.getName()
                        + "' can't be moved to spectating mode by game '"
                        + this.getMinigame().getName()
                        + "': Player is already in spectating mode!");
            }
        }
        else {
            if (StorageEngine.getProfile(player.getUniqueId()).isSpectating()) {
                player.sendMessage(ChatManager.success("You are no longer spectating!"));
                StorageEngine.getProfile(player.getUniqueId()).setSpectating(false);
                player.getInventory().clear();
                player.setGameMode(this.defaultGameMode);
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
                player.setAllowFlight(false);
                player.setFlying(false);
                this.spectatingPlayers.remove(player);
            }
            else {
                //Player is not spectating.
                Log.warn("Player '" + player.getName()
                        + "' can't be moved from spectating mode by game '"
                        + this.getMinigame().getName()
                        + "': Player is not in spectating mode!");
            }
        }
    }
    
    /**
     * Returns whether is arena empty.
     * 
     * @return true if arena is empty
     */
    public boolean empty() {
        return this.activePlayers.size() == 0;
    }
    
    /**
     * Kicks all players from arena.
     */
    public void kickAll() {
        for (Player p : this.activePlayers)
            this.onPlayerLeft(p);
    }
    
    /**
     * Sends a message to all players and kicks them.
     * 
     * @param message
     *            message to send
     */
    public void kickAll(final String message) {
        for (Player p : this.activePlayers) {
            p.sendMessage(message);
            this.onPlayerLeft(p);
        }
    }
    
    /**
     * Return minigame running in this arena.
     * 
     * @return the minigame
     */
    public Minigame getMinigame() {
        return this.minigame;
    }
    
    public void save(final String path) {
        try {
            Document conf = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = conf.createElement("aconfig");
            conf.appendChild(root);
            
            Element info = conf.createElement("info");
            
            root.appendChild(info);
            
            Element options = conf.createElement("options");
            
            for (Field f : this.getClass().getDeclaredFields())
                if (f.isAnnotationPresent(ArenaOption.class)) {
                    ArenaOption annotation = f.getAnnotation(ArenaOption.class);
                    Element option = conf.createElement("option");
                    option.setAttribute("name", f.getName());
                    option.setAttribute("type", f.getType().getCanonicalName());
                    option.setAttribute("annotation", annotation.name());
                    option.setAttribute("persistent",
                            Boolean.toString(annotation.persistent()));
                    
                    if (f.getType().equals(Location.class)) {
                        option.setTextContent(f.get(this).toString());
                    }
                    else {
                        option.setTextContent(f.get(this).toString());
                    }
                    
                    options.appendChild(option);
                }
            
            root.appendChild(options);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(conf);
            StreamResult result = new StreamResult(new File(path));
            
            transformer.transform(source, result);
            
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (DOMException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    @Deprecated
    public ServerLocation getServerLocation() {
        return new ServerLocation("DEPRECATED", ServerLocationType.UNKNOWN);
    }
    
    public void setSlots(final int slots) {
        this.slots = slots;
    }
    
    public void setState(final GameState stateToSet) {
        this.state = stateToSet;
    }
    
    @Override
    public int getPlayerCount() {
        return this.activePlayers.size();
    }
    
    @Override
    public boolean contains(final Player player) {
        return this.activePlayers.contains(player);
    }
}
