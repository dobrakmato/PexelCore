package eu.matejkormuth.pexel.master;

/**
 * Class that specifies request.
 */
public abstract class Request {
    protected final long requestID;
    
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
