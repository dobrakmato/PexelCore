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
	private final static Logger	log	= Logger.getLogger("PexelCore");
	
	public final static void info(final String msg)
	{
		Log.log.info(msg);
	}
	
	public final static void warn(final String msg)
	{
		Log.log.warning(msg);
	}
	
	public final static void severe(final String msg)
	{
		Log.log.severe(msg);
	}
	
	public final static void partEnable(final String partName)
	{
		Log.log.info("Enabling Pexel-" + partName + "...");
	}
	
	public final static void partDisable(final String partName)
	{
		Log.log.info("Disabling Pexel-" + partName + "...");
	}
	
	public final static void gameEnable(final String gameName)
	{
		Log.log.info("Enabling Minigame-" + gameName + "...");
	}
	
	public final static void gameDisable(final String gameName)
	{
		Log.log.info("Disabling Minigame-" + gameName + "...");
	}
}
