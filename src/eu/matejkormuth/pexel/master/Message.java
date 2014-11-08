package eu.matejkormuth.pexel.master;

/**
 * Class that represents message over network.
 */
public abstract class Message {
    /**
     * ID of request that involved creation of this message.
     */
    protected long requestID;
    
    /**
     * Returns byte array representation of this message.
     * 
     * @return
     */
    public abstract byte[] toByteArray();
    
    /**
     * Should constrct Message ({@link Request}, {@link Response}, ...) from byte array.
     * 
     * @param array
     *            array containing data
     */
    public abstract void fromByteArray(byte[] array);
}
