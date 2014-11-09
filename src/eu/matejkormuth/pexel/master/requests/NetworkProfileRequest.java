package eu.matejkormuth.pexel.master.requests;

import java.util.UUID;

import eu.matejkormuth.pexel.master.responses.NetworkProfileResponse;
import eu.matejkormuth.pexel.network.AsyncRequest;
import eu.matejkormuth.pexel.network.Callback;

public class NetworkProfileRequest extends AsyncRequest {
    public NetworkProfileRequest(final UUID uuid,
            final Callback<NetworkProfileResponse> callback) {
        super(callback);
    }
}
