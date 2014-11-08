package eu.matejkormuth.pexel.master;

/**
 * Interface that specifies that this class can handle messages (payloads).
 */
public interface PayloadHandler {
    void receiveMessage(ServerInfo sender, byte[] payload);
}
