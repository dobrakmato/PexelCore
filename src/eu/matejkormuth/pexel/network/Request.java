package eu.matejkormuth.pexel.network;

/**
 * Class that specifies request.
 */
public abstract class Request extends Message {
    protected long       requestID;
    // Null when sending, hold sender object when processing.
    protected ServerInfo sender;
    
    public Request() {
        this.requestID = 0;
    }
    
    /**
     * Sends this request to specified server.
     * 
     * @param target
     *            target server.
     */
    public void send(final ServerInfo target) {
        target.sendRequest(this);
    }
}
