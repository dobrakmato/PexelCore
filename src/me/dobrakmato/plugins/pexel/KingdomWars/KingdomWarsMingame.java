package me.dobrakmato.plugins.pexel.KingdomWars;

import java.util.Collection;
import java.util.List;

import me.dobrakmato.plugins.pexel.PexelCore.Pexel;
import me.dobrakmato.plugins.pexel.PexelCore.arenas.MinigameArena;
import me.dobrakmato.plugins.pexel.PexelCore.core.StorageEngine;
import me.dobrakmato.plugins.pexel.PexelCore.minigame.Minigame;
import me.dobrakmato.plugins.pexel.PexelCore.minigame.MinigameCategory;
import me.dobrakmato.plugins.pexel.PexelCore.minigame.MinigameType;

import org.bukkit.Location;

/**
 * @author Mato Kormuth
 * 
 */
public class KingdomWarsMingame implements Minigame
{
    public KingdomWarsMingame()
    {
        Pexel.getMatchmaking().registerMinigame(this);
        StorageEngine.addMinigame(this);
        StorageEngine.registerArenaAlias(KingdomWarsArena.class, "KingdomWarsArena");
        this.makeArenas();
    }
    
    private void makeArenas()
    {
        //KingdomWarsArena arena = new KingdomWarsArena(this, "arena1", region,
        //		32, 8, lobbyLocation, gameSpawn);
        
        //StorageEngine.addArena(arena);
        //Pexel.getMatchmaking().registerArena(arena);
    }
    
    @Override
    public String getDisplayName()
    {
        return "Kingdom Wars";
    }
    
    @Override
    public String getName()
    {
        return "kingdomwars";
    }
    
    @Override
    public MinigameCategory getCategory()
    {
        return MinigameCategory.TOURNAMENT;
    }
    
    @Override
    public List<MinigameType> getTypes()
    {
        return MinigameType.makeTypes();
    }
    
    @Override
    public MinigameArena getArena(final String name)
    {
        return null;
    }
    
    @Override
    public Collection<MinigameArena> getArenas()
    {
        return null;
    }
    
    @Override
    public Location getLobbyLocation()
    {
        return Pexel.getHubLocation();
    }
    
}
