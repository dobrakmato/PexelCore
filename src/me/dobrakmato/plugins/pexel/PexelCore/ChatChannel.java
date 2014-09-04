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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Class specifing chat channel.
 * 
 * @author Mato Kormuth
 * 
 */
public class ChatChannel
{
	//Last "random" channel ID.
	private static long								randomId		= 0;
	
	private final String							name;
	private final Map<Player, ChannelSubscriber>	subscribers		= new HashMap<Player, ChannelSubscriber>();
	private String									prefix			= "";
	private long									lastActivity	= Long.MAX_VALUE;
	
	/**
	 * Creates new chat channel with specified name.
	 * 
	 * @param name
	 *            name of channel
	 */
	public ChatChannel(final String name)
	{
		this.name = name;
		ChatManager.registerChannel(this);
	}
	
	public ChatChannel(final String name, final String prefix)
	{
		this.name = name;
		this.prefix = prefix;
		ChatManager.registerChannel(this);
	}
	
	public ChatChannel(final String name, final String prefix,
			final boolean writable)
	{
		this.name = name;
		this.prefix = prefix;
		ChatManager.registerChannel(this);
	}
	
	public ChatChannel(final String name, final boolean writable)
	{
		this.name = name;
		ChatManager.registerChannel(this);
	}
	
	/**
	 * Subscribes player to this chat channel.
	 * 
	 * @param player
	 *            player
	 */
	public void subscribe(final Player player, final SubscribeMode mode)
	{
		this.subscribers.put(player, new PlayerChannelSubscriber(player, mode));
		
		player.sendMessage(ChatColor.LIGHT_PURPLE + "You have joined '"
				+ this.getName() + "' chat channel with mode '"
				+ mode.toString() + "'!");
	}
	
	/**
	 * Unsubscribes player from this chat channel.
	 * 
	 * @param player
	 *            specified player
	 */
	public void unsubscribe(final Player player)
	{
		this.subscribers.remove(player);
		
		player.sendMessage(ChatColor.LIGHT_PURPLE + "You have left '"
				+ this.getName() + "' chat channel!");
	}
	
	/**
	 * Retruns true, if player is subscribed.
	 * 
	 * @param player
	 *            player to check
	 * @return state of subscription
	 */
	public boolean isSubscribed(final Player player)
	{
		return this.subscribers.containsKey(player);
	}
	
	/**
	 * Returns whether player can read messages from this channel.
	 * 
	 * @param player
	 *            player to check
	 * @return true if player can read
	 */
	public boolean canRead(final Player player)
	{
		return this.subscribers.containsKey(player);
	}
	
	/**
	 * Sends message to all subscribers.
	 * 
	 * @param message
	 *            message to be send
	 */
	public void broadcastMessage(final String message)
	{
		this.lastActivity = System.currentTimeMillis();
		for (Iterator<ChannelSubscriber> iterator = this.subscribers.values().iterator(); iterator.hasNext();)
		{
			ChannelSubscriber p = iterator.next();
			if (p.isOnline())
				p.sendMessage(this.prefix + message);
			else
				iterator.remove();
		}
	}
	
	/**
	 * Returns name of channel.
	 * 
	 * @return the name
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Returns if player can write to this channel.
	 * 
	 * @param player
	 * @return true if player can write
	 */
	public boolean canWrite(final Player player)
	{
		if (!this.subscribers.containsKey(player))
			return false;
		else
		{
			if (this.subscribers.get(player).getMode() == SubscribeMode.READ_WRITE)
				return true;
			else
				return false;
		}
	}
	
	/**
	 * Returns random new channel, without specified name and stuff.
	 * 
	 * @return random chat channel
	 */
	public static ChatChannel createRandom()
	{
		ChatChannel.randomId++;
		return new ChatChannel("r" + ChatChannel.randomId);
	}
	
	/**
	 * Set's channels prefix.
	 */
	public void setPrefix(final String prefix)
	{
		this.prefix = prefix;
	}
	
	/**
	 * @return the lastActivity
	 */
	public long getLastActivity()
	{
		return this.lastActivity;
	}
}
