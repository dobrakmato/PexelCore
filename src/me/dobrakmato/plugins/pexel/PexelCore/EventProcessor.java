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
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

/**
 * Event processor for pexel.
 * 
 * <b>There will be changes in EVENTS!</b>
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
		if (event.getPlayer().isSprinting())
		{
			for (double i = 0; i < 1.5; i += 0.20D)
			{
				Location diff = event.getTo().subtract(event.getFrom()).multiply(
						1.2F);
				if (StorageEngine.getProfile(event.getPlayer().getUniqueId()).getParticleType() != null)
				{
					StorageEngine.getProfile(event.getPlayer().getUniqueId()).getParticleType().display(
							event.getFrom().subtract(diff).clone().add(0, i, 0),
							0.50F,
							0.20F,
							0.50F,
							1,
							(int) Math.floor(0.000005 * Math.pow(
									event.getTo().distance(event.getFrom()), 2)));
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
				if (event.getPlayer().isOp()
						&& event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK
						|| !event.getPlayer().isOp())
				{
					Sign sign = (Sign) event.getClickedBlock().getState();
					String[] lines = sign.getLines();
					if (lines.length > 1)
					{
						String command = lines[0].trim();
						if (command.equalsIgnoreCase("[Server]"))
						{
							ByteArrayDataOutput out = ByteStreams.newDataOutput();
							out.writeUTF("Connect");
							out.writeUTF(lines[1]);
							event.getPlayer().sendPluginMessage(
									Pexel.getCore(), "BungeeCord",
									out.toByteArray());
						}
						else if (command.equalsIgnoreCase("[Warp]"))
						{
							event.getPlayer().performCommand("warp " + lines[1]);
						}
						else if (command.equalsIgnoreCase("[World]"))
						{
							World w = Bukkit.getWorld(lines[1]);
							if (w == null)
								event.getPlayer().sendMessage(
										ChatManager.error(Lang.getTranslation("worldnotfound")));
							else
								event.getPlayer().teleport(w.getSpawnLocation());
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	private void onPlayerPortal(final PlayerPortalEvent event)
	{
		//Pass the event further...
		StorageEngine.gateEnter(event.getPlayer(),
				event.getPlayer().getLocation());
	}
	
	@EventHandler
	private void onChat(final AsyncPlayerChatEvent event)
	{
		ChatManager.onChat(event);
		/*
		 * if (event.getPlayer().isOp()) event.setFormat(ChatManager.chatPlayerOp(event.getMessage(),
		 * event.getPlayer())); else event.setFormat(ChatManager.chatPlayer(event.getMessage(), event.getPlayer()));
		 */
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
		//Register chat channels.
		ChatManager.CHANNEL_GLOBAL.subscribe(event.getPlayer(),
				SubscribeMode.READ);
		ChatManager.CHANNEL_LOBBY.subscribe(event.getPlayer(),
				SubscribeMode.READ_WRITE);
		
		//if (event.getPlayer().isOp())
		//	ChatManager.CHANNEL_OP.subscribe(event.getPlayer(),
		//			SubscribeMode.READ_WRITE);
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
		//System.out.println("permission check: p: " + player.getName() + "; f: "
		//		+ flag.toString());
		
		ProtectedArea area = Areas.findArea(location);
		if (area != null)
		{
			//System.out.println("in area");
			if (!area.getPlayerFlag(flag, player.getUniqueId()))
			{
				//System.out.println("perm denied");
				//if (area.getPlayerFlag(AreaFlag.AREA_CHAT_PERMISSIONDENIED, player.getUniqueId()))
				player.getPlayer().sendMessage(
						ChatManager.error("You don't have permission for '"
								+ flag.toString() + "' in this area!"));
				return false;
			}
			//System.out.println("perm granted");
			return true;
		}
		
		//System.out.println("permcheck end - not in area - granted");
		return true;
	}
}
