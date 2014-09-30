package eu.matejkormuth.pexel.PexelCore.bans;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Basic Ban implementation.
 */
public class BanBase implements Ban {
    protected long      length = -1;
    protected String    reason;
    protected BanAuthor author;
    protected long      creationTime;
    protected UUID      uuid;
    protected Bannable  bannable;
    
    /**
     * Creates PERMANENT ban.
     * 
     * @param reason
     *            reason of ban
     * @param author
     *            author of ban
     * @param player
     *            player
     * @param bannable
     *            banned part
     */
    public BanBase(final String reason, final BanAuthor author, final Player player,
            final Bannable bannable) {
        this(-1, reason, author, System.currentTimeMillis(), player.getUniqueId(),
                bannable);
    }
    
    /**
     * Creates TEMPORARY ban.
     * 
     * @param length
     * @param reason
     * @param author
     * @param player
     * @param bannable
     */
    public BanBase(final long length, final String reason, final BanAuthor author,
            final Player player, final Bannable bannable) {
        this(length, reason, author, System.currentTimeMillis(), player.getUniqueId(),
                bannable);
    }
    
    public BanBase(final long length, final String reason, final BanAuthor author,
            final long creationTime, final UUID uuid, final Bannable bannable) {
        this.length = length;
        this.reason = reason;
        this.author = author;
        this.creationTime = creationTime;
        this.uuid = uuid;
        this.bannable = bannable;
    }
    
    @Override
    public boolean isPermanent() {
        return this.length == -1;
    }
    
    @Override
    public BanAuthor getAuthor() {
        return this.author;
    }
    
    @Override
    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }
    
    @Override
    public Bannable getBanned() {
        return this.bannable;
    }
    
    @Override
    public String getReason() {
        return this.reason;
    }
    
    @Override
    public long getLength() {
        return this.length;
    }
    
    @Override
    public long getCreationTime() {
        return this.creationTime;
    }
    
    @Override
    public long getExpirationTime() {
        return this.creationTime + this.length;
    }
    
    @Override
    public String toString() {
        return "BanBase{creationTime:" + this.creationTime + ", legth:" + this.length
                + ", reason:" + this.reason + ", author:" + this.author.getName()
                + ", BID:" + this.bannable.getBannableName() + ", BN:"
                + this.bannable.getBannableName() + ", uuid:" + this.uuid + "}";
    }
}
