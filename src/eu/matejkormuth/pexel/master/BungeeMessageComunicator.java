package eu.matejkormuth.pexel.master;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;

public class BungeeMessageComunicator implements MessageComunicator, Listener {
    
    public BungeeMessageComunicator() {
        // Register events.
        BungeeCord.getInstance().pluginManager.registerListener(
                BungeePlugin.getInstance(), this);
    }
    
    public void onPluginMessage(final PluginMessageEvent event) {
        if (event.getTag().equals(MasterServer.MESSAGE_CHANNEL)) {
            if (event.getSender() instanceof ServerConnection) {
                this.receive(
                        PexelMaster.getInstance().getServer(
                                ((ServerConnection) event.getSender()).getInfo().getName()),
                        event.getData());
            }
        }
    }
    
    @Override
    public void receive(final ServerInfo sender, final byte[] payload) {
        PexelMaster.getInstance().receiveMessage(sender, payload);
    }
    
    @Override
    public void send(final ServerInfo target, final byte[] payload) {
        BungeeCord.getInstance().getServerInfo(target.getName()).sendData(
                MasterServer.MESSAGE_CHANNEL, payload);
    }
}
