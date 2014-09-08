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
package me.dobrakmato.plugins.pexel.PexelCore.core;

import me.dobrakmato.plugins.pexel.PexelCore.actions.Action;

import org.bukkit.entity.Player;

/**
 * Class representating teleport portal.
 * 
 * @author Mato Kormuth
 * 
 */
public class TeleportGate {
    private final Region region;
    private Action       action;
    
    /**
     * Creates new teleport gate in specified region, with specified action that will be executed when PlayerPortalEvent
     * is thrown for this gate.
     * 
     * @param region
     *            region of gate
     * @param action
     *            action to be executed
     */
    public TeleportGate(final Region region, final Action action) {
        this.region = region;
        this.action = action;
    }
    
    /**
     * Retruns region of gate.
     * 
     * @return
     */
    public Region getRegion() {
        return this.region;
    }
    
    /**
     * Executes specified action on specified player.
     * 
     * @param player
     *            player to execute action to
     */
    protected void teleport(final Player player) {
        this.action.execute(player);
    }
    
    /**
     * Returns action of this gate.
     * 
     * @return action
     */
    public Action getAction() {
        return this.action;
    }
    
    /**
     * Sets action to this teleport gate.
     * 
     * @param action
     *            action
     */
    public void setAction(final Action action) {
        this.action = action;
    }
}
