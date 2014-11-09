package eu.matejkormuth.pexel.network;

public abstract class AsyncRequest extends Request {
    public AsyncRequest(final Callback<?> callback) {
        super();
        callback.requestID = this.requestID;
    }
}
