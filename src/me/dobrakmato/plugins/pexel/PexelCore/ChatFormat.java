package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Union chat formatter for pexel.
 * 
 * @author Mato Kormuth
 * 
 */
public class ChatFormat
{
	private static final String	minigameFormat		= ChatColor.DARK_GREEN
															+ "[%minigame%] "
															+ ChatColor.WHITE
															+ " %msg%";
	private static final String	errorFormat			= ChatColor.RED + "%msg%";
	private static final String	successFormat		= ChatColor.GREEN + "%msg%";
	private static final String	chatDefaultFormat	= ChatColor.GRAY
															+ "%player% > %msg%";
	private static final String	chatOpFormat		= ChatColor.GOLD
															+ "[OP] %player% > %msg%";
	
	public static final String error(final String msg)
	{
		return ChatFormat.errorFormat.replace("%msg%", msg);
	}
	
	public static final String success(final String msg)
	{
		return ChatFormat.successFormat.replace("%msg%", msg);
	}
	
	public static final String chatPlayer(final String msg, final Player player)
	{
		return ChatFormat.chatDefaultFormat.replace("%player%",
				player.getDisplayName()).replace("%msg%", msg);
	}
	
	public static final String chatPlayerOp(final String msg,
			final Player player)
	{
		return ChatFormat.chatOpFormat.replace("%player%",
				player.getDisplayName()).replace("%msg%", msg);
	}
	
	public static final String chatPlayerFriend(final String msg,
			final Player player)
	{
		return ChatColor.BLUE
				+ ChatFormat.chatDefaultFormat.replace("%player%",
						player.getDisplayName()).replace("%msg%", msg);
	}
	
	public static final String onlinefriends(final List<UUID> players)
	{
		String string = ChatColor.GOLD + "Online players: ";
		for (UUID uuid : players)
		{
			if (Bukkit.getPlayer(uuid).isOnline())
			{
				ServerLocation location = StorageEngine.getProfile(uuid).getServerLocation();
				string += Bukkit.getPlayer(uuid).getDisplayName() + " ("
						+ location.toString() + ")";
			}
		}
		return string;
	}
	
	public static final String minigame(final Minigame minigame,
			final String msg)
	{
		return ChatFormat.minigameFormat.replace("%minigame%",
				minigame.getDisplayName()).replace("%msg%", msg);
	}
}
