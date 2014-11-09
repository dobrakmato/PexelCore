package eu.matejkormuth.pexel.network.responses;

import eu.matejkormuth.pexel.network.PlayerInfo;
import eu.matejkormuth.pexel.network.Response;

public class NetworkProfileResponse extends Response {
    protected PlayerInfo profile;
    
    public NetworkProfileResponse(final long requestID, final PlayerInfo profile) {
        super(requestID);
        this.profile = profile;
    }
    
    public PlayerInfo getProfile() {
        return this.profile;
    }
}
