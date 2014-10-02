package eu.matejkormuth.pexel.PexelCore.util;

import eu.matejkormuth.pexel.PexelCore.Pexel;

/**
 * Timer implemented over bukkit tasks.
 */
public class BukkitTimer {
    private final int      interval;
    private int            taskId;
    private final Runnable onTick;
    
    public BukkitTimer(final int interval, final Runnable onTick) {
        this.interval = interval;
        this.onTick = onTick;
    }
    
    public boolean isRunning() {
        return this.taskId == 0;
    }
    
    public void start() {
        this.taskId = Pexel.getScheduler().scheduleSyncRepeatingTask(this.onTick, 0L,
                this.interval);
    }
    
    public void stop() {
        Pexel.getScheduler().cancelTask(this.taskId);
        this.taskId = 0;
    }
}
