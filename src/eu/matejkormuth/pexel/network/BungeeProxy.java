package eu.matejkormuth.pexel.network;

import net.md_5.bungee.BungeeCord;

/**
 * Class that executes {@link Proxy} functions on BungeeCord server.
 */
public class BungeeProxy implements Proxy {
    @Override
    public ProxyBrand getBrand() {
        return ProxyBrand.BUNGEE_CORD;
    }
    
    @Override
    public void connect(final PlayerInfo player, final ServerInfo target) {
        BungeeCord.getInstance().getPlayer(player.uuid).connect(
                BungeeCord.getInstance().getServerInfo(target.getName()));
    }
}
