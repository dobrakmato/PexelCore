package eu.matejkormuth.pexel.network;

/**
 * Class that holds message paylod.
 */
public class NettyMessage {
    public NettyMessage(final byte[] array) {
        this.payload = array;
    }
    
    public byte[] payload;
}
