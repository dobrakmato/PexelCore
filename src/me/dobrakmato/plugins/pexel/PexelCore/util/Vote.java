package me.dobrakmato.plugins.pexel.PexelCore.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Class used for voting.
 */
public abstract class Vote {
    private final List<Player>          voters;
    private final Map<Player, VoteType> votes           = new HashMap<Player, VoteType>();
    private final String                voteSubject;
    private long                        lastInteraction = Long.MAX_VALUE;
    private long                        timeout         = 5000;
    private boolean                     canVoteOnlyOnce = true;
    
    public Vote(final List<Player> voters, final Player invoker, final String voteSubject) {
        this.voters = voters;
        this.voteSubject = voteSubject;
        
        this.startVote(invoker);
        this.lastInteraction = System.currentTimeMillis();
    }
    
    /**
     * Sends message to all voters.
     */
    private void startVote(final Player invoker) {
        for (Player voter : this.voters) {
            voter.sendMessage(ChatColor.GOLD + "[VOTE] Player " + invoker.getName()
                    + " started the vote for " + this.voteSubject + ".");
        }
    }
    
    /**
     * Votes positively for specified player.
     * 
     * @param voter
     *            player that votes for subject
     */
    public void vote(final Player voter, final VoteType vote) {
        if (!this.voters.contains(voter))
            throw new RuntimeException("Invalid voter! Specified voter can't vote.");
        if (this.votes.containsKey(voter) && this.canVoteOnlyOnce)
            throw new RuntimeException("Invalid vote! One player can vote only once!");
        
        this.votes.put(voter, vote);
        this.broadcastState();
        
        this.lastInteraction = System.currentTimeMillis();
    }
    
    /**
     * Broadcast vote state to all players.
     */
    private void broadcastState() {
        int yesVotes = 0;
        for (VoteType value : this.votes.values())
            if (value == VoteType.YES)
                yesVotes++;
        
        for (Player voter : this.voters) {
            voter.sendMessage(ChatColor.GOLD + "[VOTE] " + yesVotes + "/"
                    + this.voters.size() + " players voted for " + this.voteSubject
                    + "!");
        }
    }
    
    // Abstract functions
    
    /**
     * Called when vote failed.
     */
    public abstract void onVoteFailed();
    
    /**
     * Called when vote succeeded.
     */
    public abstract void onVoteSucceeded();
    
    // Getters and setters
    
    public long getTimeout() {
        return this.timeout;
    }
    
    public void setTimeout(final long timeout) {
        this.timeout = timeout;
    }
    
    public boolean isCanVoteOnlyOnce() {
        return this.canVoteOnlyOnce;
    }
    
    public void setCanVoteOnlyOnce(final boolean canVoteOnlyOnce) {
        this.canVoteOnlyOnce = canVoteOnlyOnce;
    }
}
