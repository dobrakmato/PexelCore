package eu.matejkormuth.pexel.master;

/**
 * Class that represents server on network.
 */
public class ServerInfo {
    private static ServerInfo local;
    
    protected final String    name;
    protected ServerSide      side;
    
    public ServerInfo(final String name) {
        this.name = name;
    }
    
    public void sendResponse(final Response response) {
        throw new OperationNotSupportedException(
                "ServerInfo does not support sending responses!");
    }
    
    public void sendRequest(final Request request) {
        throw new OperationNotSupportedException(
                "ServerInfo does not support sending requests!");
    }
    
    public boolean isLocal() {
        return this.side == ServerSide.LOCAL;
    }
    
    public ServerSide getSide() {
        return this.side;
    }
    
    public String getName() {
        return this.name;
    }
    
    public static ServerInfo localServer() {
        return ServerInfo.local;
    }
    
    protected static void setLocalServer(final ServerInfo server) {
        if (ServerInfo.local == null) {
            ServerInfo.local = server;
        }
        else {
            throw new RuntimeException("Field local has been initialized before!");
        }
    }
}
