package eu.matejkormuth.pexel.master;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;

public class BungeePluginMessageComunicator extends PluginMessageComunicator implements
        Listener {
    
    public BungeePluginMessageComunicator(final PayloadHandler handler) {
        super(handler);
        // Register events.
        BungeeCord.getInstance().pluginManager.registerListener(
                BungeePlugin.getInstance(), this);
    }
    
    public void onPluginMessage(final PluginMessageEvent event) {
        if (event.getTag().equals(MasterServer.MESSAGE_CHANNEL)) {
            if (event.getSender() instanceof ServerConnection) {
                this.onReceive(
                        PexelMaster.getInstance().getServer(
                                ((ServerConnection) event.getSender()).getInfo().getName()),
                        event.getData());
            }
        }
    }
    
    @Override
    public void send(final ServerInfo target, final byte[] payload) {
        BungeeCord.getInstance().getServerInfo(target.getName()).sendData(
                MasterServer.MESSAGE_CHANNEL, payload);
    }
}
