package eu.matejkormuth.pexel.network;

/**
 * Interface that represents plugin message comunicator.
 */
public abstract class MessageComunicator {
    private final PayloadHandler handler;
    
    public MessageComunicator(final PayloadHandler handler) {
        this.handler = handler;
    }
    
    /**
     * Called when payload arrives, and should be redirected to {@link PayloadHandler}.
     * 
     * @param sender
     *            server that sent this payload (raw data)
     * @param payload
     *            data
     */
    public void onReceive(final ServerInfo sender, final byte[] payload) {
        this.handler.handleMessage(sender, payload);
    }
    
    /**
     * Sends raw data (payload) to target server.
     * 
     * @param target
     *            target server
     * @param payload
     *            data
     */
    public abstract void send(ServerInfo target, byte[] payload);
    
    /**
     * Stops all connections.
     */
    public abstract void stop();
}
