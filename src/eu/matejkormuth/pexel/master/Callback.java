package eu.matejkormuth.pexel.master;

/**
 * Class that represents callback.
 */
public abstract class Callback<T> {
    protected long requestID;
    
    public long getRequestID() {
        return this.requestID;
    }
    
    public abstract void response(T response);
    
    @SuppressWarnings("unchecked")
    protected void call(final Response response) {
        this.response((T) response);
    }
}
