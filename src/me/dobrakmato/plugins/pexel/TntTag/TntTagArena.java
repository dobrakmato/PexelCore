package me.dobrakmato.plugins.pexel.TntTag;

import java.util.ArrayList;
import java.util.List;

import me.dobrakmato.plugins.pexel.PexelCore.AreaFlag;
import me.dobrakmato.plugins.pexel.PexelCore.ChatFormat;
import me.dobrakmato.plugins.pexel.PexelCore.GameState;
import me.dobrakmato.plugins.pexel.PexelCore.Minigame;
import me.dobrakmato.plugins.pexel.PexelCore.MinigameArena;
import me.dobrakmato.plugins.pexel.PexelCore.Pexel;
import me.dobrakmato.plugins.pexel.PexelCore.Region;
import me.dobrakmato.plugins.pexel.PexelCore.ServerLocation;
import me.dobrakmato.plugins.pexel.PexelCore.ServerLocationType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Arena for recreation of Tnt Tag minigame plugin.
 * 
 * @author Mato Kormuth
 * 
 */
public class TntTagArena extends MinigameArena implements Listener
{
	private long		gameTimeLeft	= 60;
	private long		lobbyTimeLeft	= 10;
	private Location	arenaLobby;
	private Location	gameSpawn;
	private int			taskId			= 0;
	private boolean		gameRunning		= false;
	
	public TntTagArena(final Region region, final Minigame minigame,
			final String name)
	{
		super(minigame, name, region, 12);
		
		//Allow player hitting each other.
		this.setGlobalFlag(AreaFlag.PLAYER_DODAMAGE, true);
		this.setGlobalFlag(AreaFlag.PLAYER_GETDAMAGE, true);
		
		this.serverLocation = new ServerLocation("Simple Minigame",
				ServerLocationType.MINIGAME);
		
		Bukkit.getPluginManager().registerEvents(this, Pexel.getCore());
	}
	
	public void reset()
	{
		this.state = GameState.RESETING;
		//Prepeare arena for new players.
		Bukkit.getScheduler().cancelTask(this.taskId);
		this.gameTimeLeft = 60;
		this.lobbyTimeLeft = 10;
		//Open arena for new players.
		this.state = GameState.WAITING_EMPTY;
	}
	
	public void tryToStart()
	{
		this.state = GameState.WAITING_PLAYERS;
		//If we have enough players.
		if (this.activePlayers.size() >= 6)
		{
			//Start game count down!
			this.startCountDown();
		}
		else
		{
			//Spam everyone's chat with info that there is still not enough players.
			this.chatAll(ChatFormat.minigame(this.getMinigame(),
					"Can't start right now! " + (6 - this.activePlayers.size())
							+ " players have to join!"));
		}
	}
	
	private void startCountDown()
	{
		//Starts countdown
		this.taskId = Pexel.schedule(new Runnable() {
			@Override
			public void run()
			{
				TntTagArena.this.update();
			}
		}, 0L, 20L);
	}
	
	protected void update()
	{
		//If the game is in lobby state.
		if (!this.gameRunning)
		{
			//Count down lobby time.
			if (this.lobbyTimeLeft > 0)
			{
				//Tell everyone that, we are starting soon.
				this.chatAll(ChatFormat.minigame(this.getMinigame(),
						this.lobbyTimeLeft + " seconds to start!"));
				//Is the start time?
				if (this.lobbyTimeLeft == 1)
					//Start game!
					this.startGame();
				//Make countdown count!
				this.lobbyTimeLeft--;
			}
		}
		else
		{
			//Count down game time.
			if (this.gameTimeLeft > 0)
			{
				//If the round ended.
				if (this.gameTimeLeft == 1)
				{
					List<Player> deads = new ArrayList<Player>();
					for (Player p : this.activePlayers)
					{
						//Find TNT players.
						if (this.isTnt(p))
						{
							//Kill them.
							p.damage(20000D);
							p.getWorld().createExplosion(p.getLocation(), 1,
									false);
							//Add him to dead people.
							deads.add(p);
						}
					}
					//Disconnect deads from the game.
					for (Player p : deads)
						this.onPlayerLeft(p);
					
					//Check for winner.
					if (this.activePlayers.size() == 1)
					{
						Player winner = this.activePlayers.get(0);
						winner.sendMessage(ChatFormat.minigame(
								this.getMinigame(),
								ChatColor.GREEN
										+ "You won this TNT-TAG game! Congratulations!"));
						this.onPlayerLeft(winner);
					}
					else
					{
						//Start new round.
						this.newRound();
						this.gameTimeLeft = 60;
					}
				}
				this.gameTimeLeft--;
			}
			
			//If is game empty, cleanup and make space for other players.
			if (this.activePlayers.size() == 0)
				this.reset();
		}
	}
	
	private void startGame()
	{
		this.state = GameState.PLAYING_CANTJOIN;
		this.gameRunning = true;
		
		this.newRound();
	}
	
	private void newRound()
	{
		//Teleport all players to gamespawn and give them potion effect.
		for (Player p : this.activePlayers)
		{
			//Be sure that players has survival.
			p.setGameMode(GameMode.SURVIVAL);
			//Add speed potion.
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
					Integer.MAX_VALUE, 2));
			//Teleport him to game start.
			p.teleport(this.gameSpawn);
		}
		
		//Select some TNT players (5%).
		for (int i = 0; i < (this.activePlayers.size() / 100 * 20 + 1); i++)
		{
			//Get random player and set him to TNT.
			this.setTnt(
					this.activePlayers.get(Pexel.getRandom().nextInt(
							this.playerCount())), true);
		}
	}
	
	private boolean isTnt(final Player p)
	{
		return p.getInventory().getHelmet().getType() == Material.TNT;
	}
	
	private void setTnt(final Player p, final boolean value)
	{
		if (value)
		{
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
					Integer.MAX_VALUE, 4));
			p.getInventory().getHelmet().setType(Material.TNT);
			
			this.chatAll(ChatFormat.minigame(this.getMinigame(),
					"Player " + p.getName() + " is 'it' now!"));
		}
		else
		{
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
					Integer.MAX_VALUE, 2));
		}
	}
	
	@Override
	public void onPlayerJoin(final Player player)
	{
		super.onPlayerJoin(player);
		
		player.teleport(this.arenaLobby);
		
		this.chatAll(ChatFormat.minigame(this.getMinigame(), "Player '"
				+ player.getDisplayName() + "' has joined game!"));
		
		this.tryToStart();
	}
	
	@Override
	public void onPlayerLeft(final Player player)
	{
		super.onPlayerLeft(player);
		
		player.teleport(this.getMinigame().getLobby());
		
		this.chatAll(ChatFormat.minigame(this.getMinigame(), "Player '"
				+ player.getDisplayName() + "' has left game!"));
	}
	
	@EventHandler
	private void onEntityDamageEntity(final EntityDamageByEntityEvent event)
	{
		//If player damaged player.
		if (event.getDamager() instanceof Player
				&& event.getEntity() instanceof Player)
		{
			Player damager = (Player) event.getDamager();
			Player damaged = (Player) event.getEntity();
			//And damager is this lobby player.
			if (this.activePlayers.contains(damager)
					&& this.activePlayers.contains(damaged))
			{
				if (this.isTnt(damager))
				{
					this.setTnt(damager, false);
					this.setTnt(damaged, true);
				}
			}
		}
	}
}
