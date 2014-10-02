package eu.matejkormuth.pexel.PexelCore.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;

public class TimeBomb {
    private final Sign             sign;
    private final Block            tntBlock;
    private int                    timeLeft = 60;
    private final SimpleDateFormat sdf      = new SimpleDateFormat("HH:mm:ss");
    private final BukkitTimer      timer;
    
    public TimeBomb(final Block tntblock, final Block sign, final int timeLeft) {
        this.timer = new BukkitTimer(20, new Runnable() {
            @Override
            public void run() {
                TimeBomb.this.tick();
            }
        });
        this.timer.start();
        
        this.sign = (Sign) sign;
        this.tntBlock = tntblock;
        this.timeLeft = timeLeft;
        this.sign.setLine(0, "====================");
        this.sign.setLine(2, "====================");
    }
    
    protected void tick() {
        this.update(this.timeLeft--);
    }
    
    public void update(final int timeLeft) {
        this.timeLeft = timeLeft;
        if (this.timeLeft < 10) {
            this.sign.setLine(1, ChatColor.RED + this.sdf.format(new Date(timeLeft))
                    + "." + ChatColor.MAGIC + "00");
        }
        else {
            this.sign.setLine(1, this.sdf.format(new Date(timeLeft)) + "."
                    + ChatColor.MAGIC + "00");
        }
        this.sign.update();
        if (this.timeLeft <= 0) {
            this.timer.stop();
            this.tntBlock.setType(Material.TNT);
            this.tntBlock.getLocation().getWorld().spawnEntity(
                    this.tntBlock.getLocation(), EntityType.PRIMED_TNT);
        }
    }
}
