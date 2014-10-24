package eu.matejkormuth.pexel.PexelCore.teams;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.junit.Test;
import org.mockito.Mockito;

public class TeamTest {
    @Test
    public void declaration() {
        Team team1 = new Team(Color.RED, "Team name", 20);
        assertEquals("team color", Color.RED, team1.getColor());
        assertEquals("team name", "Team name", team1.getName());
        assertEquals("max players", 20, team1.getMaximumPlayers());
    }
    
    @Test
    public void joinleft() {
        Team team = new Team(Color.RED, "name", 1);
        Player p = Mockito.mock(Player.class);
        team.addPlayer(p);
        assertTrue("player in team", team.contains(p));
        team.removePlayer(p);
        assertFalse("player not in team", team.contains(p));
    }
}
