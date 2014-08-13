package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Event processor for pexel.
 * 
 * @author Mato Kormuth
 * 
 */
public class EventProcessor implements Listener
{
	private final Map<String, List<ParametrizedRunnable>>	callbacks	= new HashMap<String, List<ParametrizedRunnable>>();
	
	public EventProcessor()
	{
		Bukkit.getPluginManager().registerEvents(this, Pexel.getCore());
	}
	
	public void addCallback(final String eventType,
			final ParametrizedRunnable callback)
	{
		if (this.callbacks.containsKey(eventType))
			this.callbacks.get(eventType).add(callback);
		else
		{
			List<ParametrizedRunnable> list = new ArrayList<ParametrizedRunnable>();
			list.add(callback);
			this.callbacks.put(eventType, list);
		}
	}
	
	public void removeCallback(final Class<Event> eventType,
			final Runnable callback)
	{
		if (this.callbacks.containsKey(eventType))
			this.callbacks.get(eventType).remove(callback);
	}
	
	@EventHandler
	private void onEvent(final Event event)
	{
		if (this.callbacks.containsKey(event.getEventName()))
			for (ParametrizedRunnable callback : this.callbacks.get(event.getEventName()))
				callback.run(event);
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
	
	@EventHandler
	private void onChat(final AsyncPlayerChatEvent event)
	{
		if (event.getPlayer().isOp())
			event.setMessage(ChatFormat.chatPlayerOp(event.getMessage(),
					event.getPlayer()));
		else
			event.setMessage(ChatFormat.chatPlayer(event.getMessage(),
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
						(Player) event.getWhoClicked(), event.getCurrentItem());
				event.setCancelled(true);
			}
		}
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
