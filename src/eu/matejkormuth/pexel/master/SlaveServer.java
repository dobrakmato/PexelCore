package eu.matejkormuth.pexel.master;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class SlaveServer extends ServerInfo implements Requestable {
    
    // Requestable interface
    protected AtomicLong             lastRequestID = new AtomicLong();
    protected Map<Long, Callback<?>> callbacks     = new HashMap<Long, Callback<?>>(255);
    
    protected MessageComunicator     comunicator;
    
    public SlaveServer(final String name) {
        super(name);
        
        this.side = ServerSide.LOCAL;
        
        this.comunicator =
        
        ServerInfo.setLocalServer(this);
    }
    
    protected SlaveServer(final boolean fromMaster, final String name) {
        super(name);
        
        // Does not register this as local server.
        this.side = ServerSide.REMOTE;
    }
    
    public void sendToMaster(final Message message) {
        
    }
    
    @Override
    public void sendRequest(final Request request) {
        if (this.side == ServerSide.REMOTE) {
            // Sending from master
            PexelMaster.getInstance().send(request, this);
        }
        else {
            throw new RuntimeException("Can't send request to local server.");
        }
    }
    
    @Override
    public void sendResponse(final Response response) {
        if (this.side == ServerSide.REMOTE) {
            // Sending from master
            PexelMaster.getInstance().send(response, this);
        }
        else {
            throw new RuntimeException("Can't send response to local server.");
        }
    }
    
    @Override
    public long nextRequestID() {
        return this.lastRequestID.getAndIncrement();
    }
    
    @Override
    public void registerCallback(final long requestID, final Callback<?> callback) {
        this.callbacks.put(requestID, callback);
    }
    
    @Override
    public Callback<?> getCallback(final long requestID) {
        return this.callbacks.get(requestID);
    }
    
    @Override
    public void removeCallback(final long requestID) {
        this.callbacks.remove(requestID);
    }
}
