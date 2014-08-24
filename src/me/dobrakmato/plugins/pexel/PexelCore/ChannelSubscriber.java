package me.dobrakmato.plugins.pexel.PexelCore;

/**
 * Interface definating the chat channel subscriber.
 * 
 * @author Mato Kormuth
 * 
 */
public interface ChannelSubscriber
{
	/**
	 * Sends message to this subscriber.
	 * 
	 * @param message
	 */
	public void sendMessage(String message);
	
	/**
	 * Returns this subscriber's mode.
	 * 
	 * @return
	 */
	public SubscribeMode getMode();
	
	/**
	 * Returns if is this subscriber online/active.
	 * 
	 * @return
	 */
	public boolean isOnline();
}
