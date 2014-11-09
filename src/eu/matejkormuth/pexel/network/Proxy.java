package eu.matejkormuth.pexel.network;

/**
 * Interface that represents proxy.
 */
public interface Proxy {
    /**
     * Returns brand of this proxy server.
     * 
     * @return brand of this server
     */
    public ProxyBrand getBrand();
    
    /**
     * Transfers specified player to spcified server.
     * 
     * @param player
     *            player to transfer
     * @param target
     *            target server
     */
    public void connect(PlayerInfo player, ServerInfo target);
}
