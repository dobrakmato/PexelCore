package eu.matejkormuth.pexel.PexelCore.scoreboard;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import eu.matejkormuth.pexel.PexelCore.scoreboard.ScoreboardView;

public class TextScoreboard implements ScoreboardView {
    /**
     * Mapping scores.
     */
    private final Map<String, Score> scores = new HashMap<String, Score>();
    /**
     * Scoreboard.
     */
    private final Scoreboard         board;
    /**
     * Objective.
     */
    private final Objective          objective;
    
    /**
     * Creates a new simle player-score scoreboard.
     * 
     * @param name
     *            name of objective
     * @param title
     *            title of scoreboard
     */
    public TextScoreboard(final String name, final String title) {
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
    public void setScore(final String key, final int amount) {
        if (this.scores.containsKey(key))
            this.scores.get(key).setScore(amount);
        else {
            this.scores.put(key, this.objective.getScore(key));
            this.scores.get(key).setScore(amount);
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
    public void incrementScore(final String key, final int amount) {
        if (this.scores.containsKey(key))
            this.scores.get(key).setScore(this.scores.get(key).getScore() + amount);
        else {
            this.scores.put(key, this.objective.getScore(key));
            this.scores.get(key).setScore(amount);
        }
    }
    
    /**
     * Resets scoreboard.
     */
    @Override
    public void reset() {
        for (Score s : this.scores.values())
            s.setScore(0);
        this.scores.clear();
    }
    
    @Override
    public Scoreboard getScoreboard() {
        return this.board;
    }
}
