package me.dobrakmato.plugins.pexel.ZabiPitkesa;

import me.dobrakmato.plugins.pexel.PexelCore.AdvancedMinigameArena;
import me.dobrakmato.plugins.pexel.PexelCore.ArenaOption;
import me.dobrakmato.plugins.pexel.PexelCore.BlockChange;
import me.dobrakmato.plugins.pexel.PexelCore.BlockRollbacker;
import me.dobrakmato.plugins.pexel.PexelCore.GameState;
import me.dobrakmato.plugins.pexel.PexelCore.ItemUtils;
import me.dobrakmato.plugins.pexel.PexelCore.Log;
import me.dobrakmato.plugins.pexel.PexelCore.Minigame;
import me.dobrakmato.plugins.pexel.PexelCore.Pexel;
import me.dobrakmato.plugins.pexel.PexelCore.Region;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @author Mato Kormuth
 * 
 */
public class ZabiPitkesaArena extends AdvancedMinigameArena
{
	private final BlockRollbacker	rollback;
	private Pig						boss;
	private int						taskId;
	private long					ticks			= 0L;
	@ArenaOption(name = "bossStrenght")
	public float					bossStrenght	= 1.5F;
	
	public ZabiPitkesaArena(final Minigame minigame, final String arenaName,
			final Region region, final int maxPlayers, final int minPlayers,
			final Location lobbyLocation, final Location gameSpawn)
	{
		super(minigame, arenaName, region, maxPlayers, 10, lobbyLocation,
				gameSpawn);
		this.setPlayersCanRespawn(false);
		this.rollback = new BlockRollbacker();
		this.setCountdownLenght(15);
	}
	
	@Override
	public void onGameStart()
	{
		this.startTask();
		this.spawnBoss();
		this.teleportPlayers(this.gameSpawn);
		
		for (Player p : this.activePlayers)
			p.getInventory().addItem(
					ItemUtils.namedItemStack(Material.APPLE, ChatColor.RED
							+ "Gun", null));
	}
	
	@Override
	public void onReset()
	{
		this.ticks = 0L;
		this.rollback.rollbackAsync(new Runnable() {
			@Override
			public void run()
			{
				ZabiPitkesaArena.this.state = GameState.WAITING_EMPTY;
				Log.info("ZabiPitkesa reset skoncil.");
			}
		});
		
	}
	
	@Override
	public void onGameEnd()
	{
		this.stopTask();
	}
	
	private void startTask()
	{
		this.taskId = Pexel.schedule(new Runnable() {
			@Override
			public void run()
			{
				//Update display name.
				ZabiPitkesaArena.this.updateBossHealth();
				//Each 3 seconds perform special attach.
				if ((ZabiPitkesaArena.this.ticks % 60) == 0)
					ZabiPitkesaArena.this.specialAttack();
				ZabiPitkesaArena.this.ticks += 5;
			}
		}, 0L, 5L);
	}
	
	private void specialAttack()
	{
		//Find target.
		Player target = this.findTraget();
		//Find attack - knockback. 
		target.setVelocity(target.getLocation().clone().subtract(
				this.boss.getLocation()).multiply(-1 * this.bossStrenght).toVector());
		
		this.chatAll("The boss attacked '" + target.getDisplayName() + "'!");
	}
	
	private Player findTraget()
	{
		return this.activePlayers.get(Pexel.getRandom().nextInt(
				this.activePlayers.size() - 1));
	}
	
	private void updateBossHealth()
	{
		this.boss.setCustomName(ChatColor.RED + "pitkes22\nHealth: "
				+ ((Damageable) this.boss).getHealth() + "/"
				+ ((Damageable) this.boss).getMaxHealth());
	}
	
	private void stopTask()
	{
		Bukkit.getScheduler().cancelTask(this.taskId);
	}
	
	private void spawnBoss()
	{
		this.playSoundAll(Sound.WITHER_SPAWN, 1.0F, 1.0F);
		this.boss = (Pig) this.getWorld().spawnEntity(this.gameSpawn,
				EntityType.PIG);
		this.boss.setBreed(false);
		this.boss.setMaxHealth(500D);
		this.boss.setHealth(500D);
		this.boss.setCustomName(ChatColor.RED + "pitkes22");
		this.boss.setCustomNameVisible(true);
	}
	
	@EventHandler
	private void onEntityDied(final EntityDeathEvent event)
	{
		if (event.getEntity().getUniqueId() == this.boss.getUniqueId())
			this.onBossDefeat();
	}
	
	@EventHandler
	private void onRightClick(final PlayerInteractEvent event)
	{
		if (event.getAction() == Action.RIGHT_CLICK_AIR)
			if (event.getPlayer().getItemInHand() != null)
				if (event.getPlayer().getItemInHand().getType() == Material.APPLE)
					if (this.activePlayers.contains(event.getPlayer()))
					{
						TNTPrimed tnt = (TNTPrimed) this.getWorld().spawnEntity(
								event.getPlayer().getLocation(),
								EntityType.PRIMED_TNT);
						tnt.setVelocity(event.getPlayer().getEyeLocation().getDirection().multiply(
								1.5F));
						tnt.setYield(2F);
					}
	}
	
	@EventHandler
	private void onExplosion(final EntityExplodeEvent event)
	{
		for (Block b : event.blockList())
			this.rollback.addChange(new BlockChange(b));
	}
	
	private void onBossDefeat()
	{
		this.chatAll("You managed to kill pitkes22! Congratulations!");
		this.kickAll();
	}
}
