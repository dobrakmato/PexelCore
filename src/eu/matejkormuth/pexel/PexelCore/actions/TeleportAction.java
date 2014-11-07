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
package eu.matejkormuth.pexel.PexelCore.actions;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import eu.matejkormuth.pexel.PexelCore.Pexel;
import eu.matejkormuth.pexel.PexelNetworking.Server;

/**
 * Inventory action that teleports player to specified location.
 * 
 * @author Mato Kormuth
 * 
 */
public class TeleportAction implements Action {
    /**
     * Target location.
     */
    private final Location location;
    /**
     * Target server.
     */
    private Server         server;
    
    /**
     * Creates a new action.
     * 
     * @param location
     */
    public TeleportAction(final Location location) {
        this.location = location;
    }
    
    /**
     * Creates a new action.
     * 
     * @param location
     */
    public TeleportAction(final Location location, final Server server) {
        this.location = location;
        this.server = server;
    }
    
    @Override
    public void execute(final Player player) {
        if (this.server.isLocalServer()) {
            // Just teleport player to target location.
            player.teleport(this.location);
        }
        else {
            // TODO: Add teleport to location. Perform server-wide teleport
            
            // Teleport to other server when using Bungee.
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(this.server.getBungeeName());
            player.sendPluginMessage(Pexel.getCore(), "BungeeCord", out.toByteArray());
        }
    }
}
