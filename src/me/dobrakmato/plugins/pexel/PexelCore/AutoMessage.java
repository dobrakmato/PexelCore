package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * AutoMessage.
 * 
 * @author Mato Kormuth
 * 
 */
public class AutoMessage implements UpdatedPart
{
	private static final List<String>	strings		= new ArrayList<String>();
	private static final String			prefix		= "";
	private static final long			interval	= 20000 / 20;				//each 60 seconds.
																				
	private static final Random			random		= new Random();
	private static int					taskId		= 0;
	private static boolean				enabled		= false;
	
	static
	{
		AutoMessage.strings.add("Hras " + ChatColor.BLUE + "BETA "
				+ ChatColor.RESET + " verziu pexel-u. Dakujeme!");
		AutoMessage.strings.add("Navstivte aj nasu web stranku www.pexel.eu (v priprave) !");
		AutoMessage.strings.add("Hras " + ChatColor.BLUE + "BETA "
				+ ChatColor.RESET + " verziu pexel-u. Dakujeme!");
		AutoMessage.strings.add("Ak mas nejake pripomienky, povedz nam na na "
				+ ChatColor.GREEN + "ts.mertex.eu!");
	}
	
	/**
	 * Boradcast random message to chat.
	 */
	public static void pushMessage()
	{
		Bukkit.broadcastMessage(AutoMessage.prefix
				+ AutoMessage.strings.get(AutoMessage.random.nextInt(AutoMessage.strings.size())));
	}
	
	/**
	 * Returns if is AutoMessage enabled.
	 * 
	 * @return
	 */
	public static boolean isEnabled()
	{
		return AutoMessage.enabled;
	}
	
	@Override
	public void updateStart(final PexelCore plugin)
	{
		Log.partEnable("Automessage");
		UpdatedParts.registerPart(this);
		AutoMessage.enabled = true;
		AutoMessage.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(
				plugin, new Runnable() {
					@Override
					public void run()
					{
						AutoMessage.pushMessage();
					}
				}, 0, AutoMessage.interval);
	}
	
	@Override
	public void updateStop()
	{
		Log.partDisable("Automessage");
		AutoMessage.enabled = false;
		Bukkit.getScheduler().cancelTask(AutoMessage.taskId);
	}
}
