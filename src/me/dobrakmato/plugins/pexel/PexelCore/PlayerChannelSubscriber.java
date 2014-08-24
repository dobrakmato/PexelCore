package me.dobrakmato.plugins.pexel.PexelCore;

import org.bukkit.entity.Player;

/**
 * Chat channel subscriber.
 * 
 * @author Mato Kormuth
 * 
 */
public class PlayerChannelSubscriber implements ChannelSubscriber
{
	private final SubscribeMode	mode;
	private final Player		player;
	
	public PlayerChannelSubscriber(final Player player, final SubscribeMode mode)
	{
		this.mode = mode;
		this.player = player;
	}
	
	/**
	 * Returns subscription mode.
	 * 
	 * @return
	 */
	@Override
	public SubscribeMode getMode()
	{
		return this.mode;
	}
	
	/**
	 * Sends message to this subscriber.
	 * 
	 * @param message
	 */
	@Override
	public void sendMessage(final String message)
	{
		this.player.sendMessage(message);
	}
	
	@Override
	public boolean isOnline()
	{
		return this.player.isOnline();
	}
}
