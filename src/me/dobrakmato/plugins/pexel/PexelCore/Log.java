package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.logging.Logger;

/**
 * Logger for pexel.
 * 
 * @author Mato Kormuth
 * 
 */
public class Log
{
	/**
	 * Internal logger.
	 */
	private final static Logger	log	= Logger.getLogger("PexelCore");
	
	/**
	 * Logs 'info' message.
	 * 
	 * @param msg
	 *            message to log
	 */
	public final static void info(final String msg)
	{
		Log.log.info(msg);
	}
	
	/**
	 * Logs 'warn' message.
	 * 
	 * @param msg
	 *            message to log
	 */
	public final static void warn(final String msg)
	{
		Log.log.warning(msg);
	}
	
	/**
	 * Logs 'severe' message.
	 * 
	 * @param msg
	 *            message to log
	 */
	public final static void severe(final String msg)
	{
		Log.log.severe(msg);
	}
	
	/**
	 * Logs 'partEnable' message.
	 * 
	 * @param msg
	 *            message to log
	 */
	public final static void partEnable(final String partName)
	{
		Log.log.info("Enabling Pexel-" + partName + "...");
	}
	
	/**
	 * Logs 'parnDisable' message.
	 * 
	 * @param msg
	 *            message to log
	 */
	public final static void partDisable(final String partName)
	{
		Log.log.info("Disabling Pexel-" + partName + "...");
	}
	
	/**
	 * Logs 'gameEnable' message.
	 * 
	 * @param msg
	 *            message to log
	 */
	public final static void gameEnable(final String gameName)
	{
		Log.log.info("Enabling Minigame-" + gameName + "...");
	}
	
	/**
	 * Logs 'gameDisable' message.
	 * 
	 * @param msg
	 *            message to log
	 */
	public final static void gameDisable(final String gameName)
	{
		Log.log.info("Disabling Minigame-" + gameName + "...");
	}
}
