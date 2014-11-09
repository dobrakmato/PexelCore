package eu.matejkormuth.pexel.network;

/**
 * Class that is used for handling callbacks.
 */
public class CallbackHandler {
    private final Requestable requestable;
    
    public CallbackHandler(final Requestable requestable) {
        this.requestable = requestable;
    }
    
    /**
     * Called when response arrives at server.
     * 
     * @param response
     *            response that arrived
     */
    public void onResponse(final Response response) {
        Callback<?> cb = this.requestable.getCallback(response.requestID);
        if (cb != null) {
            cb.call(response);
            this.requestable.removeCallback(response.requestID);
        }
    }
}
