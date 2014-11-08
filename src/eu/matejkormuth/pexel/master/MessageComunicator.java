package eu.matejkormuth.pexel.master;

/**
 * Interface that represents plugin message comunicator.
 */
public interface MessageComunicator {
    /**
     * Called when payload arrives at server.
     * 
     * @param sender
     *            server that sent this paylod
     * @param payload
     *            data
     */
    public void receive(ServerInfo sender, byte[] payload);
    
    /**
     * Sends data to target server.
     * 
     * @param target
     *            target server
     * @param payload
     *            data
     */
    public void send(ServerInfo target, byte[] payload);
}
