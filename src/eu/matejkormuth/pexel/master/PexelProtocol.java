package eu.matejkormuth.pexel.master;

import eu.matejkormuth.pexel.master.requests.RegisterSlaveRequest;

/**
 * Protocol that supports all pexel request and responses.
 */
public class PexelProtocol extends Protocol {
    public PexelProtocol() {
        // Requests.
        this.registerRequest(1, RegisterSlaveRequest.class);
        
        // Responses.
    }
}
