package eu.matejkormuth.pexel.network;

import net.md_5.bungee.api.plugin.Plugin;

public class PexelMasterBungeePlugin extends Plugin {
    private static PexelMasterBungeePlugin instance;
    
    public PexelMasterBungeePlugin() {
        PexelMasterBungeePlugin.instance = this;
    }
    
    public void createMaster() {
        PexelMaster.setInstnace(new MasterServer("master"));
    }
    
    public static Plugin getInstance() {
        return PexelMasterBungeePlugin.instance;
    }
}
