package eu.matejkormuth.pexel.network;

/**
 * Interface that specifies that this class can handle messages (payloads).
 */
public interface PayloadHandler {
    void handleMessage(ServerInfo sender, byte[] payload);
}
