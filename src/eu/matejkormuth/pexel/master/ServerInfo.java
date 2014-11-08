package eu.matejkormuth.pexel.master;

/**
 * Class that represents server on network.
 */
public class ServerInfo {
    protected final String name;
    
    public ServerInfo(final String name) {
        this.name = name;
    }
    
    public void sendResponse(final Response response) {
        
    }
    
    public void sendRequest(final Request request) {
        
    }
    
    public String getName() {
        return this.name;
    }
}
