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

/**
 * Class specifing pexel compactibile server.
 * 
 * @author Mato Kormuth
 * 
 */
public class Server {
    public static final Server      THIS_SERVER = new Server(null, "$#__THIS__#$");
    
    private final PexelServerClient client;
    private final String            name;
    private final String            bungeeName;
    
    /**
     * Creates new server with same name and bungeename.
     * 
     * @param client
     * @param name
     */
    public Server(final PexelServerClient client, final String name) {
        this.client = client;
        this.name = name;
        this.bungeeName = name;
    }
    
    /**
     * Creates a new server with different name and bungeename.
     * 
     * @param client
     * @param name
     * @param bungeeName
     */
    public Server(final PexelServerClient client, final String name,
            final String bungeeName) {
        this.client = client;
        this.name = name;
        this.bungeeName = bungeeName;
    }
    
    public boolean isLocalServer() {
        return this.name.equals("$#__THIS__#$");
    }
    
    public PexelServerClient getClient() {
        return this.client;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isConnectedToMaster() {
        return this.client.isConnected();
    }
    
    public String getBungeeName() {
        return this.bungeeName;
    }
}
