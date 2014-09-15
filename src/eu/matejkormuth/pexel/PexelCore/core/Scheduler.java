// @formatter:off
/*
 * Pexel Project - Minecraft minigame server platform. 
 * Copyright (C) 2014 Matej Kormuth <http://www.matejkormuth.eu>
 * 
 * This file is part of Pexel.
 * 
 * Pexel is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * Pexel is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */
// @formatter:on
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
