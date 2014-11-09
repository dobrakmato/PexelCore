package eu.matejkormuth.pexel.master;

import eu.matejkormuth.pexel.master.requests.ServerResourcesRequest;
import eu.matejkormuth.pexel.master.responses.ServerResourcesResponse;

/**
 * Responder that creates respones to requests.
 */
public class DefaultRequestResponder {
    public ServerResourcesResponse onRequest(final ServerResourcesRequest request) {
        return new ServerResourcesResponse(Runtime.getRuntime().maxMemory(),
                Runtime.getRuntime().freeMemory());
    }
}
