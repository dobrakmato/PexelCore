package eu.matejkormuth.pexel.master.responses;

import eu.matejkormuth.pexel.master.PlayerInfo;
import eu.matejkormuth.pexel.master.Response;

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
