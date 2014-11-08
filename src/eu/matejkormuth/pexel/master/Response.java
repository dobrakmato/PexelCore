package eu.matejkormuth.pexel.master;

/**
 * Class that specifies response.
 */
public abstract class Response {
    protected long requestID;
    
    public Response(final long requestID) {
        this.requestID = requestID;
    }
}