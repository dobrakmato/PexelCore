package eu.matejkormuth.pexel.master;

/**
 * Class that specifies response.
 */
public abstract class Response extends Message {
    protected long requestID;
    
    public Response(final long requestID) {
        this.requestID = requestID;
    }
}
