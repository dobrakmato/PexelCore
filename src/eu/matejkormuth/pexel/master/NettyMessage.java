package eu.matejkormuth.pexel.master;

/**
 * Class that holds message paylod.
 */
public class NettyMessage {
    public NettyMessage(final byte[] array) {
        this.payload = array;
    }
    
    public byte[] payload;
}
