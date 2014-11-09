package eu.matejkormuth.pexel.network;

public final class PexelMaster {
    private static MasterServer instance;
    
    public static final void setInstnace(final MasterServer instance) {
        PexelMaster.instance = instance;
    }
    
    public static final MasterServer getInstance() {
        return PexelMaster.instance;
    }
}
