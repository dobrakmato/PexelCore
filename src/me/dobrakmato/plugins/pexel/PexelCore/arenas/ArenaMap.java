package me.dobrakmato.plugins.pexel.PexelCore.arenas;

import java.util.HashMap;
import java.util.Map;

import me.dobrakmato.plugins.pexel.PexelCore.minigame.Minigame;

/**
 * Class that represents playable map.
 * 
 * @author Mato Kormuth
 * 
 */
public class ArenaMap {
    private Minigame                  minigame;
    private String                    name;
    private final Map<String, String> options = new HashMap<String, String>();
    
    public String getOption(final String key) {
        return this.options.get(key);
    }
    
    public Minigame getMinigame() {
        return this.minigame;
    }
    
    public String getName() {
        return this.name;
    }
}
