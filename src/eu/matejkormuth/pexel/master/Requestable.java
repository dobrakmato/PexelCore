package eu.matejkormuth.pexel.master;

/**
 * Class that supports receiving and sending requests / reponses.
 */
public interface Requestable {
    /**
     * Returns next unique request id for this requestable.
     * 
     * @return unique request id
     */
    public long nextRequestID();
    
    /**
     * Registers specifies callback with specified request id. Callback will be called, when response to request
     * arrives.
     * 
     * @param requestID
     *            id of request
     * @param callback
     *            callback
     */
    public void registerCallback(long requestID, Callback<?> callback);
    
    /**
     * Returns callback by request id.
     * 
     * @param requestID
     *            request id
     */
    public Callback<?> getCallback(long requestID);
    
    /**
     * Removes callback for specified request id.
     * 
     * @param requestID
     *            request id
     */
    public void removeCallback(long requestID);
}
