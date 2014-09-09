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
package me.dobrakmato.plugins.pexel.PexelCore.chat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.dobrakmato.plugins.pexel.PexelCore.core.Log;
import me.dobrakmato.plugins.pexel.PexelCore.core.StorageEngine;
import me.dobrakmato.plugins.pexel.PexelCore.minigame.Minigame;
import me.dobrakmato.plugins.pexel.PexelCore.util.ServerLocation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Union chat formatter and manager for pexel.
 * 
 * @author Mato Kormuth
 * 
 */
public class ChatManager {
    private static final String                   minigameFormat    = ChatColor.DARK_GREEN
                                                                            + "[%minigame%] "
                                                                            + ChatColor.WHITE
                                                                            + " %msg%";
    private static final String                   errorFormat       = ChatColor.RED
                                                                            + "%msg%";
    private static final String                   successFormat     = ChatColor.GREEN
                                                                            + "%msg%";
    private static final String                   chatDefaultFormat = ChatColor.GRAY
                                                                            + "%player% > %msg%";
    private static final String                   chatOpFormat      = ChatColor.GOLD
                                                                            + "[OP] %player% > %msg%";
    
    private static final Map<String, ChatChannel> channels          = new HashMap<String, ChatChannel>();
    
    public static final ChatChannel               CHANNEL_GLOBAL    = new ChatChannel(
                                                                            "global",
                                                                            ChatColor.GOLD
                                                                                    + "[GLOBAL]",
                                                                            false);
    public static final ChatChannel               CHANNEL_OP        = new ChatChannel(
                                                                            "op",
                                                                            "[OP] ");
    public static final ChatChannel               CHANNEL_LOBBY     = new ChatChannel(
                                                                            "lobby",
                                                                            ChatColor.GRAY.toString());
    private static final long                     CHANNEL_LIFETIME  = 1000 * 60 * 60 * 24;               //One day
                                                                                                          
    public static final String error(final String msg) {
        return ChatManager.errorFormat.replace("%msg%", msg);
    }
    
    public static final String success(final String msg) {
        return ChatManager.successFormat.replace("%msg%", msg);
    }
    
    public static final String chatPlayer(final String msg, final Player player) {
        return ChatManager.chatDefaultFormat.replace("%player%", player.getDisplayName()).replace(
                "%msg%", msg);
    }
    
    public static final String chatPlayerOp(final String msg, final Player player) {
        return ChatManager.chatOpFormat.replace("%player%", player.getDisplayName()).replace(
                "%msg%", msg);
    }
    
    public static final String chatPlayerFriend(final String msg, final Player player) {
        return ChatColor.BLUE
                + ChatManager.chatDefaultFormat.replace("%player%",
                        player.getDisplayName()).replace("%msg%", msg);
    }
    
    public static final String onlinefriends(final List<UUID> players) {
        String string = ChatColor.GOLD + "Online players: ";
        for (UUID uuid : players) {
            if (Bukkit.getPlayer(uuid).isOnline()) {
                ServerLocation location = StorageEngine.getProfile(uuid).getServerLocation();
                string += Bukkit.getPlayer(uuid).getDisplayName() + " ("
                        + location.toString() + ")";
            }
        }
        return string;
    }
    
    /**
     * Returns formatted 'minigame' message.
     * 
     * @param minigame
     *            minigame
     * @param msg
     *            message to format
     * @return formatted message
     */
    public static final String minigame(final Minigame minigame, final String msg) {
        return ChatManager.minigameFormat.replace("%minigame%",
                minigame.getDisplayName()).replace("%msg%", msg);
    }
    
    /**
     * Registers chat channel.
     * 
     * @param chatChannel
     *            channel to register
     */
    public static void registerChannel(final ChatChannel chatChannel) {
        if (!ChatManager.channels.containsKey(chatChannel.getName()))
            ChatManager.channels.put(chatChannel.getName(), chatChannel);
        else
            throw new RuntimeException("Chat channel with name '"
                    + chatChannel.getName() + "' is already registered!");
    }
    
    /**
     * Unregisters player from channel.
     * 
     * @param channelName
     *            channel name
     * @param player
     *            player
     */
    public static void unregisterFromChannel(final String channelName,
            final Player player) {
        if (ChatManager.channels.containsKey(channelName))
            ChatManager.channels.get(channelName).unsubscribe(player);
        else
            throw new RuntimeException("Chat channel not found!");
    }
    
    /**
     * Unregisters subscriber from channel.
     * 
     * @param channelName
     *            channel name
     * @param subscriber
     *            subscriber
     */
    public static void unregisterFromChannel(final String channelName,
            final ChannelSubscriber subscriber) {
        if (ChatManager.channels.containsKey(channelName))
            ChatManager.channels.get(channelName).unsubscribe(subscriber);
        else
            throw new RuntimeException("Chat channel not found!");
    }
    
    /**
     * Removes old, unused channels.
     */
    public static void cleanUpChannels() {
        for (ChatChannel channel : ChatManager.channels.values())
            if (channel.getLastActivity() + ChatManager.CHANNEL_LIFETIME < System.currentTimeMillis())
                ChatManager.channels.remove(channel.getName());
        
    }
    
    /**
     * Called when manager should manage chat event.
     * 
     * @param event
     *            chat event
     */
    public static void __processChatEvent(final AsyncPlayerChatEvent event) {
        if (event.getMessage().trim().startsWith("@")) {
            String channelName = event.getMessage().substring(1,
                    event.getMessage().indexOf(" ")).replace(":", "");
            for (ChatChannel channel : ChatManager.channels.values())
                if (channel.getName().equalsIgnoreCase(channelName))
                    if (channel.canWrite(event.getPlayer()))
                        channel.broadcastMessage(event.getMessage());
        }
        
        for (ChatChannel channel : ChatManager.channels.values())
            if (channel.canWrite(event.getPlayer()))
                channel.broadcastMessage(event.getPlayer().getDisplayName() + " > "
                        + event.getMessage());
        
        Log.chat(event.getPlayer().getName() + ": " + event.getMessage());
        event.setCancelled(true);
    }
}
