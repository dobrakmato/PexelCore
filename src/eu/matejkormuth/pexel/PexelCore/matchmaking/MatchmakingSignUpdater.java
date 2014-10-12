package eu.matejkormuth.pexel.PexelCore.matchmaking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import eu.matejkormuth.pexel.PexelCore.Pexel;
import eu.matejkormuth.pexel.PexelCore.arenas.SimpleArena;
import eu.matejkormuth.pexel.PexelCore.core.Log;
import eu.matejkormuth.pexel.PexelCore.core.Paths;
import eu.matejkormuth.pexel.PexelCore.core.StorageEngine;
import eu.matejkormuth.pexel.PexelCore.minigame.Minigame;
import eu.matejkormuth.pexel.PexelCore.util.BukkitTimer;

/**
 * Sign updater.
 */
public class MatchmakingSignUpdater implements Runnable {
    private final List<Block> cachedSigns = new ArrayList<Block>();
    private final BukkitTimer timer       = new BukkitTimer(20, this);
    
    public MatchmakingSignUpdater() {
        Log.partEnable("MatchmakingSignUpdater");
        this.loadCache();
        this.timer.start();
    }
    
    public void stop() {
        Log.partDisable("MatchmakingSignUpdater");
        this.timer.stop();
        this.save();
    }
    
    private void save() {
        try {
            Log.info("[MatchmakingSignUpdater] Saving cache...");
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
                    Paths.msuCache())));
            for (Block b : this.cachedSigns) {
                bw.append(b.getLocation().getWorld() + "|" + b.getLocation().getBlockX()
                        + "|" + b.getLocation().getBlockY() + "|"
                        + b.getLocation().getBlockZ() + "\n");
            }
            Log.info("[MatchmakingSignUpdater] Saved " + this.cachedSigns.size()
                    + " blocks!");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void addSign(final Block b) {
        this.cachedSigns.add(b);
    }
    
    private void loadCache() {
        Log.info("[MatchmakingSignUpdater] Loading cache...");
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(
                    Paths.msuCache())));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] elems = line.split("\\|");
                World w = Bukkit.getWorld(elems[0]);
                this.cachedSigns.add(w.getBlockAt(Integer.parseInt(elems[1]),
                        Integer.parseInt(elems[2]), Integer.parseInt(elems[3])));
            }
            Log.info("[MatchmakingSignUpdater] Loaded " + this.cachedSigns.size()
                    + " blocks from cache!");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void updateSigns() {
        for (Iterator<Block> iterator = this.cachedSigns.iterator(); iterator.hasNext();) {
            Block b = iterator.next();
            if (b.getChunk().isLoaded()) {
                if (b.getType() == Material.SIGN || b.getType() == Material.SIGN_POST) {
                    if (!this.updateSign(b)) {
                        iterator.remove();
                    }
                }
                else {
                    iterator.remove();
                }
            }
        }
    }
    
    private boolean updateSign(final Block b) {
        Sign sign = (Sign) b.getState();
        if (sign.getLine(0).contains("[Matchmaking]")) {
            Minigame minigame = StorageEngine.getMinigame(sign.getLine(1));
            if (minigame != null) {
                int arenaCountTotal = 0;
                int arenaCountJoinable = 0;
                int playersTotalOnline = 0;
                int playersTotalOnlineWaiting = 0;
                int playersTotal = 0;
                for (SimpleArena arena : Pexel.getMatchmaking().arenas.get(minigame)) {
                    if (arena.canJoin()) {
                        arenaCountJoinable++;
                        arenaCountTotal++;
                    }
                    else {
                        arenaCountTotal++;
                    }
                    
                    if (arena.getState() == GameState.WAITING_PLAYERS) {
                        playersTotalOnlineWaiting += arena.getPlayerCount();
                    }
                    
                    playersTotalOnline += arena.getPlayerCount();
                    playersTotal += arena.getMaximumSlots();
                }
                ChatColor arenasColor = ChatColor.GREEN;
                if (arenaCountJoinable == 0) {
                    arenasColor = ChatColor.RED;
                    sign.setLine(3, ChatColor.RED + "All arenas full!");
                }
                else {
                    sign.setLine(3, ChatColor.GREEN + "Click to join!");
                }
                
                sign.setLine(2, arenasColor.toString() + arenaCountJoinable + "/"
                        + arenaCountTotal + "" + ChatColor.BLUE + playersTotalOnline
                        + "/" + playersTotalOnlineWaiting + "/" + playersTotal);
            }
            else {
                sign.setLine(2, ChatColor.RED + "invalid minigame");
            }
            
            sign.update();
            return true;
        }
        else {
            return false;
        }
    }
    
    @Override
    public void run() {
        this.updateSigns();
    }
}
