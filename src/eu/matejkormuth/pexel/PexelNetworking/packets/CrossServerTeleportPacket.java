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
package eu.matejkormuth.pexel.PexelNetworking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import eu.matejkormuth.pexel.PexelNetworking.AbstractPacket;

public class CrossServerTeleportPacket extends AbstractPacket {
    public UUID     player;
    public Location loc;
    
    public CrossServerTeleportPacket(final UUID player, final Location location) {
        this.player = player;
        this.loc = location;
    }
    
    private CrossServerTeleportPacket() {
    }
    
    @Override
    public void write(final DataOutputStream stream) throws IOException {
        stream.writeUTF(this.player.toString());
        stream.writeDouble(this.loc.getX());
        stream.writeDouble(this.loc.getY());
        stream.writeDouble(this.loc.getZ());
        stream.writeFloat(this.loc.getYaw());
        stream.writeFloat(this.loc.getPitch());
        stream.writeUTF(this.loc.getWorld().getName());
    }
    
    public static CrossServerTeleportPacket read(final DataInputStream stream)
            throws IOException {
        CrossServerTeleportPacket packet = new CrossServerTeleportPacket();
        
        packet.player = UUID.fromString(stream.readUTF());
        
        double x = stream.readDouble();
        double y = stream.readDouble();
        double z = stream.readDouble();
        float yaw = stream.readFloat();
        float pitch = stream.readFloat();
        String worldName = stream.readUTF();
        
        packet.loc = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
        
        return packet;
    }
    
    @Override
    public void handleClient() {
        //Add to teleport queue, expire in 30 seconds.
    }
    
    @Override
    public void handleServer() {
        //No handling on master server.
    }
}
