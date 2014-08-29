package me.dobrakmato.plugins.pexel.PexelCore;

import org.bukkit.scoreboard.Scoreboard;

/**
 * Class that specifies, that this class could be used with scoreboard manager.
 * 
 * @author Mato Kormuth
 * 
 */
public interface ScoreboardWrapper
{
	/**
	 * Returns scoreboard object.
	 * 
	 * @return the scoreboard objects
	 */
	public Scoreboard getScoreboard();
}
