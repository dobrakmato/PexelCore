package eu.matejkormuth.pexel.master;

import eu.matejkormuth.pexel.master.requests.ServerResourcesRequest;
import eu.matejkormuth.pexel.master.responses.ServerResourcesResponse;

/**
 * Protocol that supports all pexel request and responses.
 */
public class PexelProtocol extends Protocol {
    public PexelProtocol() {
        this.registerRequest(1, ServerResourcesRequest.class);
        
        this.registerResponse(1, ServerResourcesResponse.class);
    }
}
