package eu.matejkormuth.pexel.master;

import eu.matejkormuth.pexel.master.requests.NetworkProfileRequest;
import eu.matejkormuth.pexel.master.responses.NetworkProfileResponse;

/**
 * Responder that creates respones to requests.
 */
public class RequestProcessor {
    
    public Response createResponse(final Request request) {
        if (request instanceof NetworkProfileRequest) {
            return new NetworkProfileResponse(profile);
        }
        else {
            return null;
        }
    }
}
