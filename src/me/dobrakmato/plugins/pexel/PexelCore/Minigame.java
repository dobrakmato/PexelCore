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
package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.Collection;
import java.util.List;

import org.bukkit.Location;

/**
 * Interface for minigame.
 * 
 * @author Mato Kormuth
 * 
 */
public interface Minigame
{
	/**
	 * Returns display name of minigame.
	 * 
	 * @return display name
	 */
	public String getDisplayName();
	
	/**
	 * Returns code safe name of minigame.
	 * 
	 * @return code safe name
	 */
	public String getName();
	
	/**
	 * Returns minigame category.
	 * 
	 * @return minigame categorry
	 */
	public MinigameCategory getCategory();
	
	/**
	 * Returns minigame types.
	 * 
	 * @return all minigame types.
	 */
	public List<MinigameType> getTypes();
	
	/**
	 * Returns arena by name.
	 * 
	 * @param name
	 *            arena's name
	 * @return
	 */
	@Deprecated
	public MinigameArena getArena(String name);
	
	/**
	 * Returns all arenas of this minigame.
	 * 
	 * @return
	 */
	@Deprecated
	public Collection<MinigameArena> getArenas();
	
	/**
	 * Returns the minigame lobby location.
	 * 
	 * @return location of minigame's lobby
	 */
	public Location getLobbyLocation();
}
