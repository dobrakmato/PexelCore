// @formatter:off
/*
 * Pexel Project - Minecraft minigame server platform. 
 * Copyright (C) 2014 Matej Kormuth <http://www.matejkormuth.eu>
 * 
 * This file is part of Pexel.
 * 
 * Pexel is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * Pexel is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */
// @formatter:on
package me.dobrakmato.plugins.pexel.PexelCore.utils;

import java.util.ArrayList;
import java.util.List;

import me.dobrakmato.plugins.pexel.PexelCore.core.Pexel;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Utility class that help freezing of players.
 * 
 * @author Mato Kormuth
 * 
 */
public class PlayerFreezer implements Listener
{
	/**
	 * List of frozen textures. <br/>
	 * FIXME: Remove 'dead' disconnected player objects.
	 */
	private final List<Player>	frozen	= new ArrayList<Player>();
	
	public PlayerFreezer()
	{
		Bukkit.getPluginManager().registerEvents(this, Pexel.getCore());
	}
	
	/**
	 * Freezes (disables movement) the player.
	 * 
	 * @param player
	 *            player to freeze
	 */
	public void freeze(final Player player)
	{
		this.frozen.add(player);
	}
	
	/**
	 * Unfreezes (enables movement) the player.
	 * 
	 * @param player
	 *            player to unfreeze
	 */
	public void unfreeze(final Player player)
	{
		this.frozen.remove(player);
	}
	
	@EventHandler
	private void onPlayerMove(final PlayerMoveEvent event)
	{
		if (this.frozen.contains(event.getPlayer()))
			event.setTo(event.getFrom());
	}
}
