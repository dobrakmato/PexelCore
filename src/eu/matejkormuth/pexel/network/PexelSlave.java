package eu.matejkormuth.pexel.network;

public class PexelSlave {
    private static SlaveServer instance;
    
    public static final void setInstnace(final SlaveServer instance) {
        PexelSlave.instance = instance;
    }
    
    public static final SlaveServer getInstance() {
        return PexelSlave.instance;
    }
}
