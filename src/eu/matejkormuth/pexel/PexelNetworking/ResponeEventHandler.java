package eu.matejkormuth.pexel.PexelNetworking;

/**
 * Respone event handler.
 */
public interface ResponeEventHandler {
    /**
     * Called from network thread when respone arrives on client machine.
     * 
     * @param requset
     *            request that involved this action
     * @param respone
     *            respone to the request
     */
    public void onRespone(Request requset, Respone respone);
}
