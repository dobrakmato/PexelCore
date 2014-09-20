package eu.matejkormuth.pexel.PexelNetworking;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class that represents request.
 * 
 * @author Mato Kormuth
 * 
 */
public class Request {
    private final long           requestId;
    private final AbstractPacket requestContent;
    private ResponeEventHandler  onRespone;
    
    public Request(final AbstractPacket content) {
        this.requestId = Request.nextId();
        this.requestContent = content;
    }
    
    public Request(final AbstractPacket content, final long requestId) {
        this.requestId = requestId;
        this.requestContent = content;
    }
    
    protected void onRespone(final Respone respone) {
        this.onRespone.onRespone(this, respone);
    }
    
    public void setOnRespone(final ResponeEventHandler handler) {
        this.onRespone = handler;
    }
    
    public long getRequestId() {
        return this.requestId;
    }
    
    public AbstractPacket getContent() {
        return this.requestContent;
    }
    
    public static final long nextId() {
        if (Request.lastId.containsKey(Server.THIS_SERVER.hashCode())) {
            return ((long) Server.THIS_SERVER.hashCode() << 32)
                    | (Request.lastId.get(Server.THIS_SERVER.hashCode()).getAndIncrement() & 0xFFFFFFFL);
        }
        else {
            Request.lastId.put(Server.THIS_SERVER.hashCode(), new AtomicInteger());
            return nextId();
        }
        
    }
    
    public static final long nextId(final int clientUID) {
        if (Request.lastId.containsKey(clientUID)) {
            return ((long) clientUID << 32)
                    | (Request.lastId.get(clientUID).getAndIncrement() & 0xFFFFFFFL);
        }
        else {
            Request.lastId.put(clientUID, new AtomicInteger());
            return nextId();
        }
        
    }
    
    private static Map<Integer, AtomicInteger> lastId = new HashMap<Integer, AtomicInteger>();
}
