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
	private static long								randomId		= 0;
	
	private final String							name;
	private final Map<Player, ChannelSubscriber>	subscribers		= new HashMap<Player, ChannelSubscriber>();
	private String									prefix			= "";
	private long									lastActivity	= Long.MAX_VALUE;
	
	/**
	 * Creates new chat channel with specified name.
	 * 
	 * @param name
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
	 * @return
	 */
	public boolean isSubscribed(final Player player)
	{
		return this.subscribers.containsKey(player);
	}
	
	/**
	 * Returns if player can read messages from this channel.
	 * 
	 * @param player
	 * @return
	 */
	public boolean canRead(final Player player)
	{
		return this.subscribers.containsKey(player);
	}
	
	/**
	 * Sends message to all subscribers.
	 * 
	 * @param message
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
	 * @return
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Returns if player can write to this channel.
	 * 
	 * @param player
	 * @return
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
	 * @return
	 */
	public static ChatChannel createRandom()
	{
		ChatChannel.randomId++;
		return new ChatChannel("random" + ChatChannel.randomId);
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
