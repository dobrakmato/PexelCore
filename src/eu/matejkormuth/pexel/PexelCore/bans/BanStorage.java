package eu.matejkormuth.pexel.PexelCore.bans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

/**
 * Class used as ban storage.
 */
public class BanStorage {
    private final HashMap<UUID, List<BanBase>> activeBans  = new HashMap<UUID, List<BanBase>>();
    private final HashMap<UUID, List<BanBase>> historyBans = new HashMap<UUID, List<BanBase>>();
    
    public void addBan(final BanBase ban) {
        if (this.activeBans.containsKey(ban.uuid)) {
            this.activeBans.get(ban.uuid).add(ban);
        }
        else {
            this.activeBans.put(ban.uuid, new ArrayList<BanBase>());
            this.activeBans.get(ban.uuid).add(ban);
        }
    }
    
    /**
     * Return if is player banned on specified part.
     * 
     * @param player
     *            player
     * @param part
     *            bannable part
     * @return true or false
     */
    public boolean isBanned(final Player player, final Bannable part) {
        if (!this.activeBans.containsKey(player.getUniqueId())) {
            return false;
        }
        else {
            for (Iterator<BanBase> iterator = this.activeBans.get(player.getUniqueId()).iterator(); iterator.hasNext();) {
                BanBase ban = iterator.next();
                if (ban.getBanned() == part)
                    if (ban.isPermanent()) {
                        return true;
                    }
                    else {
                        if (ban.getExpirationTime() < System.currentTimeMillis()) {
                            if (this.historyBans.containsKey(player.getUniqueId())) {
                                this.historyBans.get(player.getUniqueId()).add(ban);
                            }
                            else {
                                this.historyBans.put(player.getUniqueId(),
                                        new ArrayList<BanBase>());
                                this.historyBans.get(player.getUniqueId()).add(ban);
                            }
                            iterator.remove();
                            return false;
                        }
                        else {
                            return true;
                        }
                    }
            }
            return false;
        }
    }
    
    /**
     * Returns ban or null.
     * 
     * @param player
     *            specified player
     * @param part
     *            specified part
     * @return ban or null
     */
    public BanBase getBan(final Player player, final Bannable part) {
        if (!this.activeBans.containsKey(player.getUniqueId())) {
            return null;
        }
        else {
            for (BanBase ban : this.activeBans.get(player.getUniqueId())) {
                if (ban.getBanned() == part)
                    return ban;
            }
            return null;
        }
    }
    
    /**
     * Retruns list of bans by specified player.
     * 
     * @param player
     *            specififed player.
     * @return
     */
    public List<BanBase> getBans(final Player player) {
        return this.activeBans.get(player.getUniqueId());
    }
    
    public void save() {
        
    }
    
    public void load() {
        
    }
    
    public List<BanBase> getBans() {
        List<BanBase> fullbans = new ArrayList<BanBase>();
        for (List<BanBase> bans : this.activeBans.values())
            fullbans.addAll(bans);
        return fullbans;
    }
}
