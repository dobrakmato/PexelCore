package me.dobrakmato.plugins.pexel.PexelCore;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Event processor for pexel.
 * 
 * @author Mato Kormuth
 * 
 */
public class EventProcessor implements Listener
{
	public EventProcessor()
	{
		Bukkit.getPluginManager().registerEvents(this, Pexel.getCore());
	}
	
	@EventHandler
	private void onPlayerMove(final PlayerMoveEvent event)
	{
		if (event.getPlayer().getName().equalsIgnoreCase("dobrakmato"))
		{
			if (event.getPlayer().isSprinting())
			{
				for (double i = 0; i < 2; i += 0.20D)
				{
					ParticleEffect.FLAME.display(event.getFrom(), 0, 0, 0, 1,
							10);
					ParticleEffect.DRIP_LAVA.display(event.getFrom(), 0, 0.20F,
							0, 1, 10);
					ParticleEffect.RED_DUST.display(event.getFrom(), 0, 0.10F,
							0, 1, 10);
				}
			}
		}
	}
	
	@EventHandler
	private void onBlockBreak(final BlockBreakEvent event)
	{
		if (!this.hasPermission(event.getBlock().getLocation(),
				event.getPlayer(), AreaFlag.BLOCK_BREAK))
			event.setCancelled(true);
	}
	
	@EventHandler
	private void onBlockPlace(final BlockPlaceEvent event)
	{
		if (!this.hasPermission(event.getBlock().getLocation(),
				event.getPlayer(), AreaFlag.BLOCK_PLACE))
			event.setCancelled(true);
	}
	
	@EventHandler
	private void onPlayerDropItem(final PlayerDropItemEvent event)
	{
		if (!this.hasPermission(event.getPlayer().getLocation(),
				event.getPlayer(), AreaFlag.PLAYER_DROPITEM))
			event.setCancelled(true);
	}
	
	@EventHandler
	private void onPlayerInteract(final PlayerInteractEvent event)
	{
		if (event.getClickedBlock() != null)
		{
			if (event.getClickedBlock().getType() == Material.SIGN
					|| event.getClickedBlock().getType() == Material.SIGN_POST)
			{
				Sign sign = (Sign) event.getClickedBlock();
				String[] lines = sign.getLines();
				if (lines.length > 1)
				{
					String command = lines[0];
					if (command.equalsIgnoreCase("[Server]"))
					{
						event.getPlayer().performCommand("/server " + lines[1]);
					}
					else if (command.equalsIgnoreCase("[Warp]"))
					{
						event.getPlayer().performCommand("/warp " + lines[1]);
					}
					else if (command.equalsIgnoreCase("[World]"))
					{
						World w = Bukkit.getWorld(lines[1]);
						if (w == null)
							event.getPlayer().sendMessage(
									ChatFormat.error(Lang.getTranslation("worldnotfound")));
						else
							event.getPlayer().teleport(w.getSpawnLocation());
					}
				}
			}
		}
	}
	
	@EventHandler
	private void onChat(final AsyncPlayerChatEvent event)
	{
		if (event.getPlayer().isOp())
			event.setFormat(ChatFormat.chatPlayerOp(event.getMessage(),
					event.getPlayer()));
		else
			event.setFormat(ChatFormat.chatPlayer(event.getMessage(),
					event.getPlayer()));
	}
	
	@EventHandler
	private void onInventoryClick(final InventoryClickEvent event)
	{
		if (event.getInventory().getHolder() instanceof InventoryMenu)
		{
			if (event.getWhoClicked() instanceof Player)
			{
				((InventoryMenu) event.getInventory().getHolder()).inventoryClick(
						(Player) event.getWhoClicked(), event.getSlot());
				event.setCancelled(true);
				if (((InventoryMenu) event.getInventory().getHolder()).shouldClose(event.getSlot()))
				{
					event.getView().close();
				}
			}
		}
	}
	
	@EventHandler
	private void onPlayerJoin(final PlayerJoinEvent event)
	{
		//Load profile to memory or create empty profile.
		StorageEngine.loadProfile(event.getPlayer().getUniqueId());
	}
	
	@EventHandler
	private void onPlayerLeave(final PlayerQuitEvent event)
	{
		//Force save of player's profile.
		StorageEngine.saveProfile(event.getPlayer().getUniqueId());
	}
	
	private boolean hasPermission(final Location location, final Player player,
			final AreaFlag flag)
	{
		ProtectedArea area = null;
		if ((area = Areas.findArea(location)) != null)
		{
			if (!area.getPlayerFlag(flag, player.getUniqueId()))
			{
				if (area.getGlobalFlag(AreaFlag.AREA_CHAT_PERMISSIONDENIED))
					player.getPlayer().sendMessage(
							ChatFormat.error("You don't have permission for '"
									+ flag.toString() + "' in this area!"));
				return false;
			}
			return true;
		}
		return true;
	}
}
