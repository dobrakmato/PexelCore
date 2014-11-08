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
package eu.matejkormuth.pexel.master;

import java.util.HashMap;
import java.util.Map;

/**
 * Main class of master server.
 */
public class MasterServer {
    /**
     * Message channel used for messaging between servers.
     */
    public static final String               MESSAGE_CHANNEL = "Pexel|Main";
    
    private final Platform                   platform;
    private final Proxy                      proxy;
    private final Map<String, ServerInfo> slaves          = new HashMap<String, ServerInfo>();
    private final Logger                     log;
    
    protected MessageDecoder                 decoder;
    
    public MasterServer() {
        // Currently we support only proxy and only bungee.
        this.platform = Platform.MINECRAFT_PROXY;
        this.proxy = new BungeeProxy();
        
        // Logger.
        this.log = new Logger("PexelMaster");
        
        // Start message listener base on proxy brand.
        if (this.getProxy().getBrand() == ProxyBrand.BUNGEE_CORD) {
            new BungeeMessageComunicator();
        }
        
        // Create message decoder.
        this.decoder = new MessageDecoder(processors, listeners);
    }
    
    protected Logger getLogger() {
        return this.log;
    }
    
    /**
     * Returns platform the master is running on.
     * 
     * @return platform of master server
     */
    public Platform getPlatform() {
        return this.platform;
    }
    
    /**
     * Returns {@link Proxy} used by master server.
     * 
     * @return proxy object
     */
    public Proxy getProxy() {
        return this.proxy;
    }
    
    /**
     * Returns {@link ServerInfo} by name or null if not found.
     * 
     * @param name
     *            name of the server
     * @return networkserver object
     */
    public ServerInfo getServer(final String name) {
        return this.slaves.get(name);
    }
}
