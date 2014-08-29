package me.dobrakmato.plugins.pexel.PexelCore;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

/**
 * Simple one score per player scoreboard.
 * 
 * @author Mato Kormuth
 * 
 */
public class SimpleScoreboard implements ScoreboardWrapper
{
	private final Map<Player, Score>	scores	= new HashMap<Player, Score>();
	private final Scoreboard			board;
	private final Objective				objective;
	
	/**
	 * Creates a new simle player-score scoreboard.
	 * 
	 * @param name
	 *            name of objective
	 * @param title
	 *            title of scoreboard
	 */
	public SimpleScoreboard(final String name, final String title)
	{
		this.board = Bukkit.getScoreboardManager().getNewScoreboard();
		this.objective = this.board.registerNewObjective(name, "dummy");
		this.objective.setDisplayName(title);
		this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	}
	
	/**
	 * Sets player's score to specified amount.
	 * 
	 * @param player
	 * @param amount
	 */
	public void setScore(final Player player, final int amount)
	{
		if (this.scores.containsKey(player))
			this.scores.get(player).setScore(amount);
		else
		{
			this.scores.put(player, this.objective.getScore(player.getName()));
			this.scores.get(player).setScore(amount);
		}
	}
	
	/**
	 * Increments score for specified player with specified amount.
	 * 
	 * @param player
	 *            player
	 * @param amount
	 *            amount to incerement
	 */
	public void incrementScore(final Player player, final int amount)
	{
		if (this.scores.containsKey(player))
			this.scores.get(player).setScore(
					this.scores.get(player).getScore() + amount);
		else
		{
			this.scores.put(player, this.objective.getScore(player.getName()));
			this.scores.get(player).setScore(amount);
		}
	}
	
	/**
	 * Resets scoreboard.
	 */
	public void reset()
	{
		for (Score s : this.scores.values())
			s.setScore(0);
		this.scores.clear();
	}
	
	@Override
	public Scoreboard getScoreboard()
	{
		return this.board;
	}
}
