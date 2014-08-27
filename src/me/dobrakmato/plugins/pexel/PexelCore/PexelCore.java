package me.dobrakmato.plugins.pexel.PexelCore;

import me.dobrakmato.plugins.pexel.PexelNetworking.PexelMasterServer;
import me.dobrakmato.plugins.pexel.PexelNetworking.PexelServerClient;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

/**
 * Hlavna trieda Pexel.
 * 
 * @author Mato Kormuth
 * 
 */
public class PexelCore extends JavaPlugin implements PluginMessageListener
{
	/**
	 * Pexel matchmaking.
	 */
	public Matchmaking			matchmaking;
	/**
	 * Pexel TCP server.
	 */
	public PexelServer			oldServer;
	/**
	 * Player freezer.
	 */
	public PlayerFreezer		freezer;
	/**
	 * Eent processor.
	 */
	public EventProcessor		eventProcessor;
	/**
	 * Magic clock instance.
	 */
	public MagicClock			magicClock;
	
	public AutoMessage			message;
	
	private PexelMasterServer	pexelserver;
	public PexelServerClient	pexelclient;
	
	@Override
	public void onDisable()
	{
		//Shutdown all updated parts.
		UpdatedParts.shutdown();
		
		this.oldServer.close();
		
		this.pexelserver.close();
		
		StorageEngine.saveData();
		
		Log.partDisable("Core");
	}
	
	@Override
	public void onEnable()
	{
		Log.partEnable("Core");
		
		Pexel.initialize(this);
		
		this.freezer = new PlayerFreezer();
		
		this.oldServer = new PexelServer();
		this.oldServer.listen();
		
		this.pexelserver = new PexelMasterServer(30789);
		
		this.message = new AutoMessage();
		this.message.updateStart(this);
		
		this.matchmaking = new Matchmaking();
		this.matchmaking.updateStart(this);
		
		this.magicClock = new MagicClock();
		
		this.eventProcessor = new EventProcessor();
		
		this.getCommand("arena").setExecutor(new ArenaCommand());
		this.getCommand("friend").setExecutor(new FriendCommand());
		this.getCommand("unfriend").setExecutor(new UnfriendCommand());
		this.getCommand("settings").setExecutor(new SettingsCommand());
		this.getCommand("party").setExecutor(new PartyCommand());
		this.getCommand("lobbyarena").setExecutor(new LobbyCommand());
		this.getCommand("qj").setExecutor(new QJCommand());
		this.getCommand("gate").setExecutor(new GateCommand());
		
		StorageEngine.initialize(this);
		StorageEngine.loadData();
		
		new AlternativeCommands();
		
		this.pexelclient = new PexelServerClient("127.0.0.1", 30789);
		
		HardCoded.main();
		
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord",
				this);
	}
	
	@Override
	public void onPluginMessageReceived(final String channel,
			final Player player, final byte[] message)
	{
		if (!channel.equals("BungeeCord"))
		{
			return;
		}
		else
		{
			Log.info("onPluginMessageReceived: " + new String(message));
		}
	}
}
