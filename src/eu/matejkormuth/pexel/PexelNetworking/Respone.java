package eu.matejkormuth.pexel.PexelNetworking;

/**
 * Class that represents respone.
 * 
 * @author Mato Kormuth
 * 
 */
public class Respone {
    private final long           requestId;
    private final AbstractPacket responeContent;
    
    public Respone(final long requestId, final AbstractPacket responeContent) {
        this.requestId = requestId;
        this.responeContent = responeContent;
    }
    
    public long getRequestId() {
        return this.requestId;
    }
    
    public AbstractPacket getContent() {
        return this.responeContent;
    }
}
