package me.dobrakmato.plugins.pexel.PexelCore;

/**
 * Channel subscription mode.
 * 
 * @author Mato Kormuth
 * 
 */
public enum SubscribeMode
{
	/**
	 * Player can only read (receive) messages from channel.
	 */
	READ,
	/**
	 * Player can read (receive) and write (send) messages to channel.
	 */
	READ_WRITE;
}
