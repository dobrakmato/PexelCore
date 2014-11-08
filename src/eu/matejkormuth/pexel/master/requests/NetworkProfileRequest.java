package eu.matejkormuth.pexel.master.requests;

import java.util.UUID;

import eu.matejkormuth.pexel.master.AsyncRequest;
import eu.matejkormuth.pexel.master.Callback;
import eu.matejkormuth.pexel.master.responses.NetworkProfileResponse;

public class NetworkProfileRequest extends AsyncRequest {
    public NetworkProfileRequest(final UUID uuid,
            final Callback<NetworkProfileResponse> callback) {
        super(callback);
    }
}
