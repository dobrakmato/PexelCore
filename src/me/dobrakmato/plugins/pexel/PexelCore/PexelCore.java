package me.dobrakmato.plugins.pexel.PexelCore;

import me.dobrakmato.plugins.pexel.TntTag.TntTagMinigame;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Hlavna trieda Pexel.
 * 
 * @author Mato Kormuth
 * 
 */
public class PexelCore extends JavaPlugin
{
	/**
	 * Pexel matchmaking.
	 */
	public Matchmaking		matchmaking;
	/**
	 * Pexel TCP server.
	 */
	public PexelServer		server;
	/**
	 * Player freezer.
	 */
	public PlayerFreezer	freezer;
	/**
	 * Eent processor.
	 */
	public EventProcessor	eventProcessor;
	/**
	 * Magic clock instance.
	 */
	public MagicClock		magicClock;
	
	public AutoMessage		message;
	
	@Override
	public void onDisable()
	{
		//Shutdown all updated parts.
		UpdatedParts.shutdown();
		
		this.server.close();
		
		StorageEngine.saveData();
		
		Log.partDisable("Core");
	}
	
	@Override
	public void onEnable()
	{
		Log.partEnable("Core");
		
		Pexel.initialize(this);
		
		this.freezer = new PlayerFreezer();
		
		this.server = new PexelServer();
		this.server.listen();
		
		this.message = new AutoMessage();
		this.message.updateStart(this);
		
		this.matchmaking = new Matchmaking();
		this.matchmaking.updateStart(this);
		
		this.magicClock = new MagicClock();
		
		this.eventProcessor = new EventProcessor();
		
		this.getCommand("arena").setExecutor(new ArenaCommand());
		this.getCommand("friend").setExecutor(new FriendCommand());
		this.getCommand("settings").setExecutor(new SettingsCommand());
		this.getCommand("party").setExecutor(new PartyCommand());
		this.getCommand("lobbyarena").setExecutor(new LobbyCommand());
		
		StorageEngine.initialize(this);
		
		new AlternativeCommands();
		
		//Initialize minigame
		new TntTagMinigame();
	}
}
