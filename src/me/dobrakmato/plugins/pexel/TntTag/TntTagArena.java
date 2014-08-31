package me.dobrakmato.plugins.pexel.TntTag;

import java.util.ArrayList;
import java.util.List;

import me.dobrakmato.plugins.pexel.PexelCore.AdvancedMinigameArena;
import me.dobrakmato.plugins.pexel.PexelCore.AreaFlag;
import me.dobrakmato.plugins.pexel.PexelCore.ArenaOption;
import me.dobrakmato.plugins.pexel.PexelCore.ChatManager;
import me.dobrakmato.plugins.pexel.PexelCore.GameState;
import me.dobrakmato.plugins.pexel.PexelCore.Minigame;
import me.dobrakmato.plugins.pexel.PexelCore.Pexel;
import me.dobrakmato.plugins.pexel.PexelCore.Region;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Arena for recreation of Tnt Tag minigame plugin.
 * 
 * @author Mato Kormuth
 * 
 */
public class TntTagArena extends AdvancedMinigameArena
{
	@ArenaOption(name = "gameTime")
	public long	gameTimeLeft	= 60;
	private int	taskId			= 0;
	private int	round			= 0;
	
	public TntTagArena(final Region region, final Minigame minigame,
			final String name, final Location lobbyLocation,
			final Location gameSpawn)
	{
		super(minigame, name, region, 12, 2, lobbyLocation, gameSpawn);
		
		//Allow player hitting each other.
		this.setGlobalFlag(AreaFlag.PLAYER_DODAMAGE, true);
		this.setGlobalFlag(AreaFlag.PLAYER_GETDAMAGE, true);
		this.setGlobalFlag(AreaFlag.BLOCK_BREAK, false);
		this.autoReset = true;
		this.playersCanRespawn = false;
	}
	
	@Override
	public void onReset()
	{
		//Prepeare arena for new players.
		Bukkit.getScheduler().cancelTask(this.taskId);
		this.gameTimeLeft = 60;
		this.round = 0;
		this.taskId = 0;
		//Open arena for new players.
		this.state = GameState.WAITING_EMPTY;
	}
	
	protected void update()
	{
		//Count down game time.
		if (this.gameTimeLeft > 0)
		{
			if ((this.gameTimeLeft % 10) == 0)
				this.chatAll(ChatColor.YELLOW + "" + this.gameTimeLeft
						+ " seconds to explode!");
			
			for (Player p : this.activePlayers)
				if (this.isTnt(p))
					p.playSound(p.getLocation(), Sound.NOTE_STICKS, 0.5F, 1F);
			
			if (this.gameTimeLeft <= 10)
			{
				//Play end round music...
				
				//Play cing
				for (Player p : this.activePlayers)
					if (this.isTnt(p))
						p.playSound(p.getLocation(), Sound.NOTE_PIANO, 0.5F, 1F);
			}
			
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
						p.getInventory().clear();
						p.getInventory().setHelmet(null);
						p.damage(20000D);
						p.getWorld().createExplosion(p.getLocation(), 0.1F,
								false);
						p.sendMessage("You lose the game.");
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
					winner.sendMessage(ChatManager.minigame(
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
			this.gameTimeLeft -= 1;
		}
		
		//If is game empty, cleanup and make space for other players.
		if (this.activePlayers.size() == 0)
			this.reset();
	}
	
	@Override
	public void onGameStart()
	{
		this.state = GameState.PLAYING_CANTJOIN;
		
		this.taskId = Pexel.schedule(new Runnable() {
			@Override
			public void run()
			{
				TntTagArena.this.update();
			}
		}, 0L, 20L);
		
		this.newRound();
	}
	
	private void newRound()
	{
		this.round++;
		this.chatAll(ChatColor.YELLOW + "Round #" + this.round
				+ "! 60 seconds to explode! " + ChatColor.DARK_AQUA
				+ this.playerCount() + " players left!");
		//Teleport all players to gamespawn and give them potion effect.
		for (Player p : this.activePlayers)
		{
			//Be sure that players has survival.
			p.setGameMode(GameMode.ADVENTURE);
			//Add speed potion.
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
					Integer.MAX_VALUE, 2));
			//Teleport him to game start.
			p.teleport(this.gameSpawn);
		}
		
		//Select some TNT players.
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
		if (p.getInventory().getHelmet() == null)
			return false;
		else
			return p.getInventory().getHelmet().getType() == Material.TNT;
	}
	
	private void setTnt(final Player p, final boolean value)
	{
		p.removePotionEffect(PotionEffectType.SPEED);
		if (value)
		{
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 50000, 4));
			p.getInventory().setHelmet(new ItemStack(Material.TNT));
			
			this.chatAll(ChatManager.minigame(this.getMinigame(),
					ChatColor.GOLD + "Player " + p.getName() + " is 'it' now!"));
			
			p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
			p.getWorld().playSound(p.getLocation(), Sound.FIZZ, 1, 1);
		}
		else
		{
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 50000, 2));
			
			this.getWorld().playSound(p.getLocation(), Sound.VILLAGER_YES, 1, 1);
			
			try
			{
				p.getInventory().setHelmet(new ItemStack(Material.AIR));
			} catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	@Override
	public void onPlayerJoin(final Player player)
	{
		if (this.canJoin())
		{
			super.onPlayerJoin(player);
			
			player.teleport(this.lobbyLocation);
			player.getInventory().clear();
			
			player.setGameMode(GameMode.ADVENTURE);
		}
		else
		{
			player.sendMessage("You can't join at this time!");
		}
	}
	
	@Override
	public void onPlayerLeft(final Player player)
	{
		super.onPlayerLeft(player);
		
		player.teleport(this.getMinigame().getLobbyLocation());
		player.removePotionEffect(PotionEffectType.SPEED);
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
				event.setDamage(0D);
				if (this.isTnt(damager))
				{
					this.setTnt(damager, false);
					this.setTnt(damaged, true);
				}
				else
				{
					event.setCancelled(true);
				}
			}
		}
	}
}
