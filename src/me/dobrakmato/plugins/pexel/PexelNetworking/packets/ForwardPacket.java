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
package me.dobrakmato.plugins.pexel.PexelNetworking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import me.dobrakmato.plugins.pexel.PexelNetworking.AbstractPacket;

public class ForwardPacket extends AbstractPacket {
    public AbstractPacket packet;
    public String         serverName;
    
    @Override
    public void write(final DataOutputStream stream) throws IOException {
        
    }
    
    public static ForwardPacket read(final DataInputStream stream) throws IOException {
        ForwardPacket packet = new ForwardPacket();
        
        return packet;
    }
    
    @Override
    public void handleClient() {
        //No handling.
    }
    
    @Override
    public void handleServer() {
        //Forward to specified client.
        
    }
    
}
