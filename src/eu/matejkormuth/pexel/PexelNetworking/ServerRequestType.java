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
package eu.matejkormuth.pexel.PexelNetworking;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum specificating <i>pexel protocol</i>.
 * 
 * @author Mato Kormuth
 * 
 */
public enum ServerRequestType {
    ONLINEPLAYERS_LIST(0),
    MINIGAMES_LIST(1),
    ARENAS_LIST(2),
    
    ARENA_DISABLE(3),
    ARENA_ENABLE(4),
    ARENA_GETPLAYERS(5),
    ARENA_GETFIELDS(6),
    ARENA_GETINFO(7),
    
    BASIC_SERVERINFO(8),
    PLAYER_INFO(9),
    
    ARENA_INFO_BASIC(10),
    ARENA_INFO_REFLECTION(11);
    
    private int id;
    
    private ServerRequestType(final int id) {
        this.id = (short) id;
    }
    
    public int getId() {
        return this.id;
    }
    
    private static Map<Integer, ServerRequestType> mapping = new HashMap<Integer, ServerRequestType>();
    
    static {
        for (ServerRequestType type : ServerRequestType.values())
            ServerRequestType.mapping.put(type.getId(), type);
    }
    
    public static ServerRequestType fromInt(final int typeId) {
        return ServerRequestType.mapping.get(typeId);
    }
}
