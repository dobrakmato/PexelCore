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
package me.dobrakmato.plugins.pexel.PexelNetworking;

import java.util.HashMap;
import java.util.Map;

import me.dobrakmato.plugins.pexel.PexelNetworking.packets.CrossServerTeleportPacket;

/**
 * @author Mato Kormuth
 * 
 */
public enum PacketType {
    CrossServerTeleportPacket(30, CrossServerTeleportPacket.class);
    
    private short                        id;
    private Class<? extends PexelPacket> clazz;
    
    private PacketType(final int id, final Class<? extends PexelPacket> clazz) {
        this.id = (short) id;
        this.clazz = clazz;
    }
    
    public short getId() {
        return this.id;
    }
    
    private Class<? extends PexelPacket> getClazz() {
        return this.clazz;
    }
    
    public static PacketType fromShort(final short id) {
        return PacketType.mappingShort.get(id);
    }
    
    public static PacketType fromClass(final Class<? extends PexelPacket> clazz) {
        return PacketType.mappingClass.get(clazz);
    }
    
    private static final Map<Short, PacketType>                        mappingShort = new HashMap<Short, PacketType>();
    private static final Map<Class<? extends PexelPacket>, PacketType> mappingClass = new HashMap<Class<? extends PexelPacket>, PacketType>();
    
    static {
        for (PacketType type : PacketType.values())
            if (PacketType.mappingShort.containsKey(type.getId()))
                throw new RuntimeException(
                        "Wrong mapping in PacketType! Can't store more than one type for one ID.");
            else
                
                PacketType.mappingShort.put(type.getId(), type);
        
        for (PacketType type : PacketType.values())
            if (PacketType.mappingClass.containsKey(type.getClass()))
                throw new RuntimeException(
                        "Wrong mapping in PacketType! Can't store more than one type for one CLASS.");
            else
                
                PacketType.mappingClass.put(type.getClazz(), type);
    }
}
