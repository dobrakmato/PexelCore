package me.dobrakmato.plugins.pexel.PexelCore;

import me.dobrakmato.plugins.pexel.ColorWar.ColorWarMinigame;
import me.dobrakmato.plugins.pexel.PexelNetworking.Server;
import me.dobrakmato.plugins.pexel.ZabiPitkesa.ZabiPitkesaMinigame;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Class where is all hard coded stuff stored.
 * 
 * @author Mato Kormuth
 * 
 */
public class HardCoded
{
	/**
	 * Main method called from Plugin.onEnable()
	 */
	public static final void main()
	{
		//Initialize color war mingiame
		new ColorWarMinigame();
		//Initialize zabi pitkesa minigame
		new ZabiPitkesaMinigame();
		
		//Initialize main gates
		StorageEngine.addGate("Lsurvival",
				new TeleportGate(new Region(new Vector(-7, 50, 258),
						new Vector(-9, 54, 264), Bukkit.getWorld("world")),
						new TeleportAction(null, new Server(null, "survival",
								"survival"))));
		
		StorageEngine.addGate("Lstarving",
				new TeleportGate(new Region(new Vector(26, 50, 266),
						new Vector(28, 55, 260), Bukkit.getWorld("world")),
						new TeleportAction(null, new Server(null, "starving",
								"starving"))));
		
		StorageEngine.addGate(
				"Lminigame",
				new TeleportGate(new Region(new Vector(7, 50, 280), new Vector(
						13, 55, 282), Bukkit.getWorld("world")),
						new TeleportAction(
								new Location(Bukkit.getWorld("world"), 1972.5,
										147.5, 2492.5), Server.THIS_SERVER)));
		
		//Initialize gates
		StorageEngine.addGate("mg_colorwar", new TeleportGate(new Region(
				new Vector(1976, 147, 2532), new Vector(1972, 153, 2534),
				Bukkit.getWorld("world")), new CommandAction("cwtest")));
		
		//Initialize lobbies
		StorageEngine.addLobby(new Lobby("hub", new Region(new Vector(52, 107,
				226), new Vector(-30, 1, 303), Bukkit.getWorld("world"))));
		
		StorageEngine.getLobby("hub").setThresholdY(10);
		
		StorageEngine.addLobby(new Lobby("minigamelobby", new Region(
				new Vector(2038, 0, 2571), new Vector(1910, 255, 2437),
				Bukkit.getWorld("world"))));
	}
}
