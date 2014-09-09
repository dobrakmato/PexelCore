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
package me.dobrakmato.plugins.pexel.PexelCore.areas;

import org.bukkit.entity.Player;

/**
 * Player area owner.
 * 
 * @author Mato Kormuth
 * 
 */
public class PlayerAreaOwner implements AreaOwner {
    /**
     * Player who is owner.
     */
    private final Player owner;
    
    /**
     * Initializes new instance of area owner with specified player.
     * 
     * @param player
     */
    public PlayerAreaOwner(final Player player) {
        this.owner = player;
    }
    
    @Override
    public String getName() {
        return this.owner.getName();
    }
}
