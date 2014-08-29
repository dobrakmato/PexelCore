package me.dobrakmato.plugins.pexel.PexelCore;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Manager for scoreboard.
 * 
 * @author Mato Kormuth
 * 
 */
public class ScoreboardManager
{
	private final ScoreboardWrapper	scoreboard;
	
	/**
	 * Creates new scoreboard manager, with specified scoreboard.
	 * 
	 * @param scoreboard
	 */
	public ScoreboardManager(final ScoreboardWrapper scoreboard)
	{
		this.scoreboard = scoreboard;
	}
	
	/**
	 * Adds player to this manager.
	 * 
	 * @param p
	 *            player
	 */
	public void addPlayer(final Player p)
	{
		p.setScoreboard(this.scoreboard.getScoreboard());
	}
	
	/**
	 * Removes player from this scoreboard manager.
	 * 
	 * @param p
	 *            player
	 */
	public void removePlayer(final Player p)
	{
		p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
	}
}
