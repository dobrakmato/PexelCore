package eu.matejkormuth.pexel.PexelCore.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.entity.Player;

import eu.matejkormuth.pexel.PexelCore.chat.ChatManager;
import eu.matejkormuth.pexel.PexelCore.matchmaking.MatchmakingGame;
import eu.matejkormuth.pexel.PexelCore.matchmaking.MatchmakingRequest;
import eu.matejkormuth.pexel.PexelCore.minigame.Minigame;

/**
 * Class used for party.
 * 
 * @author Mato Kormuth
 * 
 */
public class Party implements PlayerHolder {
    private final List<Player> players;
    private final Player       owner;
    
    /**
     * Creates new party.
     * 
     * @param owner
     *            owner of the party
     */
    public Party(final Player owner) {
        this.players = new ArrayList<Player>();
        this.owner = owner;
        
        owner.sendMessage(ChatManager.success("Created new party!"));
    }
    
    /**
     * Returns whether is specified player owner of the party.
     * 
     * @param player
     *            specified player
     * @return true or false
     */
    public boolean isOwner(final Player player) {
        return this.owner == player;
    }
    
    /**
     * Adds player to party.
     * 
     * @param player
     *            player to be added
     */
    public void addPlayer(final Player player) {
        this.players.add(player);
        player.sendMessage(ChatManager.success("You have joined "
                + this.owner.getDisplayName() + "'s party!"));
    }
    
    /**
     * Makes {@link MatchmakingRequest} from this party.
     * 
     * @param minigame
     *            specified minigame
     * @param game
     *            specified arena
     * @return request
     */
    public MatchmakingRequest toRequest(final Minigame minigame,
            final MatchmakingGame game) {
        return new MatchmakingRequest(this.players, minigame, game);
    }
    
    @Override
    public Collection<Player> getPlayers() {
        return this.players;
    }
    
    @Override
    public int getPlayerCount() {
        return this.players.size();
    }
    
    public void removePlayer(final Player player) {
        this.players.remove(player);
        StorageEngine.getProfile(player.getUniqueId()).setParty(null);
    }
    
    @Override
    public boolean contains(final Player player) {
        return this.players.contains(player);
    }
}
