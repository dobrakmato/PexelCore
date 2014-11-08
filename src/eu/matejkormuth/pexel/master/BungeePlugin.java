package eu.matejkormuth.pexel.master;

import net.md_5.bungee.api.plugin.Plugin;

public class BungeePlugin extends Plugin {
    private static BungeePlugin instance;
    
    public BungeePlugin() {
        
        BungeePlugin.instance = this;
    }
    
    public void createMaster() {
        PexelMaster.setInstnace(new MasterServer());
    }
    
    public static Plugin getInstance() {
        return BungeePlugin.instance;
    }
}
