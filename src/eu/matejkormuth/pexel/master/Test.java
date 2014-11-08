package eu.matejkormuth.pexel.master;

import java.util.UUID;

import eu.matejkormuth.pexel.master.requests.NetworkProfileRequest;
import eu.matejkormuth.pexel.master.responses.NetworkProfileResponse;

public class Test {
    public void a() {
        new NetworkProfileRequest(UUID.randomUUID(),
                new Callback<NetworkProfileResponse>() {
                    
                    @Override
                    public void response(final NetworkProfileResponse response) {
                        // TODO Auto-generated method stub
                        
                    }
                }).send(PexelSlave.getInstance().getMasterServer());
    }
}
