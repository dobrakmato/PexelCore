package eu.matejkormuth.pexel.slave;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import eu.matejkormuth.pexel.slave.chat.ChannelSubscriber;
import eu.matejkormuth.pexel.slave.chat.ChatManager;
import eu.matejkormuth.pexel.slave.chat.SubscribeMode;
import eu.matejkormuth.pexel.slave.core.Log;

public class PNBroadcastServer {
    private final PNBSubscriber subscriber;
    private PNBWebSocketServer  websockserv;
    
    public PNBroadcastServer() {
        Log.partEnable("PNBService");
        this.subscriber = new PNBSubscriber();
        ChatManager.CHANNEL_NETWORK.subscribe(this.subscriber);
        try {
            this.websockserv = new PNBWebSocketServer();
            this.websockserv.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            this.websockserv = null;
        }
    }
    
    public void broadcast(final String message) {
        try {
            for (WebSocket ws : this.websockserv.connections()) {
                ws.send(message);
            }
        } catch (Exception e) {
            Log.severe("[PNB] Cannot broadcast: " + e.toString());
        }
    }
    
    class PNBSubscriber implements ChannelSubscriber {
        @Override
        public void sendMessage(final String message) {
            PNBroadcastServer.this.broadcast(message);
        }
        
        @Override
        public SubscribeMode getMode() {
            return SubscribeMode.READ;
        }
        
        @Override
        public boolean isOnline() {
            return true;
        }
        
        @Override
        public String getName() {
            return "PNBroadcastService";
        }
        
    }
    
    class PNBWebSocketServer extends WebSocketServer {
        public PNBWebSocketServer() throws UnknownHostException {
            super(new InetSocketAddress(8877));
            Log.partEnable("PNBService-WebSocketServer");
        }
        
        @Override
        public void onClose(final WebSocket arg0, final int arg1, final String arg2,
                final boolean arg3) {
            Log.info("[PNBWS] Client " + arg0.getRemoteSocketAddress().getHostString()
                    + " disconnected (" + arg1 + ") [" + arg2 + "]");
        }
        
        @Override
        public void onError(final WebSocket arg0, final Exception arg1) {
            Log.severe("[PNBWS] ws: " + arg0.getRemoteSocketAddress().toString() + "; "
                    + arg1.toString());
        }
        
        @Override
        public void onMessage(final WebSocket arg0, final String arg1) {
            Log.info("Received message from "
                    + arg0.getRemoteSocketAddress().getHostString() + "; cont: " + arg1);
            if (arg1.equalsIgnoreCase("command requestall")) {
                
            }
            else {
                arg0.send("Bad request! Can't respond!");
            }
        }
        
        @Override
        public void onOpen(final WebSocket arg0, final ClientHandshake arg1) {
            //does nothing
            Log.info("[PNBWS] Client " + arg0.getRemoteSocketAddress().getHostString()
                    + " has connected!");
            arg0.send("Welcome! Current time: " + System.currentTimeMillis());
        }
        
    }
}
