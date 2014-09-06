package me.dobrakmato.plugins.pexel.KingdomWars;

import me.dobrakmato.plugins.pexel.PexelCore.arenas.AdvancedMinigameArena;
import me.dobrakmato.plugins.pexel.PexelCore.core.Region;
import me.dobrakmato.plugins.pexel.PexelCore.minigame.Minigame;
import me.dobrakmato.plugins.pexel.PexelCore.teams.Team;

import org.bukkit.Color;
import org.bukkit.Location;

/**
 * @author Mato Kormuth
 * 
 */
public class KingdomWarsArena extends AdvancedMinigameArena
{
    private final Team redTeam  = new Team(Color.RED, "Red team", this.slots / 2);
    private final Team blueTeam = new Team(Color.RED, "Blue team", this.slots / 2);
    
    public KingdomWarsArena(final Minigame minigame, final String arenaName,
            final Region region, final int maxPlayers, final int minPlayers,
            final Location lobbyLocation, final Location gameSpawn)
    {
        super(minigame, arenaName, region, maxPlayers, minPlayers, lobbyLocation,
                gameSpawn);
        
        this.setCountdownLenght(60);
    }
    
}
