package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Logger for pexel.
 * 
 * @author Mato Kormuth
 * 
 */
public class Log
{
	/**
	 * List of problems, that should admins able to view.
	 */
	private final static List<String>	problems	= new ArrayList<String>();
	
	/**
	 * Internal logger.
	 */
	private final static Logger			log			= Logger.getLogger("PexelCore");
	
	/**
	 * Logs 'info' message.
	 * 
	 * @param msg
	 *            message to log
	 */
	public final static void info(final String msg)
	{
		Log.log.info("[PEXEL] " + msg);
	}
	
	/**
	 * Logs 'warn' message.
	 * 
	 * @param msg
	 *            message to log
	 */
	public final static void warn(final String msg)
	{
		Log.log.warning("[PEXEL] " + msg);
	}
	
	/**
	 * Logs 'severe' message.
	 * 
	 * @param msg
	 *            message to log
	 */
	public final static void severe(final String msg)
	{
		Log.log.severe("[PEXEL] " + msg);
	}
	
	/**
	 * Logs 'partEnable' message.
	 * 
	 * @param msg
	 *            message to log
	 */
	public final static void partEnable(final String partName)
	{
		Log.log.info("[PEXEL] " + "Enabling Pexel-" + partName + "...");
	}
	
	/**
	 * Logs 'parnDisable' message.
	 * 
	 * @param msg
	 *            message to log
	 */
	public final static void partDisable(final String partName)
	{
		Log.log.info("[PEXEL] " + "Disabling Pexel-" + partName + "...");
	}
	
	/**
	 * Logs 'gameEnable' message.
	 * 
	 * @param msg
	 *            message to log
	 */
	public final static void gameEnable(final String gameName)
	{
		Log.log.info("[PEXEL] " + "Enabling Minigame-" + gameName + "...");
	}
	
	/**
	 * Logs 'gameDisable' message.
	 * 
	 * @param msg
	 *            message to log
	 */
	public final static void gameDisable(final String gameName)
	{
		Log.log.info("[PEXEL] " + "Disabling Minigame-" + gameName + "...");
	}
	
	/**
	 * Adds problem to list.
	 * 
	 * @param message
	 */
	protected final static void addProblem(final String message)
	{
		Log.problems.add(message);
		
		for (Player p : Bukkit.getOnlinePlayers())
			if (p.isOp())
				p.sendMessage(ChatColor.RED + "[Problem]" + message);
	}
	
	/**
	 * Returns list of all problems.
	 * 
	 * @return
	 */
	protected final static List<String> getProblems()
	{
		return Log.problems;
	}
	
	public static void chat(final String msg)
	{
		Log.log.severe("[CHAT] " + msg);
	}
}
