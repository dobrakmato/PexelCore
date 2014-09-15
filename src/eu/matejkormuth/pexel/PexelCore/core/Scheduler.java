package eu.matejkormuth.pexel.PexelCore.core;

import org.bukkit.Bukkit;

import eu.matejkormuth.pexel.PexelCore.Pexel;

/**
 * Scheduler class for pexel to be used when porting from Bukkit to Sponge..
 */
public class Scheduler {
    public Scheduler() {
        Log.partEnable("Scheduler");
    }
    
    public int scheduleSyncRepeatingTask(final Runnable runnable, final long delay,
            final long interval) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(Pexel.getCore(),
                runnable, delay, interval);
    }
    
    public int scheduleSyncDelayedTask(final Runnable runnable, final long delay) {
        return Bukkit.getScheduler().scheduleSyncDelayedTask(Pexel.getCore(), runnable,
                delay);
    }
    
    public void cancelTask(final int taskId) {
        Bukkit.getScheduler().cancelTask(taskId);
    }
    
    public void tick() {
        // TODO: Try to complete tasks.
    }
}
