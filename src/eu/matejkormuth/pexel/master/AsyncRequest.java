package eu.matejkormuth.pexel.master;

public abstract class AsyncRequest extends Request {
    public AsyncRequest(final Callback<?> callback) {
        super();
        callback.requestID = this.requestID;
    }
}
